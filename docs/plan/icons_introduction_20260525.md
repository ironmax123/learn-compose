# Icons 導入の実装計画

作成日: 2026-05-25

## 目的

検索 UI で使用する Compose の検索アイコンを解決できる依存構成にし、`Icons.Default.Search` を利用できる状態にする。

## 現状

- `app/src/main/java/com/pochipochi/cafe_app/ui/shops/components/ShopsSearch.kt` は `androidx.compose.material.icons.Icons` と `androidx.compose.material.icons.filled.Search` を import している。
- `gradle/libs.versions.toml` と `app/build.gradle.kts` には未コミットの変更として `androidx.wear.compose:compose-material` が追加されている。
- Wear Compose Material は `androidx.compose.material.icons` の依存ではないため、検索アイコンの参照を解決しない。
- `./gradlew :app:compileDebugKotlin` では `ShopsSearch.kt` の `icons` / `Icons` が unresolved reference となる。
- 同コンパイルには `AppRouter.kt` の experimental Material3 API に関する別エラーもあり、Icons 導入だけではコンパイル全体が成功しない可能性がある。

## 実装方針

1. 通常の Jetpack Compose Material アイコン用依存を追加する。
   - 標準の `Search` アイコンを提供する `androidx.compose.material:material-icons-core` を version catalog に定義する。
   - 既に導入されている Compose BOM にバージョン管理を委ね、アイコンライブラリ単独のバージョンは定義しない。

2. 誤って追加された Wear 用依存を置き換える。
   - `androidx.wear.compose:compose-material` の alias と専用バージョン定義を削除する。
   - `app/build.gradle.kts` の依存参照を Material icons の alias に置き換える。

3. Kotlin 実装は今回変更しない。
   - `ShopsSearch.kt` に既に存在する `Icons.Default.Search` の参照を、正しい依存追加によって解決する。
   - 既存の検索 UI や `ShopsScreen.kt` の未コミット変更は保持する。

## 変更予定ファイル

- `gradle/libs.versions.toml`
  - `composeMaterial` のバージョン定義と Wear 用 alias を削除する。
  - `androidx.compose.material:material-icons-core` の alias を追加する。
- `app/build.gradle.kts`
  - `implementation(libs.androidx.compose.material)` を Material icons 用 alias の参照に置き換える。

## 変更しない対象

- `app/src/main/java/com/pochipochi/cafe_app/ui/shops/components/ShopsSearch.kt`
- `app/src/main/java/com/pochipochi/cafe_app/ui/shops/ShopsScreen.kt`
- `app/src/main/java/com/pochipochi/cafe_app/ui/router/AppRouter.kt`
- AndroidManifest ファイル

## 確認方法

- APK を生成しない `./gradlew :app:compileDebugKotlin` を実行し、`ShopsSearch.kt` のアイコン unresolved reference が解消されることを確認する。
- `AppRouter.kt` の experimental API エラーが継続する場合は、Icons 導入とは別の修正対象として切り分ける。

## 許可確認対象

- Gradle 変更は、この計画書作成後に対象ファイルと変更内容を提示し、許可を得てから行う。
- Kotlin と Manifest の変更は今回予定しない。
