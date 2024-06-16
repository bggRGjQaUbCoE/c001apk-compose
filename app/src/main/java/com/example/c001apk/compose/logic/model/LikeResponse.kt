package com.example.c001apk.compose.logic.model

import com.google.gson.annotations.JsonAdapter

data class LikeResponse(
    @JsonAdapter(LikeAdapterFactory::class)
    val data: Data?,
    val status: Int?,
    val error: Int?,
    val message: String?,
    val messageStatus: String?,
) {
    data class Data(
        val count: String?,
        val follow: Int? = 0,
    )
}