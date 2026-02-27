package com.example.cm_g9.ui.home

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.example.cm_g9.R
import com.example.cm_g9.data.HomeItem

class RecabarInformacion(){
    val listaHome: MutableList<HomeItem> = mutableListOf()
    fun pedirPermisos(context: Context) {
        //Montar un bucle while hasta obtener el permiso
        //Si no salir de la aplicacion
        //Ahora no tiene el bucle while solo lo muestra una vez Hay que cambiarlo
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        context.startActivity(intent)
    }
    fun optenerInfoApp(context: Context){
        val uso: UsageStatsManager? = context.getSystemService(UsageStatsManager::class.java)
        val tiempoActual = System.currentTimeMillis()
        val inicio = tiempoActual - (1000 * 60 * 60 * 24) // últimas 24 horas
        val stats: Map<String, UsageStats> =
            uso?.queryAndAggregateUsageStats(inicio, tiempoActual)?.toMap() ?: emptyMap()
        var dire = ""
        var nombre = ""
        var id = 1
        val iconRes: Int = R.drawable.ic_launcher_foreground
        if(stats.isNotEmpty()){
            for(usp in stats.values){
                dire = usp.packageName;
                if(dire.length > 4){
                    val tiempoMs = usp.totalTimeInForeground; // milisegundos
                    var segundos = tiempoMs / 1000
                    var minutos = segundos / 60
                    var horas = minutos / 60
                    segundos %= 60
                    minutos %= 60


                    nombre = dire.split(".").last()
                    listaHome.add(
                        HomeItem(id, nombre, iconRes, "Me la suda" , horas , minutos,segundos)
                    )

                }
            }
        }

    }
}
