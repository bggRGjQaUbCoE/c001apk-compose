package com.example.c001apk.compose.logic.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FeedEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val uid: String,
    val uname: String,
    val avatar: String,
    val device: String,
    val message: String,
    val pubDate: String,
    val time: String = System.currentTimeMillis().toString(),
)