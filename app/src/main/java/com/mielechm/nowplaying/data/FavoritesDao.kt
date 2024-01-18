package com.mielechm.nowplaying.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mielechm.nowplaying.data.entities.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToFavorites(favorite: Favorite)

    @Delete
    suspend fun deleteFromFavorites(favorite: Favorite)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<Favorite>>

}