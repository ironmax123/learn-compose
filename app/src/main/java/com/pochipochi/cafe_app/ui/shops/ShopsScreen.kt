package com.pochipochi.cafe_app.ui.shops

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pochipochi.cafe_app.data.model.ShopsModel

@Composable
@ExperimentalMaterial3ExpressiveApi
fun ShopsScreen(viewModel: ShopsViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchShops()
    }

    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularWavyProgressIndicator()
            }
        }

        state.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(state.error.orEmpty())
            }
        }

        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.shops.size) { index ->
                    ShopsItem(state.shops[index])
                }
            }
        }
    }
}

@Composable
fun ShopsItem(shops: ShopsModel) {
    Text(shops.name)
    Text(shops.description)
    Text(shops.phone)
    Text(shops.address)
}
