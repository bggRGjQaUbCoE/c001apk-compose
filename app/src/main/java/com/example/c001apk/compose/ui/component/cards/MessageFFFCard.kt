package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING

/**
 * Created by bggRGjQaUbCoE on 2024/6/10
 */

data class FFFCardItem(
    val title: String,
    val value: String?,
    val imageVector: ImageVector?
)

@Composable
fun MessageFFFCard(
    modifier: Modifier = Modifier,
    fffList: List<String>
) {
    FFFCardRow(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)),
        dataList = listOf(
            FFFCardItem(title = "动态", value = fffList.getOrNull(0), imageVector = null),
            FFFCardItem(title = "关注", value = fffList.getOrNull(1), imageVector = null),
            FFFCardItem(title = "粉丝", value = fffList.getOrNull(2), imageVector = null)
        )
    )
}

@Composable
fun FFFCardRow(
    modifier: Modifier = Modifier,
    dataList: List<FFFCardItem>,
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
    ) {
        dataList.forEachIndexed { index, item ->
            FFFCardItem(
                modifier = Modifier.weight(1f),
                onClick = {

                },
                title = item.title,
                value = item.value,
                imageVector = item.imageVector
            )
            if (index != 2)
                VerticalDivider(modifier = Modifier.padding(vertical = 14.dp))
        }
    }
}

@Composable
fun FFFCardItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    title: String,
    value: String?,
    imageVector: ImageVector?
) {
    Column(
        modifier = modifier
            .clickable {
                onClick()
            }
            .padding(vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (imageVector == null) {
            Text(
                text = value ?: EMPTY_STRING,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        } else {
            Image(
                imageVector = imageVector,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.alpha(if (value.isNullOrEmpty() && imageVector == null) 0f else 1f)
        )
    }
}