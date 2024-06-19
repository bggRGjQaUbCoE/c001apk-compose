package com.example.c001apk.compose.util

import android.content.ContentResolver
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.IOException
import java.io.InputStream
import java.security.MessageDigest

/**
 * Created by bggRGjQaUbCoE on 2024/6/7
 */
object OSSUtil {

    fun getImageDimensionsAndMD5(
        contentResolver: ContentResolver,
        uri: Uri
    ): Pair<Triple<Int, Int, String>?, ByteArray?> {
        var dimensions: Triple<Int, Int, String>? = null
        var md5Hash: ByteArray? = null

        try {
            dimensions = contentResolver.openInputStream(uri)?.use { inputStream ->
                val options = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                }
                BitmapFactory.decodeStream(inputStream, null, options)
                var width = options.outWidth
                var height = options.outHeight
                with(getRotation(contentResolver, uri)) {
                    if (this == 90 || this == 270) {
                        width = height.apply {
                            height = width
                        }
                    }
                }
                Triple(width, height, options.outMimeType)
            }
            md5Hash = contentResolver.openInputStream(uri)?.use { inputStream ->
                calculateMD5(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Pair(dimensions, md5Hash)
    }

    private fun getRotation(
        contentResolver: ContentResolver,
        uri: Uri
    ): Int {
        var rotation = 0
        try {
            val exif = contentResolver.openInputStream(uri)?.let { ExifInterface(it) }
            val orientation = exif?.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            rotation = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rotation
    }

    private fun calculateMD5(input: InputStream): ByteArray {
        val md = MessageDigest.getInstance("MD5")
        val buffer = ByteArray(8192)
        var bytesRead = input.read(buffer)
        while (bytesRead > 0) {
            md.update(buffer, 0, bytesRead)
            bytesRead = input.read(buffer)
        }
        return md.digest()
    }

    fun ByteArray.toHex(): String {
        val hexString = StringBuilder()
        for (byte in this) {
            hexString.append(String.format("%02x", byte))
        }
        return hexString.toString()
    }

}