package com.example.c001apk.compose.ui.main

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.constant.Constants.PREFIX_COOLMARKET
import com.example.c001apk.compose.constant.Constants.PREFIX_FEED
import com.example.c001apk.compose.constant.Constants.PREFIX_PRODUCT
import com.example.c001apk.compose.constant.Constants.PREFIX_TOPIC
import com.example.c001apk.compose.constant.Constants.PREFIX_USER
import com.example.c001apk.compose.constant.Constants.UTF8
import com.example.c001apk.compose.ui.component.SlideTransition
import com.example.c001apk.compose.ui.feed.FeedScreen
import com.example.c001apk.compose.ui.others.CopyTextScreen
import com.example.c001apk.compose.ui.search.SearchScreen
import com.example.c001apk.compose.ui.settings.AboutScreen
import com.example.c001apk.compose.ui.settings.LicenseScreen
import com.example.c001apk.compose.ui.settings.ParamsScreen
import com.example.c001apk.compose.ui.topic.TopicScreen
import com.example.c001apk.compose.ui.user.UserScreen
import com.example.c001apk.compose.util.copyText
import com.example.c001apk.compose.util.makeToast
import java.net.URLDecoder
import java.net.URLEncoder

/**
 * Created by bggRGjQaUbCoE on 2024/5/30
 */
@Composable
fun MainNavigation(
    navController: NavHostController
) {

    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = Router.MAIN.name,
        enterTransition = {
            SlideTransition.slideLeft.enterTransition()
        },
        exitTransition = {
            SlideTransition.slideLeft.exitTransition()
        },
        popEnterTransition = {
            SlideTransition.slideRight.enterTransition()
        },
        popExitTransition = {
            SlideTransition.slideRight.exitTransition()
        }
    ) {

        composable(route = Router.MAIN.name) {
            MainScreen(
                onParamsClick = {
                    navController.navigate(Router.PARAMS.name)
                },
                onAboutClick = {
                    navController.navigate(Router.ABOUT.name)
                },
                onViewUser = { uid ->
                    navController.navigateToUser(uid)
                },
                onViewFeed = { id, rid ->
                    navController.navigateToFeed(id, rid)
                },
                onSearch = {
                    navController.navigate(Router.SEARCH.name)
                },
                onOpenLink = { url ->
                    navController.onOpenLink(context, url)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                }
            )
        }

        composable(route = Router.PARAMS.name) {
            ParamsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Router.ABOUT.name) {
            AboutScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onLicenseClick = {
                    navController.navigate(Router.LICENSE.name)
                }
            )
        }

        composable(route = Router.LICENSE.name) {
            LicenseScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = "${Router.FEED.name}/{id}/{uid}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                },
                navArgument("rid") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            val id = it.arguments?.getString("id").orEmpty()
            val rid = it.arguments?.getString("rid")
            FeedScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                id = id,
                rid = rid,
                onViewUser = { viewUid ->
                    navController.navigateToUser(viewUid)
                },
                onViewFeed = { viewId, viewRid ->
                    navController.navigateToFeed(viewId, viewRid)
                },
                onOpenLink = { url ->
                    navController.onOpenLink(context, url)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                },
                onViewTopic = {

                }
            )
        }

        // ?id={id},name={name},password={password}
        // route = "${Router.USER.name}/{uid}/{username}",
        composable(
            route = "${Router.USER.name}/{uid}",
            arguments = listOf(
                navArgument("uid") {
                    type = NavType.StringType
                }
            )
        ) {
            val uid = it.arguments?.getString("uid").orEmpty()
            UserScreen(
                uid = uid,
                onBackClick = {
                    navController.popBackStack()
                },
                onViewUser = { viewUid ->
                    navController.navigateToUser(viewUid)
                },
                onViewFeed = { id, rid ->
                    navController.navigateToFeed(id, rid)
                },
                onOpenLink = { url ->
                    navController.onOpenLink(context, url)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                }
            )
        }

        composable(
            route = Router.SEARCH.name,
        ) {
            SearchScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onSearch = {

                }
            )
        }

        composable(
            route = "${Router.COPY.name}/{text}",
            arguments = listOf(
                navArgument("text") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) {
            val text = it.arguments?.getString("text") ?: EMPTY_STRING
            CopyTextScreen(text = text)
        }

        composable(
            route = "${Router.TOPIC}/{tag}/{id}",
            arguments = listOf(
                navArgument("tag") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("id") {
                    type = NavType.StringType
                    nullable = true
                },
            )
        ) {
            val tag = it.arguments?.getString("tag")
            val id = it.arguments?.getString("id")
            TopicScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                tag = tag,
                id = id,
                onViewUser = { viewUid ->
                    navController.navigateToUser(viewUid)
                },
                onViewFeed = { viewId, rid ->
                    navController.navigateToFeed(viewId, rid)
                },
                onOpenLink = { viewUrl ->
                    navController.onOpenLink(context, viewUrl)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                },
            )
        }

    }

}

private val String?.encode
    get() = URLEncoder.encode(this?.replace("%", "%25"), UTF8)

fun NavHostController.onOpenLink(context: Context, url: String, needConvert: Boolean = false) {
    val path = with(URLDecoder.decode(url, UTF8)) {
        if (needConvert) {
            if (this.startsWith(PREFIX_COOLMARKET))
                this.replaceFirst(PREFIX_COOLMARKET, "/")
            else
                Uri.parse(this).path ?: EMPTY_STRING
        } else this
    }
    when {
        path.startsWith(PREFIX_USER) -> {
            navigateToUser(path.replaceFirst(PREFIX_USER, ""))
        }

        path.startsWith(PREFIX_FEED) -> {
            navigateToFeed(path.replaceFirst(PREFIX_FEED, ""), null)
        }

        path.startsWith(PREFIX_TOPIC) -> {
            navigateToTopic(
                id = null,
                tag = path.replaceFirst(PREFIX_TOPIC, "").replace("\\?type=[A-Za-z0-9]+".toRegex(), ""),
            )
        }

        path.startsWith(PREFIX_PRODUCT) -> {
            navigateToTopic(
                id = path.replaceFirst(PREFIX_PRODUCT, ""),
                tag = null,
            )
        }

        else -> {
            if (!needConvert)
                onOpenLink(context, url, true)
            else {
                context.makeToast("unsupported url: $url")
                context.copyText(url, false)
            }
        }
    }
}

fun NavHostController.navigateToCopyText(text: String?) {
    navigate("${Router.COPY.name}/${text.encode}")
}

fun NavHostController.navigateToFeed(id: String, rid: String?) {
    navigate("${Router.FEED.name}/$id/$rid")
}

fun NavHostController.navigateToUser(uid: String) {
    navigate("${Router.USER.name}/$uid")
}

fun NavHostController.navigateToTopic(tag: String?, id: String?) {
    navigate("${Router.TOPIC.name}/$tag/$id")
}
