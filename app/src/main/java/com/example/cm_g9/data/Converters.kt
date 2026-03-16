package com.example.cm_g9.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


// Conjunto de funciones útiles para la conversión de datos

class Converters {
    @TypeConverter
    fun fromBitmap(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    // Since Drawable is not directly serializable and often depends on Context,
    // we convert it to/from Bitmap which we then convert to/from ByteArray.
    // Note: This approach might lose some Drawable properties (like tints, states, etc.)
    // but works for basic icons.
    
    @TypeConverter
    fun fromDrawable(drawable: Drawable): ByteArray {
        val bitmap = if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            val width = if (drawable.intrinsicWidth > 0) drawable.intrinsicWidth else 1
            val height = if (drawable.intrinsicHeight > 0) drawable.intrinsicHeight else 1
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
        return fromBitmap(bitmap)
    }

    @TypeConverter
    fun toDrawable(byteArray: ByteArray): Drawable {
        val bitmap = toBitmap(byteArray)
        return BitmapDrawable(null, bitmap)
    }
}
