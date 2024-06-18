package com.example.c001apk.compose.util

import android.util.Base64
import com.example.c001apk.compose.constant.Constants
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.util.Utils.getBase64
import com.example.c001apk.compose.util.Utils.getMD5
import org.mindrot.jbcrypt.BCrypt
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.Random


object TokenDeviceUtils {

    fun encode(deviceInfo: String): String {
        val charset: Charset = StandardCharsets.UTF_8
        val bytes = deviceInfo.toByteArray(charset)
        val encodeToString = Base64.encodeToString(bytes, 0)
        val replace = StringBuilder(encodeToString).reverse().toString()
        return Regex("\\r\\n|\\r|\\n|=").replace(replace, EMPTY_STRING)
    }

    fun randHexString(@Suppress("SameParameterValue") n: Int): String {
        Random().setSeed(System.currentTimeMillis())
        return (0 until n).joinToString("") {
            Random().nextInt(256).toString(16)
        }.uppercase()
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

}