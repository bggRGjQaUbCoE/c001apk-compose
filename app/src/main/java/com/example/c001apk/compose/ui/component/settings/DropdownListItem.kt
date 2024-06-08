package com.example.c001apk.compose.ui.component.settings

import androidx.compose.foundation.background
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING

/**
 * Used for selections
 * If label is null, then value will be displayed on the screen
 */
data class SelectionItem<T>(val label: String, val value: T)

@Composable
fun <T> DropdownListItem(
    value: T?,
    leadingImageVector: ImageVector? = null,
    headlineText: String,
    selections: List<SelectionItem<T>>,
    onValueChanged: (index: Int, value: T) -> Unit,
) {
    var dropdownMenuExpanded by remember { mutableStateOf(false) }
    BasicListItem(
        headlineText = headlineText,
        supportingText = selections.find { it.value == value }?.label ?: EMPTY_STRING,
        onClick = { dropdownMenuExpanded = true },
        leadingImageVector = leadingImageVector,
        trailingContent = {
            DropdownMenu(
                expanded = dropdownMenuExpanded,
                onDismissRequest = { dropdownMenuExpanded = false },
            ) {
                selections.forEachIndexed { index, selection ->
                    DropdownMenuItem(
                        modifier = Modifier.background(
                            if (selection.value == value) MaterialTheme.colorScheme.surfaceVariant
                            else Color.Transparent
                        ),
                        text = { Text(selection.label) },
                        onClick = {
                            dropdownMenuExpanded = false
                            onValueChanged(index, selection.value)
                        }
                    )
                }
            }
        },
    )
}