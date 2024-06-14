package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.ui.component.CoilLoader
import com.example.c001apk.compose.ui.component.LinkText
import com.example.c001apk.compose.util.DateUtils.fromToday

/**
 * Created by bggRGjQaUbCoE on 2024/6/13
 */
@Composable
fun MessageCard(
    modifier: Modifier = Modifier,
    data: HomeFeedResponse.Data,
    onOpenLink: (String, String?) -> Unit,
) {

    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp))
            .clickable {

            }
            .padding(10.dp)
    ) {

        val (avatar, name, message, time, badge) = createRefs()

        CoilLoader(
            url = data.messageUserAvatar,
            modifier = Modifier
                .aspectRatio(1f)
                .clip(CircleShape)
                .constrainAs(avatar) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
        )

        Text(
            text = data.messageUsername.orEmpty(),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .constrainAs(name) {
                    start.linkTo(avatar.end)
                    top.linkTo(parent.top)
                    end.linkTo(time.start)
                    width = Dimension.fillToConstraints
                },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 14.sp)
        )

        Text(
            text = fromToday(data.dateline ?: 0),
            modifier = Modifier
                .constrainAs(time) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                },
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp)
        )

        LinkText(
            text = data.message.orEmpty(),
            onOpenLink = onOpenLink,
            maxLines = 1,
            modifier = Modifier
                .padding(start = 10.dp, end = if (data.unreadNum != 0) 10.dp else 0.dp)
                .constrainAs(message) {
                    start.linkTo(avatar.end)
                    top.linkTo(name.bottom)
                    end.linkTo(if (data.unreadNum != 0) badge.start else parent.end)
                    width = Dimension.fillToConstraints
                }
        )

        if (data.unreadNum != 0) {
            Badge(
                modifier = Modifier
                    .constrainAs(badge) {
                        top.linkTo(name.bottom)
                        end.linkTo(parent.end)
                    }
            ) {
                Text(text = data.unreadNum.toString())
            }
        }

    }

}