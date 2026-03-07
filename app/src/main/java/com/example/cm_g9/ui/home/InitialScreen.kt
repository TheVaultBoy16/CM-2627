package com.example.cm_g9.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cm_g9.R

@Composable
fun InitialScreen(
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Aplicación CM-G9",
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 100.dp)
            )
            Image(
                painter = painterResource(R.drawable.monster_baptism),
                contentDescription = ""
            )
            Text(
                text = "Una app para visualizar el tiempo de uso de nuestras aplicaciones",
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(top = 20.dp, bottom = 100.dp)
            )
            Text(
                text = "Haz clic para empezar",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onStartClick() }
            )
        }

        Column(
            modifier = Modifier.align(Alignment.BottomEnd),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(text = "Creado por:")
            Text(text = "    Ricardo Soto Gallo")
            Text(text = "    Gonzalo Aguilera Mancheño")
            Text(text = "    Pablo Rodríguez Armesto")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InitialScreenPreview() {
    InitialScreen(onStartClick = {})
}
