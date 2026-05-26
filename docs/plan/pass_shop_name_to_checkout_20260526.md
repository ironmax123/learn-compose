# 店舗名を決済画面へ渡す実装計画

作成日: 2026-05-26

## 目的

店舗一覧で選択した店舗名を会計画面まで保持し、決済内容とともに表示できるようにする。

## 現状

- `ShopsScreen` は店舗項目タップ時に引数なしの `onNavigateToMenu()` を呼んでおり、選択した店舗情報を Router へ渡していない。
- `CheckScreen` には既に `shopName: String` 引数と店舗名の表示箇所が追加されている。
- `MenuScreen` にも `shopName: String` 引数が追加されている。
- `AppRouter` の `MenuScreen` および `CheckScreen` 呼び出しには `shopName=` の未完成箇所があり、現在は Kotlin コードとして未完了である。
- `AppRouter` が `MenuViewModel` を生成して `menuState` を購読し、カート内容と合計額を `CheckScreen` に供給しようとしている。
- Router が画面固有の ViewModel と状態を所有する構造は、遷移定義の責務を越えている。

## 実装方針

1. 店舗選択時に店舗名をコールバックで渡す。
   - `ShopsScreen` の `onNavigateToMenu` を `(String) -> Unit` に変更する。
   - `ShopsItem` のタップ時に対象の `shops.name` を渡す。

2. 店舗名は navigation 引数として配送する。
   - Router 内に店舗状態を追加せず、店舗一覧から Menu への route に選択店舗名を含める。
   - Menu から Check への route にも店舗名を含め、`CheckScreen` に渡す。

3. 既存の画面引数へ店舗名を供給する。
   - `MenuScreen` の既存 `shopName` 引数へ Menu route から取得した店舗名を渡す。
   - `CheckScreen` の既存 `shopName` 引数へ Check route から取得した店舗名を渡す。
   - 会計画面側の既存店舗名表示はそのまま利用する。

4. Router から `MenuViewModel` 参照を除去する。
   - `MenuScreen` は既存のデフォルト引数により自身の範囲で `MenuViewModel` を取得する。
   - `MenuDrawerComponent` の会計遷移コールバックを、カート内容と合計額を渡す形に変更する。
   - `MenuScreen` は Drawer から受けたカート内容と合計額を Router への遷移コールバックへ引き渡す。
   - Router は `MenuViewModel` や `menuState` を購読せず、受け取ったデータを Check route の引数として配送する。

5. カート内容は既存の Serializable モデルを navigation 用文字列へ変換して渡す。
   - `ProductsModel` は既に `@Serializable` のため、カート内容を JSON 化して URL エンコードした route 引数に載せる。
   - Check route で JSON を `List<ProductsModel>` に復元し、既存の `CheckScreen(cartItem, amount, shopName)` に供給する。

## 変更予定ファイル

### Kotlin

- `app/src/main/java/com/pochipochi/cafe_app/ui/shops/ShopsScreen.kt`
  - 遷移コールバックの型を変更し、タップされた店舗名を通知する。
- `app/src/main/java/com/pochipochi/cafe_app/ui/router/AppRouter.kt`
  - `MenuViewModel` と `menuState` の参照を削除する。
  - 店舗名、カート内容、合計額を navigation 引数として配送する route を定義する。
  - 未完成の `shopName=` を route 引数で埋め、Menu／Check に渡す。
- `app/src/main/java/com/pochipochi/cafe_app/ui/menu/MenuScreen.kt`
  - 会計遷移コールバックを、カート内容と合計額を渡す型に変更する。
  - Router から `MenuViewModel` を渡さなくても既存の画面内 ViewModel が使われる形を維持する。
- `app/src/main/java/com/pochipochi/cafe_app/ui/menu/components/MenuDrawer.kt`
  - 会計ボタン押下時に、現在の `state.cartItems` と `state.cartTotalAmount` を遷移コールバックへ渡す。

## 変更しない対象

- `CheckScreen.kt` の既存表示実装
- ViewModel、Gradle、Manifest

## 確認方法

- APK を生成しない `./gradlew :app:compileDebugKotlin --console=plain` を実行し、店舗名引き渡しを含む Kotlin コードがコンパイル可能であることを確認する。

## 許可確認対象

- Kotlin 変更として `ShopsScreen.kt`、`AppRouter.kt`、`MenuScreen.kt`、`MenuDrawer.kt` を変更する。コード変更は対象と内容を提示して許可を得てから行う。
