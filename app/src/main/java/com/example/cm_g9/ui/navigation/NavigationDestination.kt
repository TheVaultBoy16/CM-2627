package com.example.cm_g9.ui.navigation

import com.example.cm_g9.R

interface NavigationDestination {
    val route: String
    val titleRes: Int
}

object InitialDestination : NavigationDestination {
    override val route = "initial"
    override val titleRes = R.string.app_name
}

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

object ItemDestination : NavigationDestination {
    override val route = "item"
    override val titleRes = R.string.app_name
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}
object AjusteDestination : NavigationDestination{
    override val route = "ajustes"
    override val titleRes = R.string.app_name

}
