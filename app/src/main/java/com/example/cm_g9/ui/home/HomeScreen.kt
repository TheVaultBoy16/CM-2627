package com.example.cm_g9.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cm_g9.R
import androidx.core.graphics.createBitmap
import com.example.cm_g9.data.AppDatabase
import com.example.cm_g9.data.HomeItemDB
import com.example.cm_g9.data.HomeItemFechas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun HomeScreen(
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onAjustes : () -> Unit,

) {
    val iconRes: Int = R.drawable.ic_launcher_foreground






    //Comentar esto para ver el preload
    //val items = recabarInformacion.listaHome; //Comentar esto para ver el preload
    //Descomentar lo de abajo para ver el preload
   /*var items = listOf(
        HomeItem(1, "App 1", iconRes),
        HomeItem(2, "App 2", iconRes),
        HomeItem(3, "App 3", iconRes),
        HomeItem(4, "App 4", iconRes),
        HomeItem(5, "App 5", iconRes),
        HomeItem(6, "App 6", iconRes),
        HomeItem(7, "App 7", iconRes),
        HomeItem(8, "App 8", iconRes),
    )*/
    //recabarInformacion.listaHome;
    val fechaActual = Calendar.getInstance().time
    val formato = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    val fechaFormateada = formato.format(fechaActual)
    //Aqui se obtiene todos los datos de la base de datos una vez al iniciar la instancia.
    val dao = AppDatabase.getDatabase(LocalContext.current).homeItemDao()
    var items by remember { mutableStateOf<List<HomeItemDB>>(emptyList()) }
    var itemsFecha by remember {  mutableStateOf<List<HomeItemFechas>>(emptyList())  }

    LaunchedEffect(Unit) {
        items = dao.getAllItems()
        itemsFecha = dao.getApliPorFecha(fechaFormateada)

    }

    Column() {
        Button(
            onClick = { onAjustes() }
        ) {
            Text("Enviar")
        }
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
                    HomeItemCard(
                        item = item,
                        modifier = Modifier.clickable { onItemClick(item.id)},
                        itemsFecha = itemsFecha

                    )
                    HorizontalDivider(color = Color.Black)
                }
            }
        }
    }

}

@Composable
fun HomeItemCard(item: HomeItemDB, modifier: Modifier = Modifier , itemsFecha : List<HomeItemFechas>) {
    val itemFecha = itemsFecha.find { it.name == item.name }?: HomeItemFechas( name = "Nada" , date = "0/0/0" ,  horaUsadas = 99 , minUsadas = 99 , segUsadas = 99 )
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = sacarIcono(item.name , LocalContext.current),//drawableToPainter(item.icono ?: LocalContext.current.getDrawable(R.drawable.ic_launcher_foreground)!!),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = sacarNomreReal(item.name , LocalContext.current),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Last time used: " + itemFecha.date,//item.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Tiempo de uso: " + itemFecha.horaUsadas + ":" + itemFecha.minUsadas + ":" + itemFecha.segUsadas, //item.horaUsadas + ":" + item.minUsadas + ":" + item.segUsadas,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onItemClick = {},
        onAjustes = {}
    )
}

fun sacarIcono(dire: String , context: Context): BitmapPainter {
    val drawable = context.packageManager.getApplicationIcon(dire)

    val bitmap: Bitmap = if (drawable is BitmapDrawable) {
        drawable.bitmap
    } else {
        val b = createBitmap(
            drawable.intrinsicWidth.coerceAtLeast(1),
            drawable.intrinsicHeight.coerceAtLeast(1)
        )
        val canvas = Canvas(b)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        b
    }
    return BitmapPainter(bitmap.asImageBitmap())

}

fun sacarNomreReal(dire: String , context: Context): String {
    return context.packageManager.getApplicationLabel(
        context.packageManager.getApplicationInfo(dire, 0)
    ).toString();
}
