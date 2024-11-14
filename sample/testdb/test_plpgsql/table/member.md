# member

## 基本情報

| RDBMS | データベース名| 作成日 |
|:---|:---|:---|
|PostgreSQL|testdb|2024/10/06|

## テーブル説明

## テーブル情報

| スキーマ名 | 論理テーブル名 | 物理テーブル名 | 区分 | 備考 |
|:---|:---|:---|:---|:---|
|test_plpgsql||member|table||

## カラム情報

| No. | 論理名 | 物理名 | データ型 | PK | Not Null | デフォルト | 備考 |
|:---|:---|:---|:---|:---|:---|:---|:---|
|1||deptno|character(5)|○|○| ||
|2||empno|character(5)|○|○| ||

## インデックス情報

| No. | インデックス名 | カラムリスト |
|:---|:---|:---|

## 制約情報

| No. | 制約名 | 種類 | 制約定義 |
|:---|:---|:---|:---|
|1|member_deptno_fkey|FOREIGN KEY|FOREIGN KEY (deptno) REFERENCES test_plpgsql.dept(deptno)|
|2|member_empno_fkey|FOREIGN KEY|FOREIGN KEY (empno) REFERENCES test_plpgsql.emp(empno)|
|3|member_pkey|PRIMARY KEY|PRIMARY KEY (deptno, empno)|

## 外部キー情報

| No. | 外部キー名 | カラムリスト | 参照先 | 参照先カラムリスト |
|:---|:---|:---|:---|:---|
|1|member_deptno_fkey|deptno|test_plpgsql.dept|deptno|
|2|member_empno_fkey|empno|test_plpgsql.emp|empno|

___
[テーブル一覧へ](../../../tableList_testdb.md)  
