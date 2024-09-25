# exportTableDefinition

## Overview

DBからMarkdown形式のテーブル定義書を作成するリポジトリ

## Description

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

### ビルド

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
schema=テーブル定義出力対象のスキーマ名（複数存在する場合はカンマ区切り） ※空白の場合はinformation_schema／pg_catalogを除く全スキーマを対象
table=テーブル定義出力対象のテーブル名（複数存在する場合はカンマ区切り） ※空白の場合は全テーブルを対象
```

### mybatis.properties の記載内容

```
url=データベース接続先のURL
username=ユーザ名
password=パスワード
```
