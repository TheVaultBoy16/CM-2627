package com.example.cm_g9.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cm_g9.R

@Composable
fun InitialScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Aplicación CM-G9",
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 100.dp).align(Alignment.TopCenter)
        )

        Column(
            modifier = modifier.padding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Image(
                painter = painterResource(R.drawable.monster_baptism),
                contentDescription = "",
            )
            Text(
                text = "Una app para visualizar el tiempo de uso de nuestras aplicaciones, " +
                        "junto con estadísticas para comprender mejor nuestra actividad durante" +
                        " el día",
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(24.dp)
            )
            Text(
                text = "Haz clic para empezar",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier.align(Alignment.BottomEnd),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = "Creado por:"
            )
            Text(
                text = "    Ricardo Soto Gallo"
            )
            Text(
                text = "    Gonzalo Aguilera Mancheño"
            )
            Text(
                text = "    Pablo Rodríguez Armesto"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InitialScreenPreview() {
    InitialScreen()
}
