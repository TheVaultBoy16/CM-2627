package com.example.cm_g9.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeItemDao {
    @Query("SELECT * FROM home_items ORDER BY name ASC")
    fun getAllItems(): Flow<List<HomeItemDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<HomeItemDB>)

    @Query("DELETE FROM home_items")
    suspend fun deleteAll()
}
