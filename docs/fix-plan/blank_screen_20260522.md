# 空画面表示の修正計画

作成日: 2026-05-22

## 現象

アプリを起動しても画面に何も表示されない。

## 確認した範囲

- `MainActivity` は `CafeappTheme` の中で `AppRouter()` を呼び出している。
- `AppRouter` は `ShopsScreen()` を呼び出している。
- `ShopsScreen` は `ShopsViewModel.state` を購読し、`state.shops` を `LazyColumn` で表示する。
- `ShopsViewModel` には `fetchShops()` があるが、画面側から呼び出されていない。
- `ShopsRepository.fetch()` のリクエスト先が `"link"` のままで、有効な URL ではない。

## 考えられる原因

1. `fetchShops()` が未呼び出し
   - `ShopsState` の初期値は `shops = emptyList()`、`isLoading = false`。
   - そのため `LazyColumn` は表示されるが item 数が 0 になり、結果として空画面になる。

2. データ取得失敗時の表示がない
   - 取得エラー時は `state.error` にメッセージが入るが、`ShopsScreen` では表示していない。
   - API URL が未設定のため、取得処理を呼び出してもユーザーには何も見えない可能性が高い。

3. ローディング表示の配置が弱い
   - `CircularProgressIndicator` に幅のみ指定されており、画面中央に出ない。
   - 表示自体はされるが、端に寄って見落としやすい。

## 対策方針

1. `ShopsScreen` 表示時に `LaunchedEffect(Unit)` で `viewModel.fetchShops()` を一度だけ呼び出す。
2. `state.error` がある場合はエラーメッセージを表示する。
3. `isLoading` 中は画面中央にインジケーターを表示する。
4. 取得成功後に `state.shops` が空の場合は空状態メッセージを表示する。
5. Repository の URL は現時点で正しい API エンドポイントが不明なため、今回の Kotlin 修正では触らない。必要な URL が確定したら別途 Repository 修正を行う。

## 変更予定ファイル

- `app/src/main/java/com/pochipochi/cafe_app/ui/shops/ShopsScreen.kt`

## 具体的な修正内容

- `LaunchedEffect` を追加して初回表示時に `fetchShops()` を実行する。
- `Box` と `fillMaxSize` を使い、ローディング、エラー、空状態を視認できるようにする。
- 店舗一覧は既存の `LazyColumn` と `ShopsItem` を維持し、必要最小限の表示改善に留める。

## 検証方法

- APK 生成を伴わない `./gradlew :app:compileDebugKotlin` を実行する。

## 注意点

- Kotlin 変更は、変更対象と内容についてユーザーの許可を得てから行う。
- Gradle、Manifest の変更は行わない。
- API エンドポイントの確定や Manifest の INTERNET 権限追加が必要になった場合は、別の許可対象として扱う。
