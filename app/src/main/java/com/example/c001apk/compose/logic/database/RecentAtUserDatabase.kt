package com.example.c001apk.compose.logic.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.c001apk.compose.logic.dao.RecentAtUserDao
import com.example.c001apk.compose.logic.model.RecentAtUser

@Database(version = 1, entities = [RecentAtUser::class])
abstract class RecentAtUserDatabase : RoomDatabase() {
    abstract fun recentAtUserDao(): RecentAtUserDao
}