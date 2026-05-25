package com.pochipochi.cafe_app.ui.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AppBarWithSearch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.pochipochi.cafe_app.data.model.ProductsModel
import com.pochipochi.cafe_app.ui.menu.components.MenuDrawerComponent
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MenuScreen(viewModel: MenuViewModel = viewModel(), onBack: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.fetchMenu()
    }
    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

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

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    MenuDrawerComponent(
                        drawerState = drawerState,
                        viewModel = viewModel
                    )
                }
            }
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
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
                                IconButton(
                                    onClick = {
                                        scope.launch {
                                            drawerState.open()
                                        }
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier.size(32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ShoppingCart,
                                            contentDescription = "カート"
                                        )
                                        if (state.cartItems.isNotEmpty()) {
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopStart)
                                                    .size(18.dp)
                                                    .background(
                                                        color = Color.Red,
                                                        shape = CircleShape
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = state.cartItems.size.toString(),
                                                    color = Color.White,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        )
                    }
                ) { innerPadding ->
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

                        else ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                LazyColumn(modifier = Modifier.fillMaxSize()) {
                                    items(state.menu.size) { index ->
                                        MenuItem(menus = state.menu[index], viewModel = viewModel)
                                        HorizontalDivider(thickness = 2.dp)
                                    }
                                }
                            }
                    }
                }
            }
        }
    }
}

@Composable
fun MenuItem(menus: ProductsModel, viewModel: MenuViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .padding(20.dp),
    ) {
        AsyncImage(
            model = menus.imageUrl,
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = menus.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Row() {
            Text(
                text = "¥${menus.price}",
                fontSize = 20.sp,
            )
            Spacer(modifier = Modifier.size(14.dp))
            Text(
                text = menus.category,
                modifier = Modifier
                    .background(
                        color = when (menus.category) {
                            "beverage" -> Color(0xFFD7CCC8) // 薄い茶色
                            "food" -> Color(0xFFC8E6C9)     // 薄い緑
                            else -> Color(0xFFFFF9C4)       // 薄い黄色
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        Text(
            text = menus.description,
            fontSize = 16.sp,
        )
        Button(
            onClick = { viewModel.cart(menus.id) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("カートに追加")
        }
    }
}
