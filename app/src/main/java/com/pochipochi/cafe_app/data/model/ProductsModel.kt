package com.pochipochi.cafe_app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProductsModel(
    val id: String,
    val name: String,
    val price: Int,
    val imageUrl: String,
    val category: String,
    val description: String
)

@Serializable
data class ProductsResponseDto(
    val status: Int,
    val data: List<ProductsModel>
)
