package com.example.cm_g9

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.cm_g9.data.AppDatabase
import com.example.cm_g9.ui.home.RecabarInformacion
import com.example.cm_g9.ui.navigation.ApplicationNavGraph
import com.example.cm_g9.ui.navigation.HomeDestination
import com.example.cm_g9.ui.navigation.InitialDestination
import com.example.cm_g9.ui.navigation.ItemDestination
import com.example.cm_g9.ui.theme.CMG9Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val info = RecabarInformacion()
        info.pedirPermisos(this);

        info.optenerInfoApp( this );


        setContent {
            CMG9Theme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        val canNavigateBack = navController.previousBackStackEntry != null
                        MyTopAppBar(
                            title = when {
                                currentRoute == InitialDestination.route -> "Inicio"
                                currentRoute == HomeDestination.route -> "Mis Aplicaciones"
                                currentRoute?.startsWith(ItemDestination.route) == true -> "Detalle de App"
                                else -> stringResource(id = R.string.app_name)
                            },
                            canNavigateBack = canNavigateBack,
                            navigateUp = { navController.navigateUp() }
                        )
                    }
                ) { innerPadding ->
                    ApplicationNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior? = null,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.back_button)
                    )
                }
            }
        }
    )
}
