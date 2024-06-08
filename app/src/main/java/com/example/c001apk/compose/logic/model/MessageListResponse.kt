package com.example.c001apk.compose.logic.model

import com.google.gson.annotations.SerializedName

data class MessageListResponse(
    val status: Int?,
    val message: String?,
    val messageStatus: String?,
    val data: List<Data>?
) {
    data class Data(
        val title: String?,
        val id: String?,
        val ukey: String?,
        val uid: String?,
        val username: String?,
        val fromuid: String?,
        val fromusername: String?,
        val islast: Int?,
        val isnew: Int?,
        val message: String?,
        @SerializedName("message_pic")
        val messagePic: String?,
        val dateline: Long?,
        val entityType: String?,
        val entityId: String?,
        val messageUid: String?,
        val messageUsername: String?,
        val userAvatar: String?,
        val fromUserAvatar: String?,
        val messageUserAvatar: String?,
        var unreadNum: Int?,
        @SerializedName("is_top")
        val isTop: Int?,
        val entityTemplate: String?,
    )
}