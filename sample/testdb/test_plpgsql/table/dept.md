# dept

## 基本情報

| RDBMS | データベース名| 作成日 |
|:---|:---|:---|
|PostgreSQL|testdb|2024/10/06|

## テーブル説明

## テーブル情報

| スキーマ名 | 論理テーブル名 | 物理テーブル名 | 区分 | 備考 |
|:---|:---|:---|:---|:---|
|test_plpgsql||dept|table||

## カラム情報

| No. | 論理名 | 物理名 | データ型 | PK | Not Null | デフォルト | 備考 |
|:---|:---|:---|:---|:---|:---|:---|:---|
|1||deptno|character(5)|○|○| ||
|2||deptname|character varying(40)||○| ||

## インデックス情報

| No. | インデックス名 | カラムリスト |
|:---|:---|:---|
|1|dept_deptno_deptname_idx|deptno,deptname|

## 制約情報

| No. | 制約名 | 種類 | 制約定義 |
|:---|:---|:---|:---|
|1|dept_pkey|PRIMARY KEY|PRIMARY KEY (deptno)|
|2|dept_deptname_key|UNIQUE|UNIQUE (deptname)|

## 外部キー情報

| No. | 外部キー名 | カラムリスト | 参照先 | 参照先カラムリスト |
|:---|:---|:---|:---|:---|

___
[テーブル一覧へ](../../../tableList_testdb.md)  
