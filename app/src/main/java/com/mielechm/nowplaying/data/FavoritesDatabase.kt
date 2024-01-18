package com.mielechm.nowplaying.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mielechm.nowplaying.data.entities.Favorite

@Database(
    entities = [
        Favorite::class
    ],
    version = 1
)
abstract class FavoritesDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao

    companion object {
        val DATABASE_NAME = "favorites_db"
    }

}