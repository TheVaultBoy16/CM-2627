package com.example.cm_g9.ui.ajustes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cm_g9.R
import com.example.cm_g9.data.HomeItem


@Composable
fun AjusteVentana(modifier: Modifier = Modifier){
    val iconRes: Int = R.drawable.ic_launcher_foreground
    val items = listOf(
        HomeItem(1, "App 1", iconRes),
        HomeItem(2, "App 2", iconRes),
        HomeItem(3, "App 3", iconRes),
        HomeItem(4, "App 4", iconRes),
        HomeItem(5, "App 5", iconRes),
        HomeItem(6, "App 6", iconRes),
        HomeItem(7, "App 7", iconRes),
        HomeItem(8, "App 8", iconRes),
        )
    var numero by remember { mutableStateOf("") }
    Column() {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Text(
                text = "Tiempo de actualizacion",
            )

            TextField(
                value = numero,
                onValueChange = { numero = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                label = { Text("Número") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                HorizontalDivider(color = Color.Black)
            }
            items(items) { item ->
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AjustesItemCard(item = item)
                    HorizontalDivider(color = Color.Black)
                }
            }
        }
    }


}

@Composable
fun AjustesItemCard(item: HomeItem, modifier: Modifier = Modifier){

    Card(
        modifier = modifier.fillMaxWidth()
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer( modifier = Modifier.width( 8.dp ) )
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd

            ){
                Switch(
                    checked = true,
                    onCheckedChange = { false }
                )
            }

        }
    }


}

@Preview(showBackground = true)
@Composable
fun AjustesVentanaPreview(){
    AjusteVentana()
}
