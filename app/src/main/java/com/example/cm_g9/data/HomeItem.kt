package com.example.cm_g9.data

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home_items")
data class HomeItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // Para la BD
    val name: String, // Para la BD (package name)
    val imageRes: Int,
    val date: String = "22/02/2026", // Para la BD
    val horaUsadas: Long = 2, // Para BD
    val minUsadas: Long = 3, // Para BD
    val segUsadas: Long = 45, // Para BD
    val icono: Drawable = 0xFF0000FF.toInt().toDrawable(),
    val habilitado: Boolean = true // Para BD
)
