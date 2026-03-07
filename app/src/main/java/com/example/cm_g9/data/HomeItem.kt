package com.example.cm_g9.data

import android.graphics.drawable.Drawable

data class HomeItem(
    val id: Int,
    val name: String,
    val imageRes: Int,
    val date: String = "22/02/2026",
    val horaUsadas: Long = 2,
    val minUsadas: Long = 3,
    val segUsadas: Long = 45,
    val icono: Drawable
)
