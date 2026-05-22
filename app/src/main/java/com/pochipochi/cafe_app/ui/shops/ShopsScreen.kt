package com.pochipochi.cafe_app.ui.shops

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.pochipochi.cafe_app.data.model.ShopsModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ShopsScreen(viewModel: ShopsViewModel = viewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    if(state.isLoading){
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }else{
    LazyColumn {
        items(state.shops.size) { index ->
            ShopsItem(state.shops[index])
        }
    }
    }

}

@Composable
fun ShopsItem(shops: ShopsModel){
    Text(shops.name)
    Text(shops.description)
    Text(shops.phone)
    Text(shops.address)
}
