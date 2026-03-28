package com.example.cm_g9.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface HomeItemDao {
    //Devolver todos los datos
    @Query("SELECT * FROM home_items ORDER BY name ASC")
    suspend fun getAllItems(): List<HomeItemDB>
    @Query("SELECT * FROM home_items WHERE id = :id LIMIT 1")
    suspend fun getPorId(id: Int): List<HomeItemDB>
    @Query("SELECT * FROM home_items_fechas WHERE name = :nombreId AND date = :fechaString")
    suspend fun getPorFechaApli(nombreId: String , fechaString : String): List<HomeItemFechas>

    @Query("SELECT * FROM home_items_fechas WHERE date = :fechaString")
    suspend fun getApliPorFecha( fechaString : String): List<HomeItemFechas>

    @Query("SELECT * FROM home_items_fechas WHERE name = :name")
    suspend fun getFechaPorApli( name : String): List<HomeItemFechas>

    //Insertar una lista de datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<HomeItemDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllFechas(items : List<HomeItemFechas>)

    //Borrar toda la lista
    @Query("DELETE FROM home_items")
    suspend fun deleteAll()
    @Query("DELETE FROM home_items_fechas")
    suspend fun deleteAllFechas()

    //Borrar por un nombre
    @Query("DELETE FROM home_items WHERE name = :nombreIn")
    suspend fun deleteOne(nombreIn : String)
    //Borramos fecha de una aplicacion
    @Query("DELETE FROM home_items_fechas WHERE name = :nombreIn")
    suspend fun deleteOneFecha(nombreIn: String)

    @Update
    suspend fun updateItem(item: HomeItemDB)
}
