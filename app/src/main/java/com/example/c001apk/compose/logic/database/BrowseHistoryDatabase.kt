package com.example.c001apk.compose.logic.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.c001apk.compose.logic.dao.HistoryFavoriteDao
import com.example.c001apk.compose.logic.model.FeedEntity

@Database(version = 1, entities = [FeedEntity::class])
abstract class BrowseHistoryDatabase : RoomDatabase() {
    abstract fun browseHistoryDao(): HistoryFavoriteDao
}