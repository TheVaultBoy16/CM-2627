package com.example.cm_g9.ui.item

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cm_g9.R

data class HomeItemDummy( // Clase para probar la interfaz, quitar para meter los datos reales
    val id: Int,
    val name: String,
    val imageRes: Int,
    val date: String = "22/02/2026",
    val horaUsadas: Long = 2,
    val minUsadas: Long = 3,
    val segUsadas: Long = 45,
)

@Composable
fun ItemScreen(
    itemId: Int,
    modifier: Modifier = Modifier
) {
    val iconRes: Int = R.drawable.ic_launcher_foreground
    val item = HomeItemDummy(itemId, "App $itemId", iconRes)
    
    // Estado para controlar la visibilidad de la gráfica de barras
    var showBarChart by remember { mutableStateOf(false) }

    // Datos de prueba para las gráficas (EJE X -> Días, EJE Y -> Minutos)
    val dummyData = listOf(
        Pair(1, 45), Pair(3, 120), Pair(5, 30), Pair(7, 200),
        Pair(10, 80), Pair(15, 150), Pair(20, 10), Pair(25, 300),
        Pair(28, 60), Pair(30, 180)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = "App Image",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "Tiempo de uso: ${item.horaUsadas}:${item.minUsadas}:${item.segUsadas}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (showBarChart) {
            Text(
                text = "Estadísticas de uso (Barras)",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            UsageBarChart(
                data = dummyData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        } else {

            Text(
                text = "Estadísticas de uso",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            UsageGraph(
                data = dummyData,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { showBarChart = !showBarChart },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (showBarChart) "Ocultar Barras" else "Ver Barras")
            }

            Button(
                onClick = {},
                modifier = Modifier.weight(1f)
            ) {
                Text("Botón 2")
            }
        }
    }
}

@Composable
fun UsageBarChart(data: List<Pair<Int, Int>>, modifier: Modifier = Modifier) {
    val maxDays = 31
    val maxMinutes = 350
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
        drawLine(
            color = Color.Black,
            start = Offset(marginLeft, 20f),
            end = Offset(marginLeft, height - marginBottom),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.Black,
            start = Offset(marginLeft, height - marginBottom),
            end = Offset(width - 20f, height - marginBottom),
            strokeWidth = 2f
        )

        // Etiquetas Eje Y
        val ySteps = listOf(0, 100, 200, 300)
        ySteps.forEach { min ->
            val y = (height - marginBottom) - (min.toFloat() / maxMinutes) * chartHeight
            drawText(
                textMeasurer = textMeasurer,
                text = min.toString(),
                style = textStyle,
                topLeft = Offset(marginLeft - 60f, y - 15f)
            )
            drawLine(
                color = Color.LightGray,
                start = Offset(marginLeft, y),
                end = Offset(width - 20f, y),
                strokeWidth = 1f
            )
        }

        // Etiquetas Eje X
        val xSteps = listOf(1, 10, 20, 30)
        xSteps.forEach { day ->
            val x = marginLeft + (day.toFloat() / maxDays) * chartWidth
            drawText(
                textMeasurer = textMeasurer,
                text = day.toString(),
                style = textStyle,
                topLeft = Offset(x - 10f, height - marginBottom + 15f)
            )
        }

        // Dibujar Barras
        val barWidth = (chartWidth / maxDays) * 0.7f
        data.forEach { (day, minutes) ->
            val x = marginLeft + (day.toFloat() / maxDays) * chartWidth - (barWidth / 2)
            val barHeight = (minutes.toFloat() / maxMinutes) * chartHeight
            val y = (height - marginBottom) - barHeight
            
            drawRect(
                color = Color(0xFF6200EE),
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight)
            )
        }
    }
}

@Composable
fun UsageGraph(data: List<Pair<Int, Int>>, modifier: Modifier = Modifier) {
    val maxDays = 31
    val maxMinutes = 350 
    val textMeasurer = rememberTextMeasurer()
    val textStyle = TextStyle(fontSize = 10.sp, color = Color.Gray)

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val marginLeft = 100f
        val marginBottom = 80f

        drawLine(
            color = Color.Black,
            start = Offset(marginLeft, 20f),
            end = Offset(marginLeft, height - marginBottom),
            strokeWidth = 2f
        )
        drawLine(
            color = Color.Black,
            start = Offset(marginLeft, height - marginBottom),
            end = Offset(width - 20f, height - marginBottom),
            strokeWidth = 2f
        )

        val ySteps = listOf(0, 100, 200, 300)
        ySteps.forEach { min ->
            val y = (height - marginBottom) - (min.toFloat() / maxMinutes) * (height - marginBottom - 20f)
            drawText(
                textMeasurer = textMeasurer,
                text = min.toString(),
                style = textStyle,
                topLeft = Offset(marginLeft - 60f, y - 15f)
            )
        }

        val xSteps = listOf(1, 10, 20, 30)
        xSteps.forEach { day ->
            val x = marginLeft + (day.toFloat() / maxDays) * (width - marginLeft - 20f)
            drawText(
                textMeasurer = textMeasurer,
                text = day.toString(),
                style = textStyle,
                topLeft = Offset(x - 10f, height - marginBottom + 15f)
            )
        }

        data.forEach { (day, minutes) ->
            val x = marginLeft + (day.toFloat() / maxDays) * (width - marginLeft - 20f)
            val y = (height - marginBottom) - (minutes.toFloat() / maxMinutes) * (height - marginBottom - 20f)
            
            drawCircle(
                color = Color.Blue,
                radius = 8f,
                center = Offset(x, y)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemScreenPreview() {
    ItemScreen(itemId = 1)
}
