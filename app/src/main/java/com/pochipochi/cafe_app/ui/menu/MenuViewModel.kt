package com.pochipochi.cafe_app.ui.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pochipochi.cafe_app.data.model.ProductsModel
import com.pochipochi.cafe_app.data.repository.ProductsRepository
import com.pochipochi.cafe_app.ui.shops.ShopsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MenuState(
    val menu: List<ProductsModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)


class MenuViewModel : ViewModel() {
    private val productsRepository = ProductsRepository()
    private val _state = MutableStateFlow(MenuState())
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
}