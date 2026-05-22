package com.pochipochi.cafe_app.data.model
import kotlinx.serialization.Serializable

@Serializable
data class ShopsModel(
    val id: String,
    val name: String,
    val prefecture: String,
    val address: String,
    val phone: String,
    val businessHours: String,
    val description: String
)
@Serializable
data class ShopsResponseDto(
    val status: Int,
    val data: List<ShopsModel>
)