package com.example.c001apk.compose.ui.main

import android.content.Context
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.constant.Constants.PREFIX_APP
import com.example.c001apk.compose.constant.Constants.PREFIX_CAROUSEL
import com.example.c001apk.compose.constant.Constants.PREFIX_CAROUSEL1
import com.example.c001apk.compose.constant.Constants.PREFIX_COOLMARKET
import com.example.c001apk.compose.constant.Constants.PREFIX_DYH
import com.example.c001apk.compose.constant.Constants.PREFIX_FEED
import com.example.c001apk.compose.constant.Constants.PREFIX_GAME
import com.example.c001apk.compose.constant.Constants.PREFIX_HTTP
import com.example.c001apk.compose.constant.Constants.PREFIX_PRODUCT
import com.example.c001apk.compose.constant.Constants.PREFIX_TOPIC
import com.example.c001apk.compose.constant.Constants.PREFIX_USER
import com.example.c001apk.compose.constant.Constants.PREFIX_USER_LIST
import com.example.c001apk.compose.constant.Constants.URL_LOGIN
import com.example.c001apk.compose.logic.model.UpdateCheckItem
import com.example.c001apk.compose.ui.app.AppScreen
import com.example.c001apk.compose.ui.appupdate.AppUpdateScreen
import com.example.c001apk.compose.ui.blacklist.BlackListScreen
import com.example.c001apk.compose.ui.carousel.CarouselScreen
import com.example.c001apk.compose.ui.component.SlideTransition
import com.example.c001apk.compose.ui.coolpic.CoolPicScreen
import com.example.c001apk.compose.ui.dyh.DyhScreen
import com.example.c001apk.compose.ui.feed.FeedScreen
import com.example.c001apk.compose.ui.ffflist.FFFListScreen
import com.example.c001apk.compose.ui.ffflist.FFFListType
import com.example.c001apk.compose.ui.history.HistoryScreen
import com.example.c001apk.compose.ui.login.LoginScreen
import com.example.c001apk.compose.ui.notification.NoticeScreen
import com.example.c001apk.compose.ui.others.CopyTextScreen
import com.example.c001apk.compose.ui.search.SearchResultScreen
import com.example.c001apk.compose.ui.search.SearchScreen
import com.example.c001apk.compose.ui.settings.AboutScreen
import com.example.c001apk.compose.ui.settings.LicenseScreen
import com.example.c001apk.compose.ui.settings.ParamsScreen
import com.example.c001apk.compose.ui.topic.TopicScreen
import com.example.c001apk.compose.ui.user.UserScreen
import com.example.c001apk.compose.ui.webview.WebViewScreen
import com.example.c001apk.compose.util.CookieUtil
import com.example.c001apk.compose.util.CookieUtil.openInBrowser
import com.example.c001apk.compose.util.copyText
import com.example.c001apk.compose.util.decode
import com.example.c001apk.compose.util.encode
import com.example.c001apk.compose.util.getReportUrl
import com.example.c001apk.compose.util.makeToast
import com.example.c001apk.compose.util.openInBrowser

/**
 * Created by bggRGjQaUbCoE on 2024/5/30
 */
@Composable
fun MainNavigation(
    navController: NavHostController,
    badge: Int,
    resetBadge: () -> Unit,
) {

    val context = LocalContext.current
    var initialPage = 0

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
                badge = badge,
                resetBadge = resetBadge,
                onParamsClick = {
                    navController.navigate(Router.PARAMS.name)
                },
                onAboutClick = {
                    navController.navigate(Router.ABOUT.name)
                },
                onViewUser = { uid ->
                    navController.navigateToUser(uid)
                },
                onViewFeed = { viewId, isViewReply ->
                    navController.navigateToFeed(viewId, isViewReply)
                },
                onSearch = {
                    initialPage = 0
                    navController.navigateToSearch(null, null, null)
                },
                onOpenLink = { viewUrl, viewTitle ->
                    navController.onOpenLink(context, viewUrl, viewTitle)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                },
                onViewApp = { packageName ->
                    navController.navigateToApp(packageName)
                },
                onLogin = {
                    navController.navigate(Router.LOGIN.name)
                },
                onCheckUpdate = { data ->
                    val bundle = Bundle()
                    bundle.putParcelableArrayList("list", ArrayList(data))
                    navController.navigate(route = Router.UPDATE.name, args = bundle)
                },
                onViewFFFList = { viewUid, viewType ->
                    navController.navigateToFFFList(viewUid, viewType, null, null)
                },
                onReport = { viewId, reportType ->
                    navController.navigateToWebView(getReportUrl(viewId, reportType))
                },
                onViewNotice = { type ->
                    navController.navigateToNotice(type)
                },
                onViewBlackList = { type ->
                    navController.navigateToBlackList(type)
                },
                onViewHistory = { type ->
                    navController.navigateToHistory(type)
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
            route = "${Router.FEED.name}/{id}/{isViewReply}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                },
                navArgument("isViewReply") {
                    type = NavType.BoolType
                },
            )
        ) {
            val id = it.arguments?.getString("id").orEmpty()
            val isViewReply = it.arguments?.getBoolean("isViewReply") ?: false
            FeedScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                id = id,
                isViewReply = isViewReply,
                onViewUser = { viewUid ->
                    navController.navigateToUser(viewUid)
                },
                onViewFeed = { viewId, viewIsViewReply ->
                    navController.navigateToFeed(viewId, viewIsViewReply)
                },
                onOpenLink = { viewUrl, viewTitle ->
                    navController.onOpenLink(context, viewUrl, viewTitle)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                },
                onReport = { viewId, reportType ->
                    navController.navigateToWebView(getReportUrl(viewId, reportType))
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
                onViewFeed = { viewId, isViewReply ->
                    navController.navigateToFeed(viewId, isViewReply)
                },
                onOpenLink = { viewUrl, viewTitle ->
                    navController.onOpenLink(context, viewUrl, viewTitle)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                },
                onSearch = { title, pageType, pageParam ->
                    initialPage = 0
                    navController.navigateToSearch(title, pageType, pageParam)
                },
                onViewFFFList = { viewUid, viewType ->
                    navController.navigateToFFFList(viewUid, viewType, null, null)
                },
                onReport = { viewId, reportType ->
                    navController.navigateToWebView(getReportUrl(viewId, reportType))
                }
            )
        }

        composable(
            route = "${Router.SEARCH.name}/{title}/{pageType}/{pageParam}",
            arguments = listOf(
                navArgument("title") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("pageType") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("pageParam") {
                    type = NavType.StringType
                    nullable = true
                },
            )
        ) {
            val title = it.arguments?.getString("title")
            val pageType = it.arguments?.getString("pageType")
            val pageParam = it.arguments?.getString("pageParam")
            SearchScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                title = title,
                onSearch = { keyword ->
                    navController.navigateToSearchResult(keyword, title, pageType, pageParam)
                }
            )
        }

        composable(
            route = "${Router.SEARCHRESULT.name}/{keyword}/{title}/{pageType}/{pageParam}",
            arguments = listOf(
                navArgument("keyword") {
                    type = NavType.StringType
                },
                navArgument("title") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("pageType") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("pageParam") {
                    type = NavType.StringType
                    nullable = true
                },
            )
        ) {
            val keyword = it.arguments?.getString("keyword") ?: EMPTY_STRING
            val title = it.arguments?.getString("title")
            val pageType = it.arguments?.getString("pageType")
            val pageParam = it.arguments?.getString("pageParam")
            SearchResultScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                keyword = keyword,
                title = title,
                pageType = pageType,
                pageParam = pageParam,
                onViewUser = { viewUid ->
                    navController.navigateToUser(viewUid)
                },
                onViewFeed = { viewId, isViewReply ->
                    navController.navigateToFeed(viewId, isViewReply)
                },
                onOpenLink = { viewUrl, viewTitle ->
                    navController.onOpenLink(context, viewUrl, viewTitle)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                },
                initialPage = initialPage,
                updateInitPage = { index ->
                    initialPage = index
                },
                onReport = { viewId, reportType ->
                    navController.navigateToWebView(getReportUrl(viewId, reportType))
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
                onViewFeed = { viewId, isViewReply ->
                    navController.navigateToFeed(viewId, isViewReply)
                },
                onOpenLink = { viewUrl, viewTitle ->
                    navController.onOpenLink(context, viewUrl, viewTitle)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                },
                onSearch = { title, pageType, pageParam ->
                    initialPage = 0
                    navController.navigateToSearch(title, pageType, pageParam)
                },
                onReport = { viewId, reportType ->
                    navController.navigateToWebView(getReportUrl(viewId, reportType))
                }
            )
        }

        composable(
            route = "${Router.WEBVIEW.name}/{url}/{isLogin}",
            arguments = listOf(
                navArgument("url") {
                    type = NavType.StringType
                },
                navArgument("isLogin") {
                    type = NavType.BoolType
                }
            )
        ) {
            val url = it.arguments?.getString("url") ?: EMPTY_STRING
            val isLogin = it.arguments?.getBoolean("isLogin") ?: false
            WebViewScreen(
                onBackClick = {
                    if (navController.currentDestination?.route?.contains(Router.WEBVIEW.name) == true) {
                        navController.popBackStack()
                    }
                },
                url = url,
                isLogin = isLogin,
            )
        }

        composable(
            route = "${Router.APP.name}/{packageName}",
            arguments = listOf(
                navArgument("packageName") {
                },
            )
        ) {
            val packageName = it.arguments?.getString("packageName") ?: EMPTY_STRING
            AppScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                packageName = packageName,
                onViewUser = { viewUid ->
                    navController.navigateToUser(viewUid)
                },
                onViewFeed = { viewId, isViewReply ->
                    navController.navigateToFeed(viewId, isViewReply)
                },
                onOpenLink = { viewUrl, viewTitle ->
                    navController.onOpenLink(context, viewUrl, viewTitle)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                },
                onSearch = { title, pageType, pageParam ->
                    initialPage = 0
                    navController.navigateToSearch(title, pageType, pageParam)
                },
                onReport = { viewId, reportType ->
                    navController.navigateToWebView(getReportUrl(viewId, reportType))
                }
            )
        }

        composable(route = Router.LOGIN.name) {
            LoginScreen(
                onBackClick = {
                    if (navController.currentDestination?.route == Router.LOGIN.name) {
                        navController.popBackStack()
                    }
                },
                onWebLogin = {
                    navController.navigateToWebView(url = URL_LOGIN, isLogin = true)
                }
            )
        }

        composable(
            route = "${Router.CAROUSEL.name}/{url}/{title}",
            arguments = listOf(
                navArgument("url") {
                    type = NavType.StringType
                },
                navArgument("title") {
                    type = NavType.StringType
                },
            )
        ) {
            val url = it.arguments?.getString("url") ?: EMPTY_STRING
            val title = it.arguments?.getString("title") ?: EMPTY_STRING
            CarouselScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                url = url,
                title = title,
                onViewUser = { viewUid ->
                    navController.navigateToUser(viewUid)
                },
                onViewFeed = { viewId, isViewReply ->
                    navController.navigateToFeed(viewId, isViewReply)
                },
                onOpenLink = { viewUrl, viewTitle ->
                    navController.onOpenLink(context, viewUrl, viewTitle)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                },
                onReport = { viewId, reportType ->
                    navController.navigateToWebView(getReportUrl(viewId, reportType))
                }
            )
        }

        composable(
            route = Router.UPDATE.name,
        ) {
            val bundle = it.arguments
            val data = if (SDK_INT >= 33)
                bundle?.getParcelableArrayList("list", UpdateCheckItem::class.java)
            else
                bundle?.getParcelableArrayList("list")
            AppUpdateScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                data = data,
                onViewApp = { packageName ->
                    navController.navigateToApp(packageName)
                },
            )
        }

        composable(
            route = "${Router.FFFLIST.name}/{uid}/{type}/{id}/{title}",
            arguments = listOf(
                navArgument("uid") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("type") {
                    type = NavType.StringType
                },
                navArgument("id") {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument("title") {
                    type = NavType.StringType
                    nullable = true
                },
            )
        ) {
            val uid = it.arguments?.getString("uid")
            val type = it.arguments?.getString("type").orEmpty()
            val id = it.arguments?.getString("id")
            val title = it.arguments?.getString("title")
            FFFListScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                id = id,
                title = title,
                uid = uid,
                type = type,
                onViewUser = { viewUid ->
                    navController.navigateToUser(viewUid)
                },
                onViewFeed = { viewId, isViewReply ->
                    navController.navigateToFeed(viewId, isViewReply)
                },
                onOpenLink = { viewUrl, viewTitle ->
                    navController.onOpenLink(context, viewUrl, viewTitle)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                },
                onReport = { viewId, reportType ->
                    navController.navigateToWebView(getReportUrl(viewId, reportType))
                },
                onViewFFFList = { viewUid, viewType, viewId, viewTitle ->
                    navController.navigateToFFFList(viewUid, viewType, viewId, viewTitle)
                }
            )
        }

        composable(
            route = "${Router.DYH.name}/{id}/{title}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                },
                navArgument("title") {
                    type = NavType.StringType
                },
            )
        ) {
            val id = it.arguments?.getString("id") ?: EMPTY_STRING
            val title = it.arguments?.getString("title") ?: EMPTY_STRING
            DyhScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                id = id,
                title = title,
                onViewUser = { viewUid ->
                    navController.navigateToUser(viewUid)
                },
                onViewFeed = { viewId, isViewReply ->
                    navController.navigateToFeed(viewId, isViewReply)
                },
                onOpenLink = { viewUrl, viewTitle ->
                    navController.onOpenLink(context, viewUrl, viewTitle)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                },
                onReport = { viewId, reportType ->
                    navController.navigateToWebView(getReportUrl(viewId, reportType))
                }
            )
        }

        composable(
            route = "${Router.COOLPIC.name}/{title}",
            arguments = listOf(
                navArgument("title") {
                    type = NavType.StringType
                },
            )
        ) {
            val title = it.arguments?.getString("title") ?: EMPTY_STRING
            CoolPicScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                title = title,
                onViewUser = { viewUid ->
                    navController.navigateToUser(viewUid)
                },
                onViewFeed = { viewId, isViewReply ->
                    navController.navigateToFeed(viewId, isViewReply)
                },
                onOpenLink = { viewUrl, viewTitle ->
                    navController.onOpenLink(context, viewUrl, viewTitle)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                },
                onReport = { viewId, reportType ->
                    navController.navigateToWebView(getReportUrl(viewId, reportType))
                }
            )
        }

        composable(
            route = "${Router.NOTICE.name}/{type}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                },
            )
        ) {
            val type = it.arguments?.getString("type") ?: EMPTY_STRING
            NoticeScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                type = type,
                onViewUser = { viewUid ->
                    navController.navigateToUser(viewUid)
                },
                onViewFeed = { viewId, isViewReply ->
                    navController.navigateToFeed(viewId, isViewReply)
                },
                onOpenLink = { viewUrl, viewTitle ->
                    navController.onOpenLink(context, viewUrl, viewTitle)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                },
                onReport = { viewId, reportType ->
                    navController.navigateToWebView(getReportUrl(viewId, reportType))
                }
            )
        }

        composable(
            route = "${Router.BLACKLIST.name}/{type}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                },
            )
        ) {
            val type = it.arguments?.getString("type") ?: EMPTY_STRING
            BlackListScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                type = type,
                onViewUser = { uid ->
                    navController.navigateToUser(uid)
                },
                onViewTopic = { tag ->
                    navController.navigateToTopic(tag, null)
                }
            )
        }

        composable(
            route = "${Router.HISTORY.name}/{type}",
            arguments = listOf(
                navArgument("type") {
                    type = NavType.StringType
                },
            )
        ) {
            val type = it.arguments?.getString("type") ?: EMPTY_STRING
            HistoryScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                type = type,
                onViewUser = { uid ->
                    navController.navigateToUser(uid)
                },
                onReport = { viewId, reportType ->
                    navController.navigateToWebView(getReportUrl(viewId, reportType))
                },
                onOpenLink = { viewUrl, viewTitle ->
                    navController.onOpenLink(context, viewUrl, viewTitle)
                },
                onViewFeed = { viewId, isViewReply ->
                    navController.navigateToFeed(viewId, isViewReply)
                },
                onCopyText = { text ->
                    navController.navigateToCopyText(text)
                },
            )
        }

    }

}

fun NavHostController.onOpenLink(
    context: Context,
    url: String,
    title: String? = null,
    needConvert: Boolean = false,
) {
    val path = with(url.decode) {
        if (needConvert) {
            if (this.startsWith(PREFIX_COOLMARKET))
                this.replaceFirst(PREFIX_COOLMARKET, "/")
            else {
                val uri = Uri.parse(this)
                if (uri.host?.contains("coolapk") == true)
                    uri.path ?: url
                else url
            }
        } else this
    }
    when {
        path.startsWith(PREFIX_USER) -> {
            navigateToUser(path.replaceFirst(PREFIX_USER, ""))
        }

        path.startsWith(PREFIX_FEED) -> {
            val id = path.replaceFirst(PREFIX_FEED, "").replace("?", "&")
            navigateToFeed(id, id.contains("rid"))
        }

        path.startsWith(PREFIX_TOPIC) -> {
            val tag = path.replaceFirst(PREFIX_TOPIC, "")
                .replace("\\?type=[A-Za-z0-9]+".toRegex(), "")
            if (path.contains("type=8"))
                navigateToCoolPic(tag)
            else
                navigateToTopic(id = null, tag = tag)
        }

        path.startsWith(PREFIX_PRODUCT) -> {
            navigateToTopic(
                id = path.replaceFirst(PREFIX_PRODUCT, ""),
                tag = null,
            )
        }

        path.startsWith(PREFIX_APP) -> {
            navigateToApp(packageName = path.replaceFirst(PREFIX_APP, ""))
        }

        path.startsWith(PREFIX_GAME) -> {
            navigateToApp(packageName = path.replaceFirst(PREFIX_GAME, ""))
        }

        path.startsWith(PREFIX_CAROUSEL) -> {
            navigateToCarousel(path.replaceFirst(PREFIX_CAROUSEL, ""), title ?: EMPTY_STRING)
        }

        path.startsWith(PREFIX_CAROUSEL1) -> {
            navigateToCarousel(path.replaceFirst("#", ""), title ?: EMPTY_STRING)
        }

        path.startsWith(PREFIX_USER_LIST) -> {
            val type = when {
                path.contains("myFollowList") -> FFFListType.USER_FOLLOW.name
                else -> EMPTY_STRING
            }
            navigateToFFFList(CookieUtil.uid, type, null, null)
        }

        path.startsWith(PREFIX_DYH) -> {
            navigateToDyh(path.replaceFirst(PREFIX_DYH, ""), title ?: EMPTY_STRING)
        }

        else -> {
            if (!needConvert)
                onOpenLink(context, url, title, true)
            else {
                if (url.startsWith(PREFIX_HTTP)) {
                    if (openInBrowser)
                        context.openInBrowser(url)
                    else
                        navigateToWebView(url)
                } else {
                    context.makeToast("unsupported url: $url")
                    context.copyText(url, false)
                }
            }
        }
    }
}

fun NavHostController.navigateToCopyText(text: String?) {
    navigate("${Router.COPY.name}/${text.encode}")
}

fun NavHostController.navigateToFeed(id: String, isViewReply: Boolean = false) {
    navigate("${Router.FEED.name}/$id/$isViewReply")
}

fun NavHostController.navigateToUser(uid: String) {
    navigate("${Router.USER.name}/$uid")
}

fun NavHostController.navigateToTopic(tag: String?, id: String?) {
    navigate("${Router.TOPIC.name}/$tag/$id")
}

fun NavHostController.navigateToWebView(url: String, isLogin: Boolean = false) {
    navigate("${Router.WEBVIEW.name}/${url.encode}/$isLogin")
}

fun NavHostController.navigateToSearch(title: String?, pageType: String?, pageParam: String?) {
    navigate("${Router.SEARCH.name}/$title/$pageType/$pageParam")
}

fun NavHostController.navigateToSearchResult(
    keyword: String,
    title: String?,
    pageType: String?,
    pageParam: String?
) {
    navigate("${Router.SEARCHRESULT.name}/${keyword.encode}/$title/$pageType/$pageParam")
}

fun NavHostController.navigateToApp(packageName: String) {
    navigate("${Router.APP.name}/$packageName")
}

fun NavHostController.navigateToCarousel(url: String, title: String) {
    navigate("${Router.CAROUSEL.name}/${url.encode}/$title")
}

fun NavHostController.navigate(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val nodeId = graph.findNode(route = route)?.id
    if (nodeId != null) {
        navigate(nodeId, args, navOptions, navigatorExtras)
    }
}

fun NavHostController.navigateToFFFList(uid: String?, type: String, id: String?, title: String?) {
    navigate("${Router.FFFLIST.name}/$uid/$type/$id/$title")
}

fun NavHostController.navigateToDyh(id: String, title: String) {
    navigate("${Router.DYH.name}/$id/$title")
}

fun NavHostController.navigateToCoolPic(title: String) {
    navigate("${Router.COOLPIC.name}/$title")
}

fun NavHostController.navigateToNotice(type: String) {
    navigate("${Router.NOTICE.name}/$type")
}

fun NavHostController.navigateToBlackList(type: String) {
    navigate("${Router.BLACKLIST.name}/$type")
}

fun NavHostController.navigateToHistory(type: String) {
    navigate("${Router.HISTORY.name}/$type")
}
