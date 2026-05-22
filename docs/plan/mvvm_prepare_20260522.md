# MVVM準備 実装計画

作成日: 2026-05-22

## 目的

Cafe App の画面実装を進める前に、Compose + MVVM で機能を追加しやすい土台を用意する。
現時点では `MainActivity` にサンプル表示が直接書かれているため、画面、状態、ViewModel、データ層の責務を分けられる構成にする。

## 現状

- Android アプリモジュールは `app` のみ。
- UI は Jetpack Compose / Material3 を使用している。
- `MainActivity` に `Greeting` のサンプル Composable が直接定義されている。
- `AndroidManifest.xml` に `.ui.ShopsActivity` の定義があるが、対応する Kotlin ファイルは存在しない。
- `androidx.lifecycle:lifecycle-runtime-ktx` は入っているが、Compose から ViewModel を扱う依存関係は未追加。

## 実装方針

1. MVVM 向けのパッケージ構成を作成する。
   - `ui/screen/...`: Compose 画面
   - `ui/viewmodel/...`: ViewModel
   - `data/model/...`: 表示や永続化の元になるデータモデル
   - `data/repository/...`: データ取得元を隠蔽する Repository

2. Compose から ViewModel を利用できる依存関係を追加する。
   - Version Catalog に `androidx.lifecycle:lifecycle-viewmodel-compose` を追加する。
   - `app/build.gradle.kts` に implementation を追加する。

3. 最小の MVVM サンプルを作成する。
   - カフェ一覧の仮データモデルを作る。
   - Repository で仮データを返す。
   - ViewModel が UI 状態を保持する。
   - Compose 画面が ViewModel の状態を表示する。

4. `MainActivity` を画面のエントリポイントとして整理する。
   - `Greeting` サンプルを削除または置き換える。
   - `CafeListScreen` のような画面 Composable を呼び出す。

5. Manifest の不整合を解消する。
   - 実体のない `.ui.ShopsActivity` は削除する。
   - Activity はひとまず `MainActivity` に集約する。

## 変更予定ファイル

- `gradle/libs.versions.toml`
- `app/build.gradle.kts`
- `app/src/main/AndroidManifest.xml`
- `app/src/main/java/com/pochipochi/cafe_app/MainActivity.kt`
- `app/src/main/java/com/pochipochi/cafe_app/data/model/Cafe.kt`
- `app/src/main/java/com/pochipochi/cafe_app/data/repository/CafeRepository.kt`
- `app/src/main/java/com/pochipochi/cafe_app/data/repository/FakeCafeRepository.kt`
- `app/src/main/java/com/pochipochi/cafe_app/ui/screen/cafe/CafeListScreen.kt`
- `app/src/main/java/com/pochipochi/cafe_app/ui/viewmodel/CafeListUiState.kt`
- `app/src/main/java/com/pochipochi/cafe_app/ui/viewmodel/CafeListViewModel.kt`

## 確認方法

1. Gradle 設定が解決できることを確認する。
   - `./gradlew :app:assembleDebug`

2. Kotlin / Compose のコンパイルが通ることを確認する。
   - `./gradlew :app:compileDebugKotlin`

3. アプリ起動時に `MainActivity` からカフェ一覧の仮画面が表示されることを確認する。

## 注意点

- まだ永続化や API 通信は追加しない。
- DI ライブラリはこの段階では追加しない。必要になった時点で Hilt などを検討する。
- Repository は後から実装差し替えできるように interface と fake 実装に分ける。
- コード変更はこの計画を確認して許可を得たあとに行う。
