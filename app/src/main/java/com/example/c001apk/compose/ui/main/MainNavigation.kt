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
import com.example.c001apk.compose.constant.Constants
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.constant.Constants.PREFIX_FEED
import com.example.c001apk.compose.constant.Constants.PREFIX_USER
import com.example.c001apk.compose.ui.component.SlideTransition
import com.example.c001apk.compose.ui.feed.FeedScreen
import com.example.c001apk.compose.ui.others.CopyTextScreen
import com.example.c001apk.compose.ui.search.SearchScreen
import com.example.c001apk.compose.ui.settings.AboutScreen
import com.example.c001apk.compose.ui.settings.LicenseScreen
import com.example.c001apk.compose.ui.settings.ParamsScreen
import com.example.c001apk.compose.ui.user.UserScreen
import com.example.c001apk.compose.util.copyText
import com.example.c001apk.compose.util.makeToast
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

    }

}

fun NavHostController.onOpenLink(context: Context, url: String, needConvert: Boolean = false) {
    val path = if (needConvert) {
        if (url.startsWith(Constants.PREFIX_COOLMARKET))
            url.replaceFirst(Constants.PREFIX_COOLMARKET, "/")
        else
            Uri.parse(url).path ?: EMPTY_STRING
    } else url
    when {
        path.startsWith(PREFIX_USER) -> {
            navigateToUser(path.replaceFirst(PREFIX_USER, ""))
        }

        path.startsWith(PREFIX_FEED) -> {
            navigateToFeed(path.replaceFirst(PREFIX_FEED, ""), null)
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
    navigate("${Router.COPY.name}/${URLEncoder.encode(text?.replace("%", "%25"), "UTF-8")}")
}

fun NavHostController.navigateToFeed(id: String, rid: String?) {
    navigate("${Router.FEED.name}/$id/$rid")
}

fun NavHostController.navigateToUser(uid: String) {
    navigate("${Router.USER.name}/$uid")
}
