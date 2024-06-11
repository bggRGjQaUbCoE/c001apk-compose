package com.example.c001apk.compose.ui.component

import android.graphics.Color
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.c001apk.compose.constant.Constants.SUFFIX_GIF
import com.example.c001apk.compose.util.http2https
import jp.wasabeef.transformers.coil.ColorFilterTransformation

/**
 * Created by bggRGjQaUbCoE on 2024/6/11
 */
@Composable
fun CoilLoader(
    modifier: Modifier = Modifier,
    url: String?,
    colorFilter: Int? = null,
) {
    url?.let {
        val context = LocalContext.current
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(it.http2https)
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
                        transformations(ColorFilterTransformation(it))
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