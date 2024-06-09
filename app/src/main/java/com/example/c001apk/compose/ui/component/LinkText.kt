package com.example.c001apk.compose.ui.component

import android.graphics.Typeface
import android.text.TextUtils
import android.widget.TextView
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.core.text.method.LinkMovementMethodCompat
import com.example.c001apk.compose.logic.providable.LocalUserPreferences
import com.example.c001apk.compose.view.LinkTextView

/**
 * Created by bggRGjQaUbCoE on 2024/6/2
 */
@Composable
fun LinkText(
    modifier: Modifier = Modifier,
    text: String,
    textSize: Float = 15f,
    isBold: Boolean = false,
    lineSpacingMultiplier: Float = 1.0f,
    maxLines: Int? = null,
    ellipsize: TextUtils.TruncateAt = TextUtils.TruncateAt.END,
    color: Int? = null,
    onOpenLink: (String) -> Unit,
    isReply: Boolean = false,
    showTotalReply: (() -> Unit)? = null,
    imgList: List<String>? = null,
) {
    val contentColor = LocalContentColor.current
    val userPreference = LocalUserPreferences.current

    val primary = MaterialTheme.colorScheme.primary.toArgb()
    AndroidView(
        modifier = modifier,
        factory = {
            LinkTextView(it)
        },
        update = { textView ->
            textView.setSpText(
                isReply = isReply,
                text = text,
                color = primary,
                onOpenLink = onOpenLink,
                showTotalReply = showTotalReply,
                size = textSize,
                fontScale = userPreference.fontScale,
                imgList = imgList
            )
            textView.setTextColor(contentColor.toArgb())
            if (isBold)
                textView.setTypeface(textView.typeface, Typeface.BOLD)
            textView.setLineSpacing(0.0f, lineSpacingMultiplier)
            maxLines?.let {
                textView.maxLines = it
                textView.ellipsize = ellipsize
            }
            color?.let {
                textView.setTextColor(it)
            }
        }
    )
}

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    val contentColor = LocalContentColor.current
    AndroidView(
        modifier = modifier,
        factory = { context ->
            TextView(context).also {
                it.movementMethod = LinkMovementMethodCompat.getInstance()
            }
        },
        update = {
            it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
            it.setTextColor(contentColor.toArgb())
        }
    )
}