package com.pochipochi.cafe_app.ui.menu.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pochipochi.cafe_app.ui.menu.MenuViewModel
import kotlinx.coroutines.launch

@Composable
fun MenuDrawerComponent(
    drawerState: DrawerState,
    viewModel: MenuViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.state.collectAsState()
    ModalDrawerSheet {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    scope.launch {
                        drawerState.close()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "閉じる"
                )
            }
            Spacer(modifier = Modifier.size(14.dp))
            Text(text = "カート")
        }
        HorizontalDivider()
        if (state.cartItems.isEmpty()) {
            Text(
                text = "カートにアイテムがありません",
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${state.cartItems.size}個",
                    fontSize = 22.sp,
                )
                Spacer(modifier = Modifier.size(12.dp))
                Text(
                    text = "合計:${state.cartTotalAmount}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {}) {
                    Text("会計に進む")
                }
            }
            HorizontalDivider(thickness = 2.dp)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                items(count = state.cartItems.size) { index ->
                    Text(
                        text = state.cartItems[index].name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "¥${state.cartItems[index].price}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    HorizontalDivider(thickness = 1.dp)
                }
            }

        }
    }
}