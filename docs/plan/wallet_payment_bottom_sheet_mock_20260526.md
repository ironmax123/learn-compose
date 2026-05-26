# ウォレット支払いボトムシートコピーモックの実装計画

作成日: 2026-05-26

## 目的

「ウォレットで支払う」操作に使用できる、Google ウォレット風の支払いボトムシートのコピーモック UI を作成する。

今回の範囲はボトムシート Composable の作成のみとし、画面への接続、決済処理、Google Wallet API 連携、実在するカード情報の利用は行わない。

## 現状

- `app/src/main/java/com/pochipochi/cafe_app/ui/check/CheckScreen.kt` には「ウォレットで支払う」ボタンがあり、`onClick` は未実装である。
- `CheckScreen`、`AppRouter`、カート関連の Kotlin ファイルには未コミットの作業中変更が存在する。
- 支払い用ボトムシートの Composable はまだ存在しない。

## 実装方針

1. 会計 UI に属する独立した Composable ファイルを新規作成する。
   - 配置先は `ui/check/components/WalletPaymentBottomSheet.kt` とする。
   - 呼び出し元から表示状態を制御できるよう、`onDismissRequest` と支払い操作用の `onPayClick` を引数として受け取る。
   - 表示額を外部から差し込めるよう、`amount: Int` を受け取る。

2. Material3 の `ModalBottomSheet` でコピーモックの外観を構築する。
   - 上部にウォレット支払いを示すヘッダーを配置する。
   - 支払い先、合計額、選択済みカード風の表示、決済確認ボタンを配置する。
   - カード番号等はモック用のマスク表現のみとし、実データは扱わない。

3. Google Wallet の機能実装は含めない。
   - SDK、API、認証、支払い完了処理、外部アプリ起動、カード登録処理は追加しない。
   - 実在サービスのロゴ画像やブランドアセットは追加せず、Compose のテキスト・図形・既存アイコンで「ウォレット風」レイアウトを作成する。

4. 作成のみとし、既存 UI には接続しない。
   - `CheckScreen` の「ウォレットで支払う」ボタンの `onClick` は変更しない。
   - Router、Menu、ViewModel、Gradle、Manifest は変更しない。

## 変更予定ファイル

### Kotlin

- `app/src/main/java/com/pochipochi/cafe_app/ui/check/components/WalletPaymentBottomSheet.kt` を新規作成する。
  - `WalletPaymentBottomSheet(amount, onDismissRequest, onPayClick)` Composable を定義する。
  - Material3 のボトムシート内に、支払い概要、モックカード、支払いボタンを表示する。

## 変更しない対象

- `app/src/main/java/com/pochipochi/cafe_app/ui/check/CheckScreen.kt`
- `app/src/main/java/com/pochipochi/cafe_app/ui/router/AppRouter.kt`
- `app/src/main/java/com/pochipochi/cafe_app/ui/menu/` 配下
- `app/build.gradle.kts`
- `gradle/libs.versions.toml`
- AndroidManifest ファイル

## 確認方法

- APK を生成しない `./gradlew :app:compileDebugKotlin --console=plain` を実行し、新規 Composable がコンパイル可能であることを確認する。
- 既存画面への表示接続は今回行わないため、実際にボトムシートを開く動作確認は接続タスクの対象とする。

## 許可確認対象

- Kotlin 変更として、新規ファイル `WalletPaymentBottomSheet.kt` の追加のみを行う。コード変更は対象と内容を提示して許可を得てから行う。
