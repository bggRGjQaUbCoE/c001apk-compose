package com.example.c001apk.compose.ui.component

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import coil.drawable.CrossfadeDrawable
import coil.load
import com.example.c001apk.compose.constant.Constants.PREFIX_HTTP
import com.example.c001apk.compose.logic.providable.LocalUserPreferences
import com.example.c001apk.compose.util.CookieUtil.token
import com.example.c001apk.compose.util.CookieUtil.uid
import com.example.c001apk.compose.util.CookieUtil.username
import com.example.c001apk.compose.util.ImageShowUtil.startBigImgViewSimple
import com.example.c001apk.compose.util.dp
import com.example.c001apk.compose.util.http2https
import com.example.c001apk.compose.view.RoundedImageView
import jp.wasabeef.transformers.coil.ColorFilterTransformation

/**
 * Created by bggRGjQaUbCoE on 2024/6/10
 */
@Composable
fun ImageView(
    modifier: Modifier = Modifier,
    url: String,
    isRound: Boolean = false,
    borderWidth: Float? = null,
    borderColor: Int? = null,
    isCover: Boolean = false,
    isChat: Boolean = false,
    onClearFocus: (() -> Unit)? = null,
) {

    val prefs = LocalUserPreferences.current
    val isDarkMode = prefs.isDarkMode()
    val cookie by lazy { "uid=$uid; username=$username; token=$token" }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            RoundedImageView(context).apply {
                scaleType = android.widget.ImageView.ScaleType.CENTER_CROP
                if (isRound) {
                    setCornerRadius(1000)
                }
                borderWidth?.let {
                    setBorderWidth(it.dp)
                }
                borderColor?.let {
                    setBorderColor(it)
                }

            }
        },
        update = { imageView ->
            imageView.setOnClickListener {
                if (isChat) {
                    onClearFocus?.let { it() }
                }
                if (url.startsWith(PREFIX_HTTP)) {
                    startBigImgViewSimple(
                        imageView,
                        url = if (isChat) url else url.http2https,
                        cookie = if (isChat) cookie else null,
                        userAgent = prefs.userAgent,
                    )
                }
            }
            imageView.load(if (isChat) url else url.http2https) {
                crossfade(CrossfadeDrawable.DEFAULT_DURATION)
                addHeader("User-Agent", prefs.userAgent)
                if (isChat) {
                    addHeader("Cookie", cookie)
                }
                if (isCover) {
                    transformations(
                        ColorFilterTransformation(
                            Color.parseColor(
                                if (isDarkMode)
                                    "#8D000000"
                                else
                                    "#5DFFFFFF"
                            )
                        )
                    )
                }
            }
        }
    )

}