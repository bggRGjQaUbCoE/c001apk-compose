package com.example.c001apk.compose.ui.component.settings

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun SwitchListItem(
    value: Boolean,
    leadingImageVector: ImageVector,
    headlineText: String,
    supportingText: String? = null,
    onValueChanged: (value: Boolean) -> Unit
) {
    BasicListItem(
        leadingImageVector = leadingImageVector,
        headlineText = headlineText,
        supportingText = supportingText,
        trailingContent = {
            Switch(
                checked = value,
                onCheckedChange = {
                    onValueChanged(it)
                },
            )
        }
    ) {
        onValueChanged(!value)
    }
}