package com.pochipochi.cafe_app.ui.shops

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pochipochi.cafe_app.data.model.ShopsModel
import com.pochipochi.cafe_app.ui.shops.components.ShopsSearch

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
            ) {
                ShopsSearch(viewModel)
            LazyColumn( modifier = Modifier.fillMaxSize()) {
                items(state.shops.size) { index ->
                    ShopsItem(state.shops[index])
                    HorizontalDivider(thickness = 2.dp)
                }
            }
            }
        }
    }
}

@Composable
fun ShopsItem(shops: ShopsModel) {
    Column(Modifier
        .padding(20.dp)
    ){
    Text(
        text = shops.name,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    )

    Text(
        text = shops.description,
        fontSize = 16.sp
    )

    Text(
        text = shops.phone,
        fontSize = 14.sp
    )

    Text(
        text = shops.address,
        fontSize = 14.sp
    )
    }
}