package com.example.c001apk.compose.ui.component

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.c001apk.compose.constant.Constants.SUFFIX_GIF
import com.example.c001apk.compose.logic.providable.LocalUserPreferences
import com.example.c001apk.compose.util.CookieUtil
import com.example.c001apk.compose.util.http2https
import jp.wasabeef.transformers.coil.ColorFilterTransformation

/**
 * Created by bggRGjQaUbCoE on 2024/6/11
 */
@Composable
fun CoilLoader(
    modifier: Modifier = Modifier,
    url: String?,
    colorFilter: Long? = null,
) {
    val prefs = LocalUserPreferences.current
    url?.let {
        val context = LocalContext.current
        val imageUrl = it.http2https
        AsyncImage(
            model = ImageRequest.Builder(context)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCacheKey(imageUrl)
                .diskCacheKey(imageUrl)
                .data(imageUrl)
                .apply {
                    if (it.endsWith(SUFFIX_GIF)) {
                        decoderFactory(
                            if (Build.VERSION.SDK_INT >= 28) {
                                ImageDecoderDecoder.Factory()
                            } else {
                                GifDecoder.Factory()
                            }
                        )
                    }
                    colorFilter?.let {
                        transformations(ColorFilterTransformation(Color(colorFilter).toArgb()))
                    }
                    if (!it.endsWith(SUFFIX_GIF) && colorFilter == null && prefs.isDarkMode() && CookieUtil.imageFilter) {
                        transformations(ColorFilterTransformation(Color(0x2D000000).toArgb()))
                    }
                }
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier,
        )
    }
}