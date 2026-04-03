package com.example.cm_g9.ui.home

import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Process
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cm_g9.R
import com.example.cm_g9.data.AppDatabase
import com.example.cm_g9.data.HomeItem
import com.example.cm_g9.data.HomeItemDB
import com.example.cm_g9.data.HomeItemFechas
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RecabarInformacion() : ViewModel(){

    private val _finalizado = MutableStateFlow(false)
    val finalizado: StateFlow<Boolean> = _finalizado

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

        //_finalizado.value = false;


        /*
        La fecha se obtendria en



         */


        val uso: UsageStatsManager? = context.getSystemService(UsageStatsManager::class.java)
        /*
        Para obtener horas minutos y segundo seria
        val horaActual = LocalTime.now()

        val hora = horaActual.hour        // 0-23
        val minutos = horaActual.minute   // 0-59
        val segundos = horaActual.second  // 0-59
         */

        //Las tiempos estan en miliSegundos
        val ahoraMili = System.currentTimeMillis()
        var tiempoActual = ahoraMili;
        //Si el inicio se pone a 0 en long sera las 12 de la noche
        var inicio = 0L//tiempoActual - (1000 * 60 * 60 * 24) // últimas 24 horas
        //Esto tenemos que hacer de alguna forma que calcule desde ahora hasta las 12 por ejemplo
        //Es mas sencillo de lo que parece si obtenemos el dateTime
        val franjas = arrayOf(
            0L ,
            ahoraMili ,
            ahoraMili - (1000 * 60 * 60 * 24) ,
            ahoraMili - 2*(1000 * 60 * 60 * 24) ,
            ahoraMili - 3*(1000 * 60 * 60 * 24) ,
            ahoraMili - 4*(1000 * 60 * 60 * 24) ,
            ahoraMili - 5*(1000 * 60 * 60 * 24) ,
            ahoraMili - 6*(1000 * 60 * 60 * 24) ,
            ahoraMili - 7*(1000 * 60 * 60 * 24) )




        val iconRes: Int = R.drawable.ic_launcher_foreground
        val pm = context.packageManager;

        //val objBDPru = HomeItemDB(name = "pruebaBD");

        //viewModelScope.launch { dao.insertAll(listOf(objBDPru)) }

        val dao = AppDatabase.getDatabase(context).homeItemDao()
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR , +2)
        for(i in 0..7){
            inicio = franjas.get(i+1);
            tiempoActual = franjas.get(i);

            //Con esto tendria las fechas
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            val fechaActual = calendar.time
            val formato = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            val fechaFormateada = formato.format(fechaActual)

            val stats: Map<String, UsageStats> =
                uso?.queryAndAggregateUsageStats(inicio, tiempoActual) ?: emptyMap()
            if (stats.isNotEmpty()) {
                for (usp in stats.values) {
                    val dire = usp.packageName // Guardar en DB
                    if (dire.length > 4 ) {
                        //&& dire.contains("whatsapp")
                        val tiempoMs = usp.totalTimeInForeground
                        var segundos = tiempoMs / 1000
                        var minutos = segundos / 60
                        val horas = minutos / 60
                        segundos %= 60
                        minutos %= 60
                        if(i == 1){
                            if( dire.contains("whatsapp") ||
                                dire.contains("youtube") ||
                                dire.contains("telegram") ||
                                dire.contains("netflix") ||
                                dire.contains("discord")){
                                listaHomeDB.add(
                                    HomeItemDB( name = dire, habilitado = true)
                                )
                            }else{
                                listaHomeDB.add(
                                    HomeItemDB( name = dire, habilitado = false)
                                )
                            }


                        }
                        listaHomeItemFechas.add(
                            HomeItemFechas( name = dire , date = fechaFormateada ,  horaUsadas = horas , minUsadas = minutos , segUsadas = segundos)
                        )

                    }

                }

            }
        }
        viewModelScope.launch {
            dao.insertAll(listaHomeDB)
            dao.insertAllFechas(listaHomeItemFechas)
            _finalizado.value = true
            }
        }
}
