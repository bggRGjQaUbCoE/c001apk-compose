package com.example.c001apk.compose.ui.component

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.example.c001apk.compose.util.noRippleClickable

/**
 * Created by bggRGjQaUbCoE on 2024/6/9
 */
@Composable
fun IconText(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    title: String,
    textSize: Float = 14f,
    onClick: (() -> Unit)? = null,
    isLike: Boolean = false,
) {

    val color = if (isLike) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.outline

    val id = "0"
    val text1 = buildAnnotatedString {
        if (title.isNotEmpty()) appendInlineContent(id, "[icon]")
        append(title)
    }

    val inlineContent = if (title.isNotEmpty()) mapOf(
        Pair(
            id,
            InlineTextContent(
                Placeholder(
                    width = textSize.sp,
                    height = textSize.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter
                )
            ) {
                Icon(imageVector, null, tint = color)
            }
        )
    ) else mapOf()

    Text(
        inlineContent = inlineContent,
        text = text1,
        lineHeight = textSize.sp,
        fontSize = textSize.sp,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = run {
            val tmp = if (onClick == null) modifier
            else modifier
                .noRippleClickable {
                    onClick()
                }
            tmp
        }
    )

}