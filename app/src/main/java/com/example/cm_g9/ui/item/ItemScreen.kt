package com.example.cm_g9.ui.item

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
import androidx.compose.material3.R
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cm_g9.data.HomeItem
import com.example.cm_g9.ui.home.HomeScreen

@Composable
fun ItemScreen(
    modifier: Modifier = Modifier
){
    val iconRes: Int = com.example.cm_g9.R.drawable.ic_launcher_foreground
    val item = HomeItem(1,"WhatsApp",iconRes)

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

@Preview(showBackground = true)
@Composable
fun ItemScreenPreview() {
    ItemScreen()
}