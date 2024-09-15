package com.example.c001apk.compose.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build.VERSION.SDK_INT
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import com.example.c001apk.compose.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream


/**
 * Created by bggRGjQaUbCoE on 2024/6/7
 */
object ImageDownloadUtil {

    suspend fun downloadImage(
        context: Context,
        imageUrl: String,
        fileName: String,
        isEnd: Boolean,
        isShare: Boolean = false,
        userAgent: String?,
    ) {
        val imageLoader = ImageLoader.Builder(context).build()

        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .addHeader("User-Agent", userAgent ?: "")
            .target(
                onSuccess = { drawable ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val result =
                            if (isShare || SDK_INT < 29) {
                                saveImageBelowQ(context, drawable, fileName, isShare)
                            } else {
                                saveImageAboveQ(context, drawable, fileName)
                            }
                        if (!isShare && isEnd) {
                            withContext(Dispatchers.Main) {
                                context.makeToast(
                                    if (result) "Image saved successfully"
                                    else "Failed to save image"
                                )
                            }
                        }
                        if (isShare && result) {
                            ImageShowUtil.shareImage(
                                context,
                                File(context.externalCacheDir, "imageShare/$fileName"),
                                null
                            )
                        }
                    }
                },
                onError = {
                    Toast.makeText(context, "Failed to download image", Toast.LENGTH_SHORT).show()
                }
            )
            .build()

        imageLoader.enqueue(request)
    }

    private fun saveImageAboveQ(
        context: Context,
        drawable: Drawable,
        fileName: String
    ): Boolean {
        return try {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.DESCRIPTION, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(
                    MediaStore.Images.Media.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + "/${context.getString(R.string.app_name)}/"
                )
            }
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                ?.let { uri ->
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        drawable.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    } ?: false
                } ?: false
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private suspend fun saveImageBelowQ(
        context: Context,
        drawable: Drawable,
        fileName: String,
        isShare: Boolean
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val imagesDir =
                // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                Environment.getExternalStorageDirectory().toString() +
                        "/${context.getString(R.string.app_name)}/"
            val imageFile =
                if (isShare) File(context.externalCacheDir, "/imageShare/$fileName") else File(
                    imagesDir,
                    fileName
                )
            if (imageFile.parentFile?.exists() == false)
                imageFile.parentFile?.mkdirs()

            try {
                FileOutputStream(imageFile).use { outputStream ->
                    drawable.toBitmap().compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }


}