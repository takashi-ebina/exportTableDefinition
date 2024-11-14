# accesslog（アクセスログ）

## 基本情報

| RDBMS | データベース名| 作成日 |
|:---|:---|:---|
|PostgreSQL|testdb|2024/10/06|

## テーブル説明

## テーブル情報

| スキーマ名 | 論理テーブル名 | 物理テーブル名 | 区分 | 備考 |
|:---|:---|:---|:---|:---|
|test_plpgsql|アクセスログ|accesslog|table||

## カラム情報

| No. | 論理名 | 物理名 | データ型 | PK | Not Null | デフォルト | 備考 |
|:---|:---|:---|:---|:---|:---|:---|:---|
|1|ID|id|numeric(10,0)|○|○| ||
|2|ユーザID|userid|character varying(10)|○|○| ||
|3|取引コード|opecode|character varying(10)|○|○| ||
|4|取引ステータス|opests|numeric(1,0)|○|○| ||
|5|ログメッセージ|logmsg|character varying(256)||| ||
|6|アクセス日時|accesstime|timestamp(6) without time zone||| ||

## インデックス情報

| No. | インデックス名 | カラムリスト |
|:---|:---|:---|

## 制約情報

| No. | 制約名 | 種類 | 制約定義 |
|:---|:---|:---|:---|
|1|accesslog_pkey|PRIMARY KEY|PRIMARY KEY (id, userid, opecode, opests)|

## 外部キー情報

| No. | 外部キー名 | カラムリスト | 参照先 | 参照先カラムリスト |
|:---|:---|:---|:---|:---|

___
[テーブル一覧へ](../../../tableList_testdb.md)  
