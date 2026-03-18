package com.example.cm_g9.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeItemDao {
    @Query("SELECT * FROM home_items ORDER BY name ASC")
    fun getAllItems(): Flow<List<HomeItemDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<HomeItemDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: HomeItemDB)

    @Upsert
    suspend fun upsert(item: HomeItemDB)

    @Query("DELETE FROM home_items")
    suspend fun deleteAll()

    @Query("DELETE FROM home_items WHERE name = :nombreIn")
    suspend fun deleteOne(nombreIn : String)

    @Query("""
        UPDATE home_items 
        SET date = :date, 
            horaUsadas = :horaUsadas, 
            minUsadas = :minUsadas, 
            segUsadas = :segUsadas, 
            habilitado = :habilitado 
        WHERE name = :name
    """)
    suspend fun updateByName(
        name: String,
        date: String,
        horaUsadas: Long,
        minUsadas: Long,
        segUsadas: Long,
        habilitado: Boolean
    )

    @Update
    suspend fun update(item: HomeItemDB)
}
