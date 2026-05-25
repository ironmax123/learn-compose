package com.pochipochi.cafe_app.ui.menu

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MenuScreen(onBack: () -> Unit) {
    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
    val scope = rememberCoroutineScope()

    val inputField: @Composable () -> Unit = {
        SearchBarDefaults.InputField(
            searchBarState = searchBarState,
            textFieldState = textFieldState,
            onSearch = {
                scope.launch {
                    searchBarState.animateToCollapsed()
                }
            },
            placeholder = {
                Text("メニューを検索")
            }
        )
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AppBarWithSearch(
                scrollBehavior = scrollBehavior,
                state = searchBarState,
                inputField = inputField,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "戻る"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: カート画面へ遷移 */ }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "カート"
                        )
                    }
                }
            )

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(10) {
                    MenuItem()
                    HorizontalDivider(thickness = 2.dp)
                }
            }
        }
    }
}

@Composable
fun MenuItem() {
    Column(
        Modifier
            .padding(20.dp)
    ) {
        AsyncImage(
            model = "https://img.smhn.info/wp-content/uploads/2023/09/230909_135817.jpg",
            contentDescription = null,
        )
        Text(
            text = "メニュー名",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "¥⚪︎︎︎︎⚪︎︎︎︎⚪︎︎︎︎⚪︎",
            fontSize = 20.sp,
        )
        Text(
            text = "詳細テキスト",
            fontSize = 16.sp,
        )
        Button(
            onClick = {}
        ) {
            Text("カートに追加")
        }
    }
}

