# Serializer not found エラーの修正計画

作成日: 2026-05-22

## 現象

店舗情報取得時に以下のエラーが発生する。

```text
Serializer for class 'ShopsResponseDto' is not found.
Please ensure that class is marked as '@Serializable' and that the serialization compiler plugin is applied.
```

## 確認した範囲

- `ShopsRepository.fetch()` は Ktor の `body<ShopsResponseDto>()` でレスポンスを DTO に変換している。
- `NetworkClient` は `ContentNegotiation` と `kotlinx-json` を設定している。
- `ShopsModel` と `ShopsResponseDto` は `data class` だが `@Serializable` が付いていない。
- `app/build.gradle.kts` とルート `build.gradle.kts` に `org.jetbrains.kotlin.plugin.serialization` が設定されていない。
- `gradle/libs.versions.toml` には Kotlin バージョン `2.2.10` があるため、同じバージョンで serialization plugin を追加できる。

## 考えられる原因

1. DTO に `@Serializable` がない
   - kotlinx.serialization は対象クラスに serializer を生成するため、DTO クラスに `@Serializable` が必要。

2. Kotlin Serialization compiler plugin が未適用
   - `@Serializable` を付けても、compiler plugin がないと serializer が生成されない。

## 対策方針

1. `gradle/libs.versions.toml` に Kotlin serialization plugin alias を追加する。
2. ルート `build.gradle.kts` に serialization plugin を `apply false` で追加する。
3. `app/build.gradle.kts` の plugins に serialization plugin を適用する。
4. `ShopsModel` と `ShopsResponseDto` に `@Serializable` を付ける。

## 変更予定ファイル

### Gradle

- `gradle/libs.versions.toml`
- `build.gradle.kts`
- `app/build.gradle.kts`

### Kotlin

- `app/src/main/java/com/pochipochi/cafe_app/data/model/ShopsModel.kt`

## 検証方法

- APK 生成を伴わない `./gradlew :app:compileDebugKotlin` を実行する。

## 注意点

- Manifest は変更しない。
- Repository の API URL は今回の serializer エラーとは別問題のため変更しない。
