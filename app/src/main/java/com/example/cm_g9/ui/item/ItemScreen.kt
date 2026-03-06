package com.example.cm_g9.ui.item

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cm_g9.R
import com.example.cm_g9.data.HomeItem


data class HomeItemDummy( // De prueba, sin iconos, para probar la interfaz
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
    modifier: Modifier = Modifier
){

    val iconRes: Int = com.example.cm_g9.R.drawable.ic_launcher_foreground
    val item = HomeItemDummy(1,"WhatsApp",iconRes)

    // Datos inventados: (Día del mes, Minutos de uso)
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
                modifier = Modifier
                    .size(100.dp)
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
                    text = "Tiempo de uso: "+item.horaUsadas+":"+item.minUsadas+":"+item.segUsadas,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Estadísticas de uso",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Gráfica de puntos con unidades
        UsageGraph(
            data = dummyData,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {},
                modifier = Modifier.weight(1f)
            ) {
                Text("TBD")
            }

            Button(
                onClick = {},
                modifier = Modifier.weight(1f)
            ) {
                Text("TBD")
            }
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
        val marginLeft = 100f // Espacio para los números del eje Y
        val marginBottom = 80f // Espacio para los números del eje X

        // Eje Y
        drawLine(
            color = Color.Black,
            start = Offset(marginLeft, 20f),
            end = Offset(marginLeft, height - marginBottom),
            strokeWidth = 2f
        )
        // Eje X
        drawLine(
            color = Color.Black,
            start = Offset(marginLeft, height - marginBottom),
            end = Offset(width - 20f, height - marginBottom),
            strokeWidth = 2f
        )

        // Marcas y etiquetas del Eje Y (Minutos)
        val ySteps = listOf(0, 100, 200, 300)
        ySteps.forEach { min ->
            val y = (height - marginBottom) - (min.toFloat() / maxMinutes) * (height - marginBottom - 20f)
            drawText(
                textMeasurer = textMeasurer,
                text = min.toString(),
                style = textStyle,
                topLeft = Offset(marginLeft - 60f, y - 15f)
            )
            drawLine(
                color = Color.Black,
                start = Offset(marginLeft - 10f, y),
                end = Offset(marginLeft, y),
                strokeWidth = 1f
            )
        }
        
        drawText(
            textMeasurer = textMeasurer,
            text = "Min",
            style = textStyle.copy(color = Color.Black, fontSize = 12.sp),
            topLeft = Offset(marginLeft - 80f, 0f)
        )

        // Marcas y etiquetas del Eje X (Días)
        val xSteps = listOf(1, 10, 20, 30)
        xSteps.forEach { day ->
            val x = marginLeft + (day.toFloat() / maxDays) * (width - marginLeft - 20f)
            drawText(
                textMeasurer = textMeasurer,
                text = day.toString(),
                style = textStyle,
                topLeft = Offset(x - 10f, height - marginBottom + 15f)
            )
            drawLine(
                color = Color.Black,
                start = Offset(x, height - marginBottom),
                end = Offset(x, height - marginBottom + 10f),
                strokeWidth = 1f
            )
        }

        drawText(
            textMeasurer = textMeasurer,
            text = "Día",
            style = textStyle.copy(color = Color.Black, fontSize = 12.sp),
            topLeft = Offset(width - 50f, height - marginBottom + 40f)
        )

        // Puntos
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
    ItemScreen()
}
