package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.res.ResourcesCompat
import com.example.c001apk.compose.R
import com.google.accompanist.drawablepainter.rememberDrawablePainter

/**
 * Created by bggRGjQaUbCoE on 2024/6/4
 */
@Composable
fun AppInfoCard() {
    val context = LocalContext.current

    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {

        val (logo, appName, version, size, updateTime, downloadBtn) = createRefs()

        Image(
            painter = rememberDrawablePainter(
                drawable = ResourcesCompat.getDrawable(
                    context.resources,
                    R.mipmap.ic_launcher,
                    context.theme
                )
            ),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(start = 20.dp)
                .height(80.dp)
                .width(80.dp)
                .clip(RoundedCornerShape(18.dp))
                .constrainAs(logo) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        Text(
            text = "appName",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
            modifier = Modifier
                .padding(start = 10.dp, top = 20.dp, end = 10.dp)
                .constrainAs(appName) {
                    start.linkTo(logo.end)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )

        Text(
            text = "version: 1.0.0(1)",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            modifier = Modifier
                .padding(start = 10.dp, top = 5.dp, end = 10.dp)
                .constrainAs(version) {
                    start.linkTo(logo.end)
                    top.linkTo(appName.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )

        Text(
            text = "size: 100M",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(start = 10.dp, top = 5.dp, end = 10.dp)
                .constrainAs(size) {
                    start.linkTo(logo.end)
                    top.linkTo(version.bottom)
                    end.linkTo(downloadBtn.start)
                    width = Dimension.fillToConstraints
                }
        )

        Text(
            text = "updateTime: 1 min ago",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(start = 10.dp, top = 5.dp, end = 10.dp, bottom = 20.dp)
                .constrainAs(updateTime) {
                    start.linkTo(logo.end)
                    top.linkTo(size.bottom)
                    end.linkTo(downloadBtn.start)
                    width = Dimension.fillToConstraints
                }
        )

        FilledTonalButton(
            onClick = {
                // TODO: onDownloadApk
            },
            modifier = Modifier
                .padding(end = 10.dp, bottom = 20.dp)
                .constrainAs(downloadBtn) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
        ) {
            Text(text = "下载")
        }

    }

}