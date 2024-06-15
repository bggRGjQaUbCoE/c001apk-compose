package com.example.c001apk.compose.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
    isConstraint: Boolean = false,
    isLike: Boolean = false,
) {

    val color = if (isLike) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.outline

    ConstraintLayout(
        modifier = run {
            val tmp = if (onClick == null) modifier
            else modifier
                .noRippleClickable {
                    onClick()
                }
            tmp
        }
    ) {
        val (icon, text) = createRefs()
        if (title.isNotEmpty()) {
            Image(
                imageVector = imageVector,
                contentDescription = null,
                colorFilter = ColorFilter.tint(color),
                modifier = Modifier
                    .padding(vertical = 1.dp)
                    .aspectRatio(1f)
                    .constrainAs(icon) {
                        start.linkTo(parent.start)
                        top.linkTo(text.top)
                        bottom.linkTo(text.bottom)
                        height = Dimension.fillToConstraints
                    }
            )
        }

        Text(
            text = title,
            lineHeight = textSize.sp,
            fontSize = textSize.sp,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(text) {
                    start.linkTo(icon.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    if (isConstraint) {
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                }
        )
    }

}