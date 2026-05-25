# カートアイコン件数バッジの実装計画

作成日: 2026-05-25

## 目的

メニュー画面のカートアイコンに、カートへ追加済みの商品数を赤背景の丸いバッジとして表示する。

## 現状

- `app/src/main/java/com/pochipochi/cafe_app/ui/menu/MenuScreen.kt` のトップバー actions に `Icons.Default.ShoppingCart` が表示されている。
- カートアイコンを押すと右側の Drawer が開く。
- `MenuViewModel` の `MenuState.cartItems` にカートの商品が保持され、`cart(id)` 実行後に更新される。
- `MenuDrawerComponent` は既に `state.cartItems.size` を件数として表示している。

## 実装方針

1. 既存のカートアイコンを `Box` 内に配置し、左上に円形の件数表示を重ねる。
2. `state.cartItems.size` をバッジの表示件数に使用し、Drawer と同じ状態を参照する。
3. カートが空のときはバッジを表示しない。
4. 1 件以上の場合のみ、赤背景・白文字の円形 `Box` に件数を表示する。
5. カートアイコンのクリック処理と Drawer の開閉処理は変更しない。

## 変更予定ファイル

- `app/src/main/java/com/pochipochi/cafe_app/ui/menu/MenuScreen.kt`
  - 円形背景のために `CircleShape` の import を追加する。
  - トップバーのカート `Icon` の周囲に件数バッジ表示を追加する。

## 変更しない対象

- `MenuViewModel.kt`
- `components/MenuDrawer.kt`
- Gradle および Manifest

## 確認方法

- APK を生成しない `./gradlew :app:compileDebugKotlin` を実行し、Kotlin コードがコンパイル可能であることを確認する。
- 実行確認時には、カートが空ならバッジ非表示、商品追加後はカートアイコン左上に件数が表示されることを確認対象とする。

## 許可確認対象

- Kotlin 変更として `MenuScreen.kt` のみを変更する。コード変更は対象と内容を提示して許可を得てから行う。
