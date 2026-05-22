package com.pochipochi.cafe_app.data.repository
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.ContentType
import io.ktor.http.contentType

object NetworkClient {
    val client = HttpClient(Android) {
        install(ContentNegotiation) { // JSON シリアライゼーション
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = false
            })
        }
        install(HttpTimeout) { // タイムアウト設定
            requestTimeoutMillis = 15000
            connectTimeoutMillis = 15000
            socketTimeoutMillis = 15000
        }
        install(Logging) { // ログ出力
            level = LogLevel.INFO
        }
        install(DefaultRequest) { // デフォルトリクエスト設定
            contentType(ContentType.Application.Json)
        }
    }
}
