package com.example.cm_g9.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun InitialScreen(
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier


) {
    //Eso da grandes problemas en el contexto de que recaba informacion antes de pedir los permisos
    var context = LocalContext.current;
    var recabarInformacion = remember {  RecabarInformacion( ) }//Comentar esto para ver el preload
    LaunchedEffect(Unit) {
        recabarInformacion.optenerInfoApp(context)
    }
    val finalizado by recabarInformacion.finalizado.collectAsState()
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                if(finalizado) Color.Black else Color.Blue
            )
            .padding(24.dp)
            .clickable(
                enabled = finalizado,
                onClick = { onStartClick() }
            ),
        contentAlignment = Alignment.TopCenter,
        //modifier = Modifier.clickable { onStartClick() }
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
//            Image(
//                painter = painterResource(R.drawable.monster_baptism),
//                contentDescription = ""
//            )
            Text(
                text = "Una app para visualizar el tiempo de uso de nuestras aplicaciones",
                textAlign = TextAlign.Justify,
                modifier = Modifier.padding(top = 20.dp, bottom = 100.dp)
            )
            Text(
                text = "Haz clic para empezar",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
                //modifier = Modifier.clickable { onStartClick() }
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
