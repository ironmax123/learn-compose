package com.pochipochi.cafe_app.ui.router

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pochipochi.cafe_app.ui.menu.MenuScreen
import com.pochipochi.cafe_app.ui.shops.ShopsScreen

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppRouter() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.Shops
    ) {
        composable(Routes.Shops) {
            ShopsScreen(
                onNavigateToMenu = {
                    navController.navigate(Routes.Menu)
                }
            )
        }

        composable(Routes.Menu) {
            MenuScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

object Routes {
    const val Shops = "shops"
    const val Menu = "detail"
}