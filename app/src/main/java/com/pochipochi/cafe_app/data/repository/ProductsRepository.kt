package com.pochipochi.cafe_app.data.repository

import android.util.Log
import com.pochipochi.cafe_app.data.model.ProductsModel
import com.pochipochi.cafe_app.data.model.ProductsResponseDto
import com.pochipochi.cafe_app.data.model.ShopsResponseDto
import io.ktor.client.call.body
import io.ktor.client.request.get

class ProductsRepository {
    suspend fun fetch(): List<ProductsModel> {
        val response: ProductsResponseDto =
            NetworkClient.client.get("https://cafe-api.flutterapp-dev-12345.workers.dev/api/products")
                .body()
        Log.d("response", "response:$response")
        return response.data
    }
}