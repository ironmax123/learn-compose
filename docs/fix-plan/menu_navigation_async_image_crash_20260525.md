# メニュー画面遷移時クラッシュの修正計画

作成日: 2026-05-25

## 症状

店舗一覧の項目をタップしてメニュー画面へ遷移しようとすると、アプリがクラッシュする。

## 確認結果

- `AppRouter` は店舗一覧から `detail` route へ遷移し、遷移先として `MenuScreen` を表示する。
- `MenuScreen` は `LazyColumn` 内で `MenuItem` を描画する。
- `MenuItem` は同ファイル内の `AsyncImage` Composable を呼び出す。
- 現在の `AsyncImage` 実装は `TODO("Not yet implemented")` のみであり、Composition 時に `NotImplementedError` を必ず送出する。
- `./gradlew :app:compileDebugKotlin` は成功する。`TODO()` はコンパイルエラーではないため、問題は遷移時の実行時例外として発生する。

## 原因

`app/src/main/java/com/pochipochi/cafe_app/ui/menu/MenuScreen.kt` に仮実装として残っている `AsyncImage` が、メニュー画面の描画時に無条件で `TODO()` を実行することが直接原因である。

## 対策

Coil 3 の Compose 対応ライブラリを導入し、仮の `AsyncImage` 関数ではなく `coil3.compose.AsyncImage` を使用して画像を表示する。

ネットワーク画像を表示する目的で既に `coil-network-okhttp` が依存に追加されているため、画像表示機能を削除してクラッシュだけ回避するのではなく、不足している Compose 側の Coil 依存を追加して意図した UI を成立させる。

## 修正予定

### Gradle

- `app/build.gradle.kts`
  - `implementation("io.coil-kt.coil3:coil-compose:3.4.0")` を追加する。
  - 既存の `coil-network-okhttp:3.4.0` はネットワーク画像取得に必要なため保持する。

### Kotlin

- `app/src/main/java/com/pochipochi/cafe_app/ui/menu/MenuScreen.kt`
  - `coil3.compose.AsyncImage` を import する。
  - `TODO()` を実行するローカル `AsyncImage` Composable を削除し、Coil の `AsyncImage` が呼ばれるようにする。
  - 既存の戻る操作、一覧表示、表示文字列は変更しない。

## 確認方法

- APK を生成しない `./gradlew :app:compileDebugKotlin` を実行し、変更後の Kotlin コードと依存解決が成立することを確認する。
- 実機またはエミュレーターでの遷移確認は、APK 生成を行わないというプロジェクトルール上、この作業では行わない。

## 変更対象外

- `AppRouter.kt` の route 構成
- `ShopsScreen.kt` のタップ処理
- Manifest
