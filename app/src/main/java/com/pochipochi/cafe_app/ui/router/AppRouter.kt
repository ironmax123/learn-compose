package com.pochipochi.cafe_app.ui.router

import android.net.Uri
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pochipochi.cafe_app.data.model.ProductsModel
import com.pochipochi.cafe_app.ui.check.CheckScreen
import com.pochipochi.cafe_app.ui.menu.MenuScreen
import com.pochipochi.cafe_app.ui.shops.ShopsScreen
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

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
                onNavigateToMenu = { shopName ->
                    navController.navigate(Routes.menu(shopName))
                }
            )
        }

        composable(Routes.Menu) { backStackEntry ->
            val shopName = Uri.decode(
                backStackEntry.arguments?.getString(Routes.ShopNameArgument).orEmpty()
            )
            MenuScreen(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateCheck = { cartItems, amount ->
                    navController.navigate(Routes.check(shopName, cartItems, amount))
                },
                shopName = shopName
            )
        }
        composable(Routes.Check) { backStackEntry ->
            val shopName = Uri.decode(
                backStackEntry.arguments?.getString(Routes.ShopNameArgument).orEmpty()
            )
            val cartItems = backStackEntry.arguments
                ?.getString(Routes.CartItemsArgument)
                ?.let(Uri::decode)
                ?.let { Json.decodeFromString<List<ProductsModel>>(it) }
                .orEmpty()
            val amount = backStackEntry.arguments
                ?.getString(Routes.AmountArgument)
                ?.toIntOrNull()
                ?: 0
            CheckScreen(
                onBack = {
                    navController.popBackStack()
                },
                onNavigateToShops = {
                    navController.navigate(Routes.Shops) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                cartItem = cartItems,
                amount = amount,
                shopName = shopName
            )
        }
    }
}

object Routes {
    const val Shops = "shops"
    const val ShopNameArgument = "shopName"
    const val CartItemsArgument = "cartItems"
    const val AmountArgument = "amount"
    const val Menu = "menu/{$ShopNameArgument}"
    const val Check = "check/{$ShopNameArgument}/{$AmountArgument}/{$CartItemsArgument}"

    fun menu(shopName: String): String = "menu/${Uri.encode(shopName)}"

    fun check(shopName: String, cartItems: List<ProductsModel>, amount: Int): String {
        val encodedShopName = Uri.encode(shopName)
        val encodedCartItems = Uri.encode(Json.encodeToString(cartItems))
        return "check/$encodedShopName/$amount/$encodedCartItems"
    }
}
