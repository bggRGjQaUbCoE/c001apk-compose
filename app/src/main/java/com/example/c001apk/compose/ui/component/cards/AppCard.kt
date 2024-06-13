package com.example.c001apk.compose.ui.component.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.c001apk.compose.util.DateUtils.fromToday

/**
 * Created by bggRGjQaUbCoE on 2024/6/10
 */

enum class AppCardType {
    APP, PRODUCT, TOPIC, USER, CONTACTS, RECENT
}

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    data: HomeFeedResponse.Data,
    onOpenLink: (String, String?) -> Unit,
    appCardType: AppCardType,
    isHomeFeed: Boolean = false,
    onViewUser: (String) -> Unit,
) {

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isHomeFeed) MaterialTheme.colorScheme.surface
                else MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
            )
            .clickable {
                if (appCardType == AppCardType.CONTACTS)
                    onViewUser(data.userInfo?.uid ?: data.fUserInfo?.uid.orEmpty())
                else
                    onOpenLink(data.url.orEmpty(), data.title)
            }
            .padding(10.dp)
    ) {

        val (logo, appName, commentNum, downCount, active, follow) = createRefs()

        CoilLoader(
            url = when (appCardType) {
                AppCardType.APP, AppCardType.PRODUCT, AppCardType.TOPIC, AppCardType.RECENT -> data.logo
                AppCardType.USER -> data.userAvatar
                AppCardType.CONTACTS -> data.userInfo?.userAvatar ?: data.fUserInfo?.userAvatar
            },
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .aspectRatio(1f)
                .constrainAs(logo) {
                    start.linkTo(parent.start)
                    top.linkTo(appName.top)
                    bottom.linkTo(commentNum.bottom)
                    height = Dimension.fillToConstraints
                },
        )

        Text(
            text = when (appCardType) {
                AppCardType.APP, AppCardType.PRODUCT, AppCardType.TOPIC -> data.title.orEmpty()
                AppCardType.USER -> data.username.orEmpty()
                AppCardType.CONTACTS ->
                    data.userInfo?.username ?: data.fUserInfo?.username.orEmpty()

                AppCardType.RECENT -> "${data.targetTypeTitle}: ${data.title}"
            },
            modifier = Modifier
                .padding(start = 10.dp, end = if (appCardType == AppCardType.USER) 10.dp else 0.dp)
                .constrainAs(appName) {
                    start.linkTo(logo.end)
                    top.linkTo(parent.top)
                    end.linkTo(if (appCardType == AppCardType.USER) follow.start else parent.end)
                    width = Dimension.fillToConstraints
                },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Text(
            text = when (appCardType) {
                AppCardType.APP -> "${data.commentnum}讨论"
                AppCardType.PRODUCT, AppCardType.TOPIC -> "${data.hotNumTxt}热度"
                AppCardType.USER -> "${data.follow}关注"
                AppCardType.CONTACTS -> "${data.userInfo?.follow ?: data.fUserInfo?.follow}关注"
                AppCardType.RECENT -> "${data.followNum}关注"
            },
            modifier = Modifier
                .padding(start = 10.dp)
                .constrainAs(commentNum) {
                    start.linkTo(logo.end)
                    top.linkTo(appName.bottom)
                },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
            color = MaterialTheme.colorScheme.outline,
        )

        Text(
            text = when (appCardType) {
                AppCardType.APP -> "${data.downCount}下载"
                AppCardType.PRODUCT -> "${data.feedCommentNumTxt}讨论"
                AppCardType.TOPIC -> "${data.commentnumTxt}讨论"
                AppCardType.USER -> "${data.fans}粉丝"
                AppCardType.CONTACTS -> "${data.userInfo?.fans ?: data.fUserInfo?.fans}粉丝"
                AppCardType.RECENT -> when (data.targetType) {
                    "user" -> "${data.fansNum}粉丝"
                    else -> "${data.commentNum}讨论" /*"apk","topic"*/
                }
            },
            modifier = Modifier
                .padding(start = 10.dp)
                .constrainAs(downCount) {
                    start.linkTo(commentNum.end)
                    top.linkTo(appName.bottom)
                },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
            color = MaterialTheme.colorScheme.outline,
        )

        if (appCardType in listOf(AppCardType.USER, AppCardType.CONTACTS)) {
            Text(
                text = if (appCardType == AppCardType.USER)
                    "${fromToday(data.logintime ?: 0)}活跃"
                else
                    "${fromToday(data.userInfo?.logintime ?: data.fUserInfo?.logintime ?: 0)}活跃",
                modifier = Modifier
                    .padding(start = 10.dp)
                    .constrainAs(active) {
                        start.linkTo(downCount.end)
                        top.linkTo(appName.bottom)
                    },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                color = MaterialTheme.colorScheme.outline,
            )

            TextButton(
                onClick = {
                    // TODO
                },
                modifier = Modifier
                    .constrainAs(follow) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                Text(text = if (data.isFollow == 1) "取消关注" else "关注")
            }
        }

    }

}