package com.example.c001apk.compose.util

import android.content.Context
import android.util.Base64
import com.example.c001apk.compose.constant.Constants
import com.example.c001apk.compose.util.Utils.getBase64
import com.example.c001apk.compose.util.Utils.getMD5
import com.example.c001apk.compose.util.Utils.randomAndroidVersionRelease
import com.example.c001apk.compose.util.Utils.randomBrand
import com.example.c001apk.compose.util.Utils.randomDeviceModel
import com.example.c001apk.compose.util.Utils.randomManufacturer
import com.example.c001apk.compose.util.Utils.randomSdkInt
import org.mindrot.jbcrypt.BCrypt
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.Random


object TokenDeviceUtils {

    private fun encode(deviceInfo: String): String {
        val charset: Charset = StandardCharsets.UTF_8
        val bytes = deviceInfo.toByteArray(charset)
        val encodeToString = Base64.encodeToString(bytes, 0)
        val replace = StringBuilder(encodeToString).reverse().toString()
        return Regex("\\r\\n|\\r|\\n|=").replace(replace, "")
    }

    private fun randHexString(@Suppress("SameParameterValue") n: Int): String {
        Random().setSeed(System.currentTimeMillis())
        return (0 until n).joinToString("") {
            Random().nextInt(256).toString(16)
        }.uppercase()
    }

    fun getDeviceCode(regenerate: Boolean): String {
        if (regenerate) {
            PrefManager.apply {
                manufacturer = randomManufacturer()
                brand = randomBrand()
                model = randomDeviceModel()
                buildNumber = randHexString(16)
                sdkInt = randomSdkInt()
                androidVersion = randomAndroidVersionRelease()
                userAgent =
                "Dalvik/2.1.0 (Linux; U; Android $androidVersion; $model $buildNumber) (#Build; $brand; $model; $buildNumber; $androidVersion) +CoolMarket/$versionName-$versionCode-${Constants.MODE}"
            }
        }
        val szlmId = PrefManager.szlmId.ifEmpty { randHexString(16) }
        val mac = Utils.randomMacAddress()
        val manuFactor = PrefManager.manufacturer
        val brand = PrefManager.brand
        val model = PrefManager.model
        val buildNumber = PrefManager.buildNumber
        return encode("$szlmId; ; ; $mac; $manuFactor; $brand; $model; $buildNumber; null")
    }

    fun String.getTokenV2(): String {
        val timeStamp = (System.currentTimeMillis() / 1000f).toString()

        val base64TimeStamp = timeStamp.getBase64()
        val md5TimeStamp = timeStamp.getMD5()
        val md5DeviceCode = this.getMD5()

        val token = "${Constants.APP_LABEL}?$md5TimeStamp$$md5DeviceCode&${Constants.APP_ID}"
        val base64Token = token.getBase64()
        val md5Base64Token = base64Token.getMD5()
        val md5Token = token.getMD5()

        val bcryptSalt = "${"$2a$10$$base64TimeStamp/$md5Token".substring(0, 31)}u"
        val bcryptResult = BCrypt.hashpw(md5Base64Token, bcryptSalt)

        return "v2${bcryptResult.replaceRange(0, 3, "$2y").getBase64()}"
    }

    fun getLastingDeviceCode(): String {
        if (PrefManager.xAppDevice == "")
            PrefManager.xAppDevice = getDeviceCode(true)
        return PrefManager.xAppDevice
    }

    fun getLastingInstallTime(context: Context): String {
        val sp = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        return sp.getString("INSTALL_TIME", null).let {
            it ?: System.currentTimeMillis().toString().apply {
                sp.edit().putString("INSTALL_TIME", this).apply()
            }
        }
    }

}