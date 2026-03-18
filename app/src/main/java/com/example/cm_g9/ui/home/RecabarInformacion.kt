package com.example.cm_g9.ui.home

import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Process
import android.provider.Settings
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cm_g9.R
import com.example.cm_g9.data.AppDatabase
import com.example.cm_g9.data.HomeItem
import com.example.cm_g9.data.HomeItemDB
import com.example.cm_g9.data.HomeItemDao
import kotlinx.coroutines.launch

class RecabarInformacion() : ViewModel(){

    val listaHome: MutableList<HomeItem> = mutableListOf()
    val listaHomeDB : MutableList<HomeItemDB> = mutableListOf()
    fun tienePermiso(context: Context): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }
    fun pedirPermisos(context: Context) {
        if (!tienePermiso(context)) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            context.startActivity(intent)
        }
    }
    fun optenerInfoApp(context: Context) {
        if (!tienePermiso(context)) return

        val uso: UsageStatsManager? = context.getSystemService(UsageStatsManager::class.java)
        val tiempoActual = System.currentTimeMillis()
        val inicio = tiempoActual - (1000 * 60 * 60 * 24) // últimas 24 horas
        val stats: Map<String, UsageStats> =
            uso?.queryAndAggregateUsageStats(inicio, tiempoActual) ?: emptyMap()
        
        var id = 1
        val iconRes: Int = R.drawable.ic_launcher_foreground
        val pm = context.packageManager;

        //val objBDPru = HomeItemDB(name = "pruebaBD");

        //viewModelScope.launch { dao.insertAll(listOf(objBDPru)) }

        val dao = AppDatabase.getDatabase(context).homeItemDao()
        if (stats.isNotEmpty()) {
            for (usp in stats.values) {
                val dire = usp.packageName // Guardar en DB
                if (dire.length > 4) {
                    val tiempoMs = usp.totalTimeInForeground
                    var segundos = tiempoMs / 1000
                    var minutos = segundos / 60
                    val horas = minutos / 60
                    segundos %= 60
                    minutos %= 60

                    val nombre = dire.split(".").last()
                    val icon = pm.getApplicationIcon(dire)
                    val nombreReal = pm.getApplicationLabel(
                        pm.getApplicationInfo(dire,0)
                    ).toString()

                    listaHome.add(
                        HomeItem(id, nombreReal, iconRes, "TDB", horas, minutos, segundos,icon)
                    )
                    listaHomeDB.add(
                        HomeItemDB( name = dire , horaUsadas = horas , minUsadas = minutos , segUsadas = segundos , habilitado = true)
                    )
                    id++
                    //id = id,
                }
            }

            viewModelScope.launch { dao.insertAll(listaHomeDB) }
        }
    }
    fun sacarIcono(dire: String , context: Context): BitmapPainter {
        val drawable = context.packageManager.getApplicationIcon(dire)

        val bitmap: Bitmap = if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            val b = createBitmap(
                drawable.intrinsicWidth.coerceAtLeast(1),
                drawable.intrinsicHeight.coerceAtLeast(1)
            )
            val canvas = Canvas(b)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            b
        }
        return BitmapPainter(bitmap.asImageBitmap())

    }

    fun sacarNomreReal(dire: String , context: Context): String {
        return context.packageManager.getApplicationLabel(
            context.packageManager.getApplicationInfo(dire, 0)
        ).toString();
    }
}
