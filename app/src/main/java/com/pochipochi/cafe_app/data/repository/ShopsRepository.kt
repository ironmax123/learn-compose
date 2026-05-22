package com.pochipochi.cafe_app.data.repository

import android.util.Log
import com.pochipochi.cafe_app.data.model.ShopsModel
import com.pochipochi.cafe_app.data.model.ShopsResponseDto
import io.ktor.client.call.body
import io.ktor.client.request.get


class ShopsRepository {
    suspend fun fetch():  List<ShopsModel>  {
        val response: ShopsResponseDto = NetworkClient.client.get("https://cafe-api.flutterapp-dev-12345.workers.dev/api/shops").body()
        Log.d("response", "response:$response")
        return response.data
    }
}