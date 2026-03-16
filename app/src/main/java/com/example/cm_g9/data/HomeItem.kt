package com.example.cm_g9.data

import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toDrawable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

data class HomeItem(
    val id: Int,
    val name: String,
    val imageRes: Int,
    val date: String = "22/02/2026",
    val horaUsadas: Long = 2,
    val minUsadas: Long = 3,
    val segUsadas: Long = 45,
    val icono: Drawable = 0xFF0000FF.toInt().toDrawable(),
    val habilitado: Boolean = true
)

@Entity(tableName = "home_items")
data class HomeItemDB @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val date: String = "22/02/2026",
    val horaUsadas: Long = 0,
    val minUsadas: Long = 0,
    val segUsadas: Long = 0,
    val habilitado: Boolean = true,
)
