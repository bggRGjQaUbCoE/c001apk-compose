package com.example.c001apk.compose.logic.model

import java.util.UUID

data class FeedArticleContentBean(
    val key: String = UUID.randomUUID().toString(),
    val type: String?,
    val message: String?,
    val url: String?,
    val description: String?,
    val title: String?,
    val subTitle: String?,
    val logo: String?,
)