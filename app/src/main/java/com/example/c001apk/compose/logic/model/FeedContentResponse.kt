package com.example.c001apk.compose.logic.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FeedContentResponse(
    val status: Int?,
    val error: Int?,
    val message: String?,
    val messageStatus: String?,
    val data: HomeFeedResponse.Data?
) : Parcelable

