# ShopsScreen / CheckScreen のダークテーマ不整合修正計画

作成日: 2026-05-26

## 症状

`ShopsScreen` と `CheckScreen` でダークテーマ時に、テーマに追従する部分と追従しない部分が混在して見える。

## 確認結果

- `ShopsScreen` は一部の文字色に `MaterialTheme.colorScheme.onSurface` を使っている。
- `ShopsSearch` は検索欄の外枠やコンテナ色に `MaterialTheme.colorScheme` を使っている。
- 一方で、`ShopsItem` 内の本文テキストは色指定がなく、親の既定色に依存している。
- 画面全体を包む明示的な `Surface` や背景指定がなく、ウィンドウ背景と Compose 側の配色が分離して見える可能性がある。
- ダークテーマ不整合の主因は、MaterialTheme 参照とデフォルト値依存が混在していること、およびルート背景をテーマ色に固定していないことにある。
- `CheckScreen` は一部のテキストがテーマ準拠だが、画面全体を包む背景と区切り線、ボトムシート側の一部色が固定値に依存している。
- `WalletPaymentBottomSheet` はアクセント色に固定の青を使っており、ダークテーマ時に画面全体の配色と馴染み切らない。

## 原因

`ShopsScreen` が画面全体の背景と個々のコンポーネントの配色を統一しておらず、`MaterialTheme` に乗る箇所と未指定の箇所が混在している。

## 対策

画面全体をテーマの `background` / `surface` に合わせて構築し、`ShopsItem`、`ShopsSearch`、`CheckScreen`、`WalletPaymentBottomSheet` の文字色・補助色・区切り線色・アクセント色を `MaterialTheme.colorScheme` ベースに揃える。

## 修正予定

### Kotlin

- `app/src/main/java/com/pochipochi/cafe_app/ui/shops/ShopsScreen.kt`
  - 画面ルートを `Surface(color = MaterialTheme.colorScheme.background)` で包む。
  - 店舗名、説明、電話番号、住所の文字色をテーマ色で明示する。
  - 区切り線の色をテーマ色に合わせる。

- `app/src/main/java/com/pochipochi/cafe_app/ui/shops/components/ShopsSearch.kt`
  - 検索欄の前景色・アイコン色・プレースホルダー色をテーマに合わせて明示する。
  - 必要なら `OutlinedTextFieldDefaults.colors` の指定を拡張し、ダークテーマでも一貫した見た目にする。

- `app/src/main/java/com/pochipochi/cafe_app/ui/check/CheckScreen.kt`
  - 画面ルートをテーマ背景に合わせる。
  - 店舗名、合計額、明細テキスト、区切り線をテーマ色で明示する。

- `app/src/main/java/com/pochipochi/cafe_app/ui/check/components/WalletPaymentBottomSheet.kt`
  - 固定の青系アクセントを必要最小限に絞り、テーマ由来の配色へ寄せる。
  - ボトムシート全体の背景と文字色を `MaterialTheme.colorScheme` 基準に揃える。

### テーマ

- `app/src/main/java/com/pochipochi/cafe_app/ui/theme/Theme.kt`
  - 必要に応じて `Surface` を前提とした配色の確認のみ行う。
  - 現時点では配色定義の大きな変更は行わない。

## 変更しない対象

- `MenuScreen.kt`
- `CheckScreen.kt`
- `AppRouter.kt`
- ViewModel
- Gradle
- Manifest

## 確認方法

- APK を生成しない `./gradlew :app:compileDebugKotlin --console=plain` を実行し、Kotlin コードがコンパイル可能であることを確認する。

## 許可確認対象

- Kotlin 変更として `ShopsScreen.kt`、`ShopsSearch.kt`、`CheckScreen.kt`、`WalletPaymentBottomSheet.kt`、`WalletPaymentDialog.kt` を修正する。コード変更は対象と内容を提示して許可を得てから行う。
