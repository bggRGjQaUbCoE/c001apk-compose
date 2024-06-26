package com.example.c001apk.compose.ui.component.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun StateBasicListItem(
    modifier: Modifier = Modifier,
    isEnable: Boolean = true,
    headlineText: String? = null,
    supportingText: String? = null,
    leadingImageVector: ImageVector? = null,
    leadingPainter: Painter? = null,
    leadingText: String? = null,
    trailingContent: @Composable () -> Unit = {},
    onClick: (() -> Unit)? = null,
) {
    val textColor =
        if (isEnable)
            MaterialTheme.colorScheme.onSurface
        else
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    val iconColor =
        if (isEnable)
            MaterialTheme.colorScheme.onSurfaceVariant
        else
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
    ListItem(
        modifier = onClick?.let {
            if (isEnable) {
                modifier.clickable(onClick = onClick)
            } else modifier
        } ?: modifier,
        headlineContent = {
            if (headlineText != null) {
                Text(headlineText, color = textColor)
            }
        },
        supportingContent = {
            if (supportingText != null) {
                Text(supportingText, color = iconColor)
            }
        },
        leadingContent = {
            if (leadingText != null) {
                Text(
                    text = leadingText,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleSmall
                )
            } else if (leadingImageVector != null) {
                Icon(
                    imageVector = leadingImageVector,
                    contentDescription = null,
                    tint = iconColor
                )
            } else if (leadingPainter != null) {
                Image(
                    painter = leadingPainter, contentDescription = null, modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .size(24.dp)
                )
            }
        },
        trailingContent = trailingContent
    )
}