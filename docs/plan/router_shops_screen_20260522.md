# RouterでShopsScreenを表示する実装計画

作成日: 2026-05-22

## 目的

アプリ起動時に `MainActivity` から Router を経由して `ShopsScreen` を表示できる構成にする。

## 現状

- `MainActivity` は `Greeting` サンプルを直接表示している。
- `app/src/main/java/com/pochipochi/cafe_app/ui/shops/ShopsScreen.kt` は存在する。
- `ShopsScreen` は現在 `ComponentActivity` として定義されている。
- Router 用の Kotlin ファイルはまだ存在しない。

## 実装方針

1. Router 用の Composable を追加する。
   - `AppRouter` のようなエントリ Composable を定義する。
   - 初期表示として shops 画面を表示する。

2. `MainActivity` から Router を呼び出す。
   - `Greeting` サンプル表示をやめる。
   - `CafeappTheme` の内側で Router を表示する。

3. `ShopsScreen` を Router から表示できる形にする。
   - Router から呼べる Composable を用意する。
   - 既存の表示内容は必要最小限の変更に留める。

## 変更予定ファイル

- `app/src/main/java/com/pochipochi/cafe_app/MainActivity.kt`
- `app/src/main/java/com/pochipochi/cafe_app/ui/router/AppRouter.kt`
- `app/src/main/java/com/pochipochi/cafe_app/ui/shops/ShopsScreen.kt`

## 確認方法

- APK生成を伴わない `./gradlew :app:compileDebugKotlin` で Kotlin コンパイルを確認する。

## 注意点

- Kotlin変更は、この計画書作成後に変更対象と内容を明示し、許可を得てから行う。
- Gradle、Manifest、docs の追加変更は行わない。
