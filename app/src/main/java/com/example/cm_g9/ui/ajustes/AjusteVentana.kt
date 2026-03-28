package com.example.cm_g9.ui.ajustes

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cm_g9.R
import com.example.cm_g9.data.AppDatabase
import com.example.cm_g9.data.HomeItemDB
import kotlinx.coroutines.launch

@Composable
fun AjusteVentana(modifier: Modifier = Modifier){
    val dao = AppDatabase.getDatabase(LocalContext.current).homeItemDao()
    var itemsState by remember { mutableStateOf<List<HomeItemDB>>(emptyList()) }
    LaunchedEffect(Unit) {
        itemsState = dao.getAllItems()
    }
    val iconRes: Int = R.drawable.ic_launcher_foreground
    val scope = rememberCoroutineScope()

    /*var itemsState by remember {
        mutableStateOf(
            listOf(
                HomeItem(1, "App 1", iconRes, habilitado = true),
                HomeItem(2, "App 2", iconRes, habilitado = true),
                HomeItem(3, "App 3", iconRes, habilitado = true),
                HomeItem(4, "App 4", iconRes, habilitado = true),
                HomeItem(5, "App 5", iconRes, habilitado = true),
                HomeItem(6, "App 6", iconRes, habilitado = true),
                HomeItem(7, "App 7", iconRes, habilitado = true),
                HomeItem(8, "App 8", iconRes, habilitado = true),
            )
        )
    }*/
    var textoPruebas = "Pruebas"
    var numero by remember { mutableStateOf("") }
    Column {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = textoPruebas,
                modifier = Modifier.weight(1f)
            )

            /*TextField(
                value = numero,
                onValueChange = { numero = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                label = { Text("Segundos") },
                modifier = Modifier.width(120.dp)
            )*/
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
            items(itemsState) { item ->
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AjustesItemCard(
                        item = item,
                        onCheckedChange = { isChecked ->
                            // Actualizamos el estado de la lista al deslizar el switch
                            //textoPruebas = item.id.toString()
                            println("Item seleccionado con id: ${item.id}")
                            itemsState = itemsState.map {

                                if (it.id == item.id){
                                    it.copy(habilitado = isChecked)
                                } else {
                                    it
                                }
                            }
                            scope.launch {
                                dao.updateItem(item.copy(habilitado = isChecked))
                            }
                        }
                    )
                    HorizontalDivider(color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun AjustesItemCard(
    item: HomeItemDB,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier.fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            /*Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )*/
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text =  sacarNomreReal(item.name , LocalContext.current),//item.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )

            Switch(
                checked = item.habilitado,
                onCheckedChange = onCheckedChange
            )
        }
    }
}
fun sacarNomreReal(dire: String , context: Context): String {
    return context.packageManager.getApplicationLabel(
        context.packageManager.getApplicationInfo(dire, 0)
    ).toString();
}

@Preview(showBackground = true)
@Composable
fun AjustesVentanaPreview(){
    AjusteVentana()
}


