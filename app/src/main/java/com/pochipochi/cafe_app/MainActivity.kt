package com.pochipochi.cafe_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pochipochi.cafe_app.ui.router.AppRouter
import com.pochipochi.cafe_app.ui.theme.CafeappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CafeappTheme {
                AppRouter()
            }
        }
    }
}
