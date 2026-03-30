package com.example.cm_g9.ui.item

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.createBitmap
import com.example.cm_g9.R
import com.example.cm_g9.data.AppDatabase
import com.example.cm_g9.data.HomeItemDB
import com.example.cm_g9.data.HomeItemFechas
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class HomeItemDummy( // Clase para probar la interfaz, quitar para meter los datos reales
    val id: Int,
    val name: String,
    val imageRes: Int,
    val date: String = "22/02/2026",
    val horaUsadas: Long = 2,
    val minUsadas: Long = 3,
    val segUsadas: Long = 45,
)

enum class ChartType {
    POINTS, BARS, AREA
}

@Composable
fun ItemScreen(
    itemId: Int,
    modifier: Modifier = Modifier
) {
    val iconRes: Int = R.drawable.ic_launcher_foreground
    val item = HomeItemDummy(itemId, "App $itemId", iconRes)
    //Obtener la fecha actual
    val fechaActual = Calendar.getInstance().time
    val formato = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    val fechaFormateada = formato.format(fechaActual)

    // Estado para controlar qué gráfica mostrar
    var selectedChart by remember { mutableStateOf(ChartType.POINTS) }
    val dao = AppDatabase.getDatabase(LocalContext.current).homeItemDao()
    var items by remember { mutableStateOf<List<HomeItemDB>>(emptyList()) }
    var itemsFecha by remember { mutableStateOf<List<HomeItemFechas>>(emptyList()) }
    var appAMirar = HomeItemDB( name = "nada", habilitado = true)
    var usoActual = HomeItemFechas( name = "Nada" , date = "0/0/0" ,  horaUsadas = 99 , minUsadas = 99 , segUsadas = 99 )
    LaunchedEffect(Unit) {
        items = dao.getPorId(itemId);
        appAMirar = items.firstOrNull() ?: HomeItemDB(name = "nada", habilitado = true)
        itemsFecha = dao.getFechaPorApli(appAMirar.name)
    }
    appAMirar = items.firstOrNull() ?: HomeItemDB(name = "nada", habilitado = true)
    usoActual = itemsFecha.find { it.date == fechaFormateada }?: HomeItemFechas( name = "Nada" , date = "0/0/0" ,  horaUsadas = 99 , minUsadas = 99 , segUsadas = 99 )


    // Datos reales para los últimos 7 días
    val dummyData = mutableListOf<Pair<String, Int>>()
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -6) // Empezamos hace 6 días para completar 7 con hoy
    
    var fechaMaximaCalculada = -1
    
    for (i in 0 until 7) {
        val date = calendar.time
        val dayLabel = SimpleDateFormat("d", Locale.getDefault()).format(date)
        val dateString = SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(date)
        
        val usage = itemsFecha.find { it.date == dateString }
        val minutes = if (usage != null && usage.name != "Nada") {
            (usage.minUsadas + usage.horaUsadas * 60).toInt()
        } else {
            0
        }
        
        dummyData.add(dayLabel to minutes)
        if (minutes > fechaMaximaCalculada) {
            fechaMaximaCalculada = minutes
        }
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    val fechaMaxima = (if (fechaMaximaCalculada > 0) fechaMaximaCalculada * 1.2 else 350.0).toInt()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter =  painterResource(id = item.imageRes),
                contentDescription = "App Image",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = sacarNomreReal( appAMirar.name , LocalContext.current ),
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "Tiempo de uso hoy: ${usoActual.horaUsadas}h ${usoActual.minUsadas}m ${usoActual.segUsadas}s",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = when(selectedChart) {
                ChartType.POINTS -> "Estadísticas: Puntos"
                ChartType.BARS -> "Estadísticas: Barras"
                ChartType.AREA -> "Estadísticas: Tendencia (Área)"
            },
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            when (selectedChart) {
                ChartType.POINTS -> UsageGraph(data = dummyData, maxMinutes = fechaMaxima, modifier = Modifier.fillMaxSize())
                ChartType.BARS -> UsageBarChart(data = dummyData, maxMinutes = fechaMaxima, modifier = Modifier.fillMaxSize())
                ChartType.AREA -> UsageAreaChart(data = dummyData, maxMinutes = fechaMaxima, modifier = Modifier.fillMaxSize())
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { 
                    selectedChart = if (selectedChart == ChartType.BARS) ChartType.POINTS else ChartType.BARS 
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (selectedChart == ChartType.BARS) "Ver Puntos" else "Ver Barras")
            }

            Button(
                onClick = { 
                    selectedChart = if (selectedChart == ChartType.AREA) ChartType.POINTS else ChartType.AREA 
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (selectedChart == ChartType.AREA) "Ver Puntos" else "Tendencia")
            }
        }
    }
}

@Composable
fun UsageBarChart(data: List<Pair<String, Int>>, maxMinutes: Int, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(fontSize = 10.sp, color = Color.Gray)

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val marginLeft = 100f
        val marginBottom = 80f
        val chartWidth = width - marginLeft - 20f
        val chartHeight = height - marginBottom - 20f

        // Ejes
        drawLine(Color.Black, Offset(marginLeft, 20f), Offset(marginLeft, height - marginBottom), 2f)
        drawLine(Color.Black, Offset(marginLeft, height - marginBottom), Offset(width - 20f, height - marginBottom), 2f)

        // Etiquetas Eje Y
        val step = maxMinutes / 3
        listOf(0, step, step * 2, maxMinutes).forEach { min ->
            val y = (height - marginBottom) - (min.toFloat() / maxMinutes) * chartHeight
            drawText(textMeasurer, min.toString(), Offset(marginLeft - 60f, y - 15f), textStyle)
            drawLine(Color.LightGray, Offset(marginLeft, y), Offset(width - 20f, y), 1f)
        }

        // Barras y Etiquetas Eje X
        val barWidth = (chartWidth / data.size) * 0.6f
        data.forEachIndexed { index, (dayLabel, minutes) ->
            val x = marginLeft + (index.toFloat() / (data.size - 1).coerceAtLeast(1)) * chartWidth
            val barHeight = (minutes.toFloat() / maxMinutes) * chartHeight
            val y = (height - marginBottom) - barHeight
            
            drawRect(
                color = Color(0xFF6200EE),
                topLeft = Offset(x - (barWidth / 2), y),
                size = Size(barWidth, barHeight)
            )
            
            drawText(textMeasurer, dayLabel, Offset(x - 10f, height - marginBottom + 15f), textStyle)
        }
    }
}

@Composable
fun UsageAreaChart(data: List<Pair<String, Int>>, maxMinutes: Int, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(fontSize = 10.sp, color = Color.Gray)

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val marginLeft = 100f
        val marginBottom = 80f
        val chartWidth = width - marginLeft - 20f
        val chartHeight = height - marginBottom - 20f

        // Ejes y Etiquetas Y
        drawLine(Color.Black, Offset(marginLeft, 20f), Offset(marginLeft, height - marginBottom), 2f)
        drawLine(Color.Black, Offset(marginLeft, height - marginBottom), Offset(width - 20f, height - marginBottom), 2f)
        val step = maxMinutes / 3
        listOf(0, step, step * 2, maxMinutes).forEach { min ->
            val y = (height - marginBottom) - (min.toFloat() / maxMinutes) * chartHeight
            drawText(textMeasurer, min.toString(), Offset(marginLeft - 60f, y - 15f), textStyle)
        }

        if (data.isNotEmpty()) {
            val path = Path()
            val fillPath = Path()

            data.forEachIndexed { index, (dayLabel, minutes) ->
                val x = marginLeft + (index.toFloat() / (data.size - 1).coerceAtLeast(1)) * chartWidth
                val y = (height - marginBottom) - (minutes.toFloat() / maxMinutes) * chartHeight
                
                if (index == 0) {
                    path.moveTo(x, y)
                    fillPath.moveTo(x, height - marginBottom)
                    fillPath.lineTo(x, y)
                } else {
                    path.lineTo(x, y)
                    fillPath.lineTo(x, y)
                }
                
                if (index == data.size - 1) {
                    fillPath.lineTo(x, height - marginBottom)
                    fillPath.close()
                }
                
                drawText(textMeasurer, dayLabel, Offset(x - 10f, height - marginBottom + 15f), textStyle)
            }

            drawPath(fillPath, Brush.verticalGradient(listOf(Color(0xFF6200EE).copy(alpha = 0.5f), Color.Transparent), 20f, height - marginBottom))
            drawPath(path, Color(0xFF6200EE), style = Stroke(width = 4f, cap = StrokeCap.Round))
        }
    }
}

@Composable
fun UsageGraph(data: List<Pair<String, Int>>, maxMinutes: Int, modifier: Modifier = Modifier) {
    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(fontSize = 10.sp, color = Color.Gray)

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val marginLeft = 100f
        val marginBottom = 80f
        val chartWidth = width - marginLeft - 20f
        val chartHeight = height - marginBottom - 20f

        drawLine(Color.Black, Offset(marginLeft, 20f), Offset(marginLeft, height - marginBottom), 2f)
        drawLine(Color.Black, Offset(marginLeft, height - marginBottom), Offset(width - 20f, height - marginBottom), 2f)

        val step = maxMinutes / 3
        listOf(0, step, step * 2, maxMinutes).forEach { min ->
            val y = (height - marginBottom) - (min.toFloat() / maxMinutes) * chartHeight
            drawText(textMeasurer, min.toString(), Offset(marginLeft - 60f, y - 15f), textStyle)
        }

        data.forEachIndexed { index, (dayLabel, minutes) ->
            val x = marginLeft + (index.toFloat() / (data.size - 1).coerceAtLeast(1)) * chartWidth
            val y = (height - marginBottom) - (minutes.toFloat() / maxMinutes) * chartHeight
            
            drawCircle(Color.Blue, radius = 8f, center = Offset(x, y))
            drawText(textMeasurer, dayLabel, Offset(x - 10f, height - marginBottom + 15f), textStyle)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemScreenPreview() {
    ItemScreen(itemId = 1)
}

fun sacarIcono(dire: String , context: Context): BitmapPainter {
    val drawable = context.packageManager.getApplicationIcon(dire)
    val bitmap: Bitmap = if (drawable is BitmapDrawable) {
        drawable.bitmap
    } else {
        val b = createBitmap(drawable.intrinsicWidth.coerceAtLeast(1), drawable.intrinsicHeight.coerceAtLeast(1))
        val canvas = android.graphics.Canvas(b)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        b
    }
    return BitmapPainter(bitmap.asImageBitmap())
}

fun sacarNomreReal(dire: String , context: Context): String {
    if(dire == "nada") return "nada"
    return try {
        context.packageManager.getApplicationLabel(context.packageManager.getApplicationInfo(dire, 0)).toString()
    } catch (e: Exception) {
        dire
    }
}
