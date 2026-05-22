package com.pochipochi.cafe_app.ui.shops

import androidx.compose.foundation.interaction.HoverInteraction
import androidx.lifecycle.ViewModel
import com.pochipochi.cafe_app.data.model.ShopsModel
import com.pochipochi.cafe_app.data.repository.ShopsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

data class ShopsState(
    val shops: List<ShopsModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ShopsViewModel: ViewModel() {
    private val shopsRepository = ShopsRepository()
    private val _state = MutableStateFlow(ShopsState())
    val state: StateFlow<ShopsState> = _state.asStateFlow()

    fun fetchShops() {
        viewModelScope.launch {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val shops = shopsRepository.fetch()
            _state.value = _state.value.copy(shops = shops, isLoading = false)
        }catch (e: Exception){
            print("店舗情報取得エラー:e.message")
            _state.value = _state.value.copy(isLoading = false, error = "エラーが発生しました。しばらくしてからやり直してください。")
        }
        }
    }
}

