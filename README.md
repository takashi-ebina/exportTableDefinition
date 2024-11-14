# exportTableDefinition

## Overview

DBからMarkdown形式のテーブル定義書を作成するリポジトリ

## Description

対象のDBに接続し、テーブル一覧と各TBLのテーブル一覧をMarkdown形式で出力します。

### sample

[テーブル一覧](./sample/tableList_testdb.md)

### 対象DBMS
* PostgreSQL
* Oracle(一部制限あり)
    * Oracleの場合は、以下の項目の出力が不可
        * デフォルト値
        * view／materialized_viewのソース
        * Check制約の定義 

### 主なディレクトリ構成

```
exportTableDefinition
│  build
│  └─libs
│      ├─conf  ・・・ 設定ファイルが格納されているフォルダ
│      │  ├─ExportTableDefinition.properties
│      │  └─mybatis.properties
│      ├─output
│      └─exportTableDefinition-1.0-SNAPSHOT.jar ・・・ 実行可能形式Jarファイル
├─docs           ・・・ JavaDoc等のドキュメントが格納されているフォルダ
│  └─javadoc
├─gradle
│  └─wrapper
└─src
    └─ main     ・・・ javaソースコードが格納されているフォルダ
        └─ java
             └─ com
                 └─ export_table_definition
```

## Usage

### build

以下のコマンドを実行することで、`exportTableDefinition/build/libs`フォルダ配下に`exportTableDefinition-1.0-SNAPSHOT.jar`が作成される

```
gradle build
```

### Javadoc

以下のコマンドを実行することで、`exportTableDefinition/docs/javadoc`フォルダ配下にjavadocが作成される

```
gradle javadoc
```

### 実行方法

`conf/ExportTableDefinition.properties`及び`conf/mybatis.properties`に必要な設定値を記載した状態で以下のコマンドを実行する

```
java -jar .\exportTableDefinition-1.0-SNAPSHOT.jar
```

### ExportTableDefinition.properties の記載内容

```
schema={テーブル定義出力対象のスキーマ名（複数存在する場合はカンマ区切り）} ※空白の場合はinformation_schema／pg_catalogを除く全スキーマを対象
table={テーブル定義出力対象のテーブル名（複数存在する場合はカンマ区切り）} ※空白の場合は全テーブルを対象
outputPath={テーブル定義出力先のディレクトリパス}　※空白の場合は./outputにテーブル定義が出力されます
```

### mybatis.properties の記載内容

```
driver=ドライバーの名称
url=データベース接続先のURL
username=ユーザ名
password=パスワード
```
