package com.pochipochi.cafe_app.ui.menu

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pochipochi.cafe_app.data.model.ProductsModel
import com.pochipochi.cafe_app.data.repository.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MenuState(
    val menu: List<ProductsModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val cartItems: List<ProductsModel> = emptyList(),
    val cartTotalAmount: Int = 0
)


class MenuViewModel : ViewModel() {
    private val productsRepository = ProductsRepository()
    private val _state = MutableStateFlow(MenuState())
    private val _cartState = mutableStateListOf<ProductsModel>()
    val state: StateFlow<MenuState> = _state.asStateFlow()

    fun fetchMenu() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val menu = productsRepository.fetch()
                _state.value = _state.value.copy(menu = menu, isLoading = false)
            } catch (e: Exception) {
                Log.d("ShopsViewModel", "メニュー情報取得エラー: ${e.message}")
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "エラーが発生しました。\nしばらくしてからやり直してください。"
                )
            }
        }
    }

    fun cart(id: String) {
        viewModelScope.launch {
            try {
                val menuList = productsRepository.fetch()
                val product = menuList.firstOrNull { it.id == id } ?: return@launch
                _cartState.add(product)
                _state.value = _state.value.copy(
                    cartItems = _cartState.toList(),
                    cartTotalAmount = _cartState.sumOf { it.price }
                )
            } catch (e: Exception) {
                Log.d("ShopsViewModel", "カートエラー: ${e.message}")
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "エラーが発生しました。\nしばらくしてからやり直してください。"
                )
            }
        }
    }

    fun searchQuery(query: String) {
        viewModelScope.launch {
            try {
                val result = productsRepository.searchQuery(query)
                _state.value = _state.value.copy(menu = result)
            } catch (e: Exception) {
                Log.d("ShopsViewModel", "カートエラー: ${e.message}")
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "エラーが発生しました。\nしばらくしてからやり直してください。"
                )
            }
        }
    }

    fun showToast(context: android.content.Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}