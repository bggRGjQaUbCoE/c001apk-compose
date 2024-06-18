package com.example.c001apk.compose.ui.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.constant.Constants.EMPTY_STRING
import com.example.c001apk.compose.logic.model.FeedArticleContentBean
import com.example.c001apk.compose.logic.model.FeedEntity
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.repository.BlackListRepo
import com.example.c001apk.compose.logic.repository.HistoryFavoriteRepo
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.logic.state.FooterState
import com.example.c001apk.compose.logic.state.LoadingState
import com.example.c001apk.compose.ui.base.BaseViewModel
import com.example.c001apk.compose.ui.history.HistoryType
import com.example.c001apk.compose.util.CookieUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/6
 */
@HiltViewModel(assistedFactory = FeedViewModel.ViewModelFactory::class)
class FeedViewModel @AssistedInject constructor(
    @Assisted val id: String,
    @Assisted var isViewReply: Boolean,
    private val networkRepo: NetworkRepo,
    private val blackListRepo: BlackListRepo,
    private val historyFavoriteRepo: HistoryFavoriteRepo,
) : BaseViewModel(networkRepo, blackListRepo) {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(id: String, isViewReply: Boolean): FeedViewModel
    }

    var feedState by mutableStateOf<LoadingState<HomeFeedResponse.Data>>(LoadingState.Loading)
        private set

    var itemSize = 2

    private var discussMode: Int = 1
    var listType: String = "lastupdate_desc"
    var feedType: String = "feed"
    private var blockStatus = 0
    var fromFeedAuthor = 0

    var topId: String? = null
    var meId: String? = null

    var articleList: List<FeedArticleContentBean>? = null

    init {
        fetchFeedData()
    }

    var replyCount by mutableStateOf("0")
    var isFav by mutableStateOf(false)
    var isBlocked by mutableStateOf(false)

    private suspend fun recordHistory(
        username: String,
        avatar: String,
        device: String,
        message: String,
        pubDate: String,
        type: HistoryType = HistoryType.HISTORY
    ) {
        when (type) {
            HistoryType.FAV -> historyFavoriteRepo.insertFavorite(
                FeedEntity(id, feedUid, username, avatar, device, message, pubDate)
            )

            HistoryType.HISTORY -> historyFavoriteRepo.insertHistory(
                FeedEntity(id, feedUid, username, avatar, device, message, pubDate)
            )
        }

    }

    private fun fetchFeedData() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getFeedContent("/v6/feed/detail?id=$id")
                .collect { state ->
                    if (state is LoadingState.Success) {
                        val response = state.response

                        feedUid = response.uid.orEmpty()
                        replyCount = response.replynum.orEmpty()
                        feedTypeName = response.feedTypeName.orEmpty()
                        feedType = response.feedType.orEmpty()

                        if (response.messageRawOutput != "null") {
                            val listType =
                                object : TypeToken<List<FeedArticleContentBean?>?>() {}.type
                            val message: List<FeedArticleContentBean> =
                                Gson().fromJson(response.messageRawOutput, listType)

                            articleList = message.filter {
                                it.type in listOf("text", "image", "shareUrl")
                            }

                            if (!response.messageCover.isNullOrEmpty()) itemSize++
                            if (!response.title.isNullOrEmpty()) itemSize++
                            if (response.targetRow != null || !response.relationRows.isNullOrEmpty())
                                itemSize++
                            itemSize += articleList?.size ?: 0
                        }

                        if (!response.topReplyRows.isNullOrEmpty()) {
                            val reply = response.topReplyRows[0]
                            topId = reply.id
                            val unameTag = when (reply.uid) {
                                feedUid -> " [楼主]"
                                else -> EMPTY_STRING
                            }
                            reply.username = "${reply.username}$unameTag [置顶]"
                            if (!reply.replyRows.isNullOrEmpty()) {
                                reply.replyRows = reply.replyRows?.map {
                                    it.copy(
                                        message = generateMess(
                                            it,
                                            feedUid,
                                            reply.uid
                                        )
                                    )
                                }
                            }
                        }
                        if (!response.replyMeRows.isNullOrEmpty()) {
                            val reply = response.replyMeRows[0]
                            meId = reply.id
                            if (!reply.replyRows.isNullOrEmpty()) {
                                reply.replyRows = reply.replyRows?.map {
                                    it.copy(
                                        message = generateMess(
                                            it,
                                            feedUid,
                                            reply.uid
                                        )
                                    )
                                }
                            }
                        }
                        fetchData()

                        if (CookieUtil.recordHistory && !historyFavoriteRepo.checkHistory(id)) {
                            recordHistory(
                                username = response.username.orEmpty(),
                                avatar = response.userAvatar.orEmpty(),
                                device = response.deviceTitle.orEmpty(),
                                message = with(response.message.orEmpty()) {
                                    if (this.length > 150) this.substring(0, 150) else this
                                },
                                pubDate = response.dateline.toString()
                            )
                        }
                        isFav = historyFavoriteRepo.checkFavorite(id)
                        isBlocked = blackListRepo.checkUid(feedUid)
                    }
                    feedState = state
                    isRefreshing = false
                }
        }
    }

    private lateinit var feedUid: String
    lateinit var uid: String
    var feedTypeName by mutableStateOf(EMPTY_STRING)

    override suspend fun customFetchData() =
        networkRepo.getFeedContentReply(
            id, listType, page, firstItem, lastItem, discussMode,
            feedType, blockStatus, fromFeedAuthor
        )

    override fun handleResponse(response: List<HomeFeedResponse.Data>): List<HomeFeedResponse.Data> {
        response.forEach { item ->
            item.username = "${item.username}${if (item.uid == feedUid) " [楼主]" else EMPTY_STRING}"
            if (!item.replyRows.isNullOrEmpty()) {
                item.replyRows = item.replyRows?.map {
                    it.copy(
                        message = generateMess(
                            it,
                            feedUid,
                            item.uid
                        )
                    )
                }
            }
        }

        return response.distinctBy { it.entityId }
            .filterNot {
                if (listType == "lastupdate_desc")
                    it.id in listOf(topId, meId)
                else false
            }
    }

    var isPull = false
    override fun refresh() {
        if (!isRefreshing && !isLoadMore) {
            if (feedState is LoadingState.Success) {
                page = 1
                isEnd = false
                isLoadMore = false
                isRefreshing = true
                firstItem = null
                lastItem = null
                fetchData()
            } else {
                if (isPull) {
                    isPull = false
                    viewModelScope.launch {
                        isRefreshing = true
                        delay(50)
                        isRefreshing = false
                    }
                }
                feedState = LoadingState.Loading
                fetchFeedData()
            }
        }
    }

    private fun generateMess(
        reply: HomeFeedResponse.Data,
        feedUid: String?,
        uid: String?
    ): String = run {
        val replyTag =
            when (reply.uid) {
                feedUid -> " [楼主] "
                uid -> " [层主] "
                else -> EMPTY_STRING
            }

        val rReplyTag =
            when (reply.ruid) {
                feedUid -> " [楼主] "
                uid -> " [层主] "
                else -> EMPTY_STRING
            }

        val rReplyUser =
            when (reply.ruid) {
                uid -> EMPTY_STRING
                else -> """<a class="feed-link-uname" href="/u/${reply.ruid}">${reply.rusername}${rReplyTag}</a>"""
            }

        val replyPic =
            when (reply.pic) {
                EMPTY_STRING -> EMPTY_STRING
                else -> """ <a class=\"feed-forward-pic\" href=${reply.pic}>查看图片(${reply.picArr?.size})</a>"""
            }

        """<a class="feed-link-uname" href="/u/${reply.uid}">${reply.username}${replyTag}</a>回复${rReplyUser}: ${reply.message}${replyPic}"""

    }

    var frid: String? = null
    lateinit var replyId: String
    private var replyPage = 1
    private var replyLastItem: String? = null
    var isEndReply = false
    private var isLoadMoreReply = false
    var replyLoadingState by mutableStateOf<LoadingState<List<HomeFeedResponse.Data>>>(LoadingState.Loading)
        private set
    var replyFooterState by mutableStateOf<FooterState>(FooterState.Success)
        private set

    fun fetchTotalReply() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getReply2Reply(replyId, replyPage, replyLastItem)
                .collect { result ->
                    when (result) {
                        LoadingState.Empty -> {
                            if (replyLoadingState is LoadingState.Success && !isRefreshing)
                                replyFooterState = FooterState.End
                            else
                                replyLoadingState = result
                            isEndReply = true
                        }

                        is LoadingState.Error -> {
                            if (replyLoadingState is LoadingState.Success)
                                replyFooterState = FooterState.Error(result.errMsg)
                            else
                                replyLoadingState = result
                            isEndReply = true
                        }

                        LoadingState.Loading -> {
                            if (replyLoadingState is LoadingState.Success)
                                replyFooterState = FooterState.Loading
                            else
                                replyLoadingState = result
                        }

                        is LoadingState.Success -> {
                            replyPage++
                            var response = result.response.filter {
                                it.entityType == "feed_reply"
                            }
                            response = response.map { reply ->
                                reply.copy(
                                    username = generateName(reply, uid)
                                )
                            }
                            replyLastItem = response.lastOrNull()?.id
                            replyLoadingState =
                                if (isLoadMoreReply)
                                    LoadingState.Success(
                                        (((replyLoadingState as? LoadingState.Success)?.response
                                            ?: emptyList()) + response).distinctBy { it.entityId }
                                    )
                                else
                                    LoadingState.Success(response)
                            replyFooterState = FooterState.Success
                        }
                    }
                    isLoadMoreReply = false
                }
        }
    }

    private fun generateName(data: HomeFeedResponse.Data, uid: String): String = run {
        val replyTag =
            when (data.uid) {
                feedUid -> " [楼主] "
                uid -> " [层主] "
                else -> EMPTY_STRING
            }

        val rReplyTag =
            when (data.ruid) {
                feedUid -> " [楼主] "
                uid -> " [层主] "
                else -> EMPTY_STRING
            }

        if (data.ruid == "0")
            """<a class="feed-link-uname" href="/u/${data.uid}">${data.username}$replyTag</a>"""
        else
            """<a class="feed-link-uname" href="/u/${data.uid}">${data.username}$replyTag</a>回复<a class="feed-link-uname" href="/u/${data.rusername}">${data.rusername}$rReplyTag</a>"""
    }

    fun resetReplyState() {
        replyPage = 1
        isLoadMoreReply = false
        isEndReply = false
        replyLastItem = null
        frid = null
        replyLoadingState = LoadingState.Loading
        replyFooterState = FooterState.Success
    }

    fun loadMoreReply() {
        if (!isLoadMoreReply) {
            isEndReply = false
            isLoadMoreReply = true
            fetchTotalReply()
            if (replyLoadingState is LoadingState.Success) {
                replyFooterState = FooterState.Loading
            } else {
                replyLoadingState = LoadingState.Loading
            }
        }
    }

    override fun handleLikeResponse(id: String, like: Int, count: String?): Boolean? {
        return if (id in listOf(this.id, topId, meId)) {
            var response = (feedState as LoadingState.Success).response
            val isLike = if (like == 1) 0 else 1
            when (id) {
                this.id -> {
                    response = response.copy(
                        likenum = count,
                        userAction = HomeFeedResponse.UserAction(isLike)
                    )
                }

                topId -> {
                    response.topReplyRows?.getOrNull(0)?.let {
                        response = response.copy(
                            topReplyRows = listOf(
                                it.copy(
                                    likenum = count,
                                    userAction = HomeFeedResponse.UserAction(isLike)
                                )
                            )
                        )
                    }

                }

                meId -> {
                    response.replyMeRows?.getOrNull(0)?.let {
                        response = response.copy(
                            replyMeRows = listOf(
                                it.copy(
                                    likenum = count,
                                    userAction = HomeFeedResponse.UserAction(isLike)
                                )
                            )
                        )
                    }

                }
            }
            feedState = LoadingState.Success(response)
            true
        } else null
    }

    fun blockUser() {
        viewModelScope.launch(Dispatchers.IO) {
            if (isBlocked)
                blackListRepo.deleteUid(feedUid)
            else
                blackListRepo.saveUid(feedUid)
            isBlocked = !isBlocked
        }
    }

    fun onFav() {
        viewModelScope.launch(Dispatchers.IO) {
            if (isFav) {
                historyFavoriteRepo.deleteFavorite(id)
            } else {
                val response = (feedState as LoadingState.Success).response
                recordHistory(
                    username = response.username.orEmpty(),
                    avatar = response.userAvatar.orEmpty(),
                    device = response.deviceTitle.orEmpty(),
                    message = with(response.message.orEmpty()) {
                        if (this.length > 150) this.substring(0, 150) else this
                    },
                    pubDate = response.dateline.toString(),
                    type = HistoryType.FAV
                )
            }
            isFav = !isFav
        }
    }

}