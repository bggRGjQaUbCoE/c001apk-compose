package com.example.c001apk.compose.ui.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c001apk.compose.logic.model.HomeFeedResponse
import com.example.c001apk.compose.logic.repository.NetworkRepo
import com.example.c001apk.compose.logic.state.FooterState
import com.example.c001apk.compose.logic.state.LoadingState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Created by bggRGjQaUbCoE on 2024/6/6
 */
@HiltViewModel(assistedFactory = FeedViewModel.ViewModelFactory::class)
class FeedViewModel @AssistedInject constructor(
    @Assisted val id: String,
    private val networkRepo: NetworkRepo
) : ViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(id: String): FeedViewModel
    }

    var feedState by mutableStateOf<LoadingState<HomeFeedResponse.Data>>(LoadingState.Loading)
        private set
    var loadingState by mutableStateOf<LoadingState<List<HomeFeedResponse.Data>>>(LoadingState.Loading)
        private set
    var footerState by mutableStateOf<FooterState>(FooterState.Success)
        private set
    var isRefreshing by mutableStateOf(false)
        private set

    var itemSize = 1

    private var page = 1
    private var isLoadMore = false
    var isEnd = false
    private var firstItem: String? = null
    private var lastItem: String? = null
    private var discussMode: Int = 1
    var listType: String = "lastupdate_desc"
    var feedType: String = "feed"
    private var blockStatus = 0
    var fromFeedAuthor = 0

    private var topId: String? = null
    private var meId: String? = null

    init {
        fetchFeedData()
    }

    private fun fetchFeedData() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getFeedContent("/v6/feed/detail?id=$id")
                .collect { state ->
                    feedState = state
                    if (state is LoadingState.Success) {
                        val response = state.response
                        feedUid = response.uid.orEmpty()
                        if (!response.topReplyRows.isNullOrEmpty()) {
                            val reply = response.topReplyRows[0]
                            topId = reply.id
                            val unameTag = when (reply.uid) {
                                feedUid -> " [楼主]"
                                else -> ""
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
                        fetchFeedReply()
                    }
                    isRefreshing = false
                }
        }
    }

    private lateinit var feedUid: String
    lateinit var uid: String
    var feedTypeName by mutableStateOf("")

    private fun fetchFeedReply() {
        viewModelScope.launch(Dispatchers.IO) {
            networkRepo.getFeedContentReply(
                id, listType, page, firstItem, lastItem, discussMode,
                feedType, blockStatus, fromFeedAuthor
            )
                .collect { result ->
                    when (result) {
                        LoadingState.Empty -> {
                            if (loadingState is LoadingState.Success && !isRefreshing)
                                footerState = FooterState.End
                            else {
                                loadingState = result
                                footerState = FooterState.Success
                            }
                            isEnd = true
                        }

                        is LoadingState.Error -> {
                            if (loadingState is LoadingState.Success)
                                footerState = FooterState.Error(result.errMsg)
                            else
                                loadingState = result
                            isEnd = true
                        }

                        LoadingState.Loading -> {
                            if (loadingState is LoadingState.Success)
                                footerState = FooterState.Loading
                            else
                                loadingState = result
                        }

                        is LoadingState.Success -> {
                            page++
                            val response = result.response.filter {
                                it.entityType == "feed_reply"
                            }
                            response.forEach { item ->
                                val unameTag = when (item.uid) {
                                    feedUid -> " [楼主]"
                                    else -> ""
                                }
                                item.username = "${item.username}$unameTag"

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
                            lastItem = response.lastOrNull()?.id
                            loadingState =
                                if (isLoadMore)
                                    LoadingState.Success(
                                        (((loadingState as? LoadingState.Success)?.response
                                            ?: emptyList()) + response).distinctBy { it.entityId }
                                            .filterNot {
                                                if (listType == "lastupdate_desc")
                                                    it.id in listOf(topId, meId)
                                                else false
                                            }
                                    )
                                else
                                    LoadingState.Success(
                                        response.filterNot {
                                            if (listType == "lastupdate_desc")
                                                it.id in listOf(topId, meId)
                                            else false
                                        }
                                    )
                            footerState = FooterState.Success
                        }
                    }
                    isLoadMore = false
                    isRefreshing = false
                }
        }
    }

    fun refresh() {
        if (!isRefreshing && !isLoadMore) {
            if (feedState is LoadingState.Success) {
                page = 1
                isEnd = false
                isLoadMore = false
                isRefreshing = true
                firstItem = null
                lastItem = null
                fetchFeedReply()
            } else {
                isRefreshing = false
                feedState = LoadingState.Loading
                fetchFeedData()
            }
        }
    }

    fun loadMore() {
        if (!isRefreshing && !isLoadMore) {
            isEnd = false
            isLoadMore = true
            firstItem = null
            lastItem = null
            fetchFeedReply()
            if (loadingState is LoadingState.Success) {
                footerState = FooterState.Loading
            } else {
                loadingState = LoadingState.Loading
            }
        }
    }

    private fun generateMess(
        reply: HomeFeedResponse.ReplyRows,
        feedUid: String?,
        uid: String?
    ): String = run {
        val replyTag =
            when (reply.uid) {
                feedUid -> " [楼主] "
                uid -> " [层主] "
                else -> ""
            }

        val rReplyTag =
            when (reply.ruid) {
                feedUid -> " [楼主] "
                uid -> " [层主] "
                else -> ""
            }

        val rReplyUser =
            when (reply.ruid) {
                uid -> ""
                else -> """<a class="feed-link-uname" href="/u/${reply.ruid}">${reply.rusername}${rReplyTag}</a>"""
            }

        val replyPic =
            when (reply.pic) {
                "" -> ""
                else -> """ <a class=\"feed-forward-pic\" href=${reply.pic}>查看图片(${reply.picArr?.size})</a>"""
            }

        """<a class="feed-link-uname" href="/u/${reply.uid}">${reply.username}${replyTag}</a>回复${rReplyUser}: ${reply.message}${replyPic}"""

    }

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
                else -> ""
            }

        val rReplyTag =
            when (data.ruid) {
                feedUid -> " [楼主] "
                uid -> " [层主] "
                else -> ""
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

}