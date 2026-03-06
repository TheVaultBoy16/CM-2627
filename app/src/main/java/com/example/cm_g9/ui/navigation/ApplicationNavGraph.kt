package com.example.cm_g9.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cm_g9.ui.home.HomeScreen
import com.example.cm_g9.ui.home.InitialScreen
import com.example.cm_g9.ui.item.ItemScreen

@Composable
fun ApplicationNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = InitialDestination.route,
        modifier = modifier
    ) {
        composable(route = InitialDestination.route) {
            InitialScreen(
                onStartClick = {
                    navController.navigate(HomeDestination.route)
                }
            )
        }
        composable(route = HomeDestination.route) {
            HomeScreen(
                onItemClick = { itemId ->
                    navController.navigate("${ItemDestination.route}/$itemId")
                }
            )
        }
        composable(
            route = ItemDestination.routeWithArgs,
            arguments = listOf(navArgument(ItemDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getInt(ItemDestination.itemIdArg) ?: 0
            ItemScreen(itemId = itemId)
        }
    }
}
