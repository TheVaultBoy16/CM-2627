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
import com.example.cm_g9.data.HomeItemFechas
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RecabarInformacion() : ViewModel(){

    val listaHome: MutableList<HomeItem> = mutableListOf()
    val listaHomeDB : MutableList<HomeItemDB> = mutableListOf()

    val listaHomeItemFechas : MutableList<HomeItemFechas> = mutableListOf()
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


        /*
        La fecha se obtendria en



         */
        val fechaActual = Calendar.getInstance().time
        val formato = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        val fechaFormateada = formato.format(fechaActual)

        val uso: UsageStatsManager? = context.getSystemService(UsageStatsManager::class.java)
        /*
        Para obtener horas minutos y segundo seria
        val horaActual = LocalTime.now()

        val hora = horaActual.hour        // 0-23
        val minutos = horaActual.minute   // 0-59
        val segundos = horaActual.second  // 0-59
         */

        //Las tiempos estan en miliSegundos
        val tiempoActual = System.currentTimeMillis()
        //Si el inicio se pone a 0 en long sera las 12 de la noche
        val inicio = 0L//tiempoActual - (1000 * 60 * 60 * 24) // últimas 24 horas
        //Esto tenemos que hacer de alguna forma que calcule desde ahora hasta las 12 por ejemplo
        //Es mas sencillo de lo que parece si obtenemos el dateTime
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
                    //Aqui hay que añadir en la base de datos. Fecha hoy Aunque creo que no hace falta si obtenemos el date time
                    listaHomeDB.add(
                        HomeItemDB( name = dire, habilitado = true)
                    )
                    listaHomeItemFechas.add(
                        HomeItemFechas( name = dire , date = fechaFormateada ,  horaUsadas = horas , minUsadas = minutos , segUsadas = segundos)
                    )
                    /*listaHomeItemFechas.add(
                        HomeItemFechas( name = dire , date = "19/03/2026" , horaUsadas = horas , minUsadas = minutos , segUsadas = segundos )
                    )
                    listaHomeItemFechas.add(
                        HomeItemFechas( name = dire , date = "19/04/2026" , horaUsadas = horas , minUsadas = minutos , segUsadas = segundos )
                    )*/
                    id++
                    //id = id,
                }
            }

            viewModelScope.launch { dao.insertAll(listaHomeDB) }
            viewModelScope.launch { dao.insertAllFechas(listaHomeItemFechas) }
        }
    }
}
