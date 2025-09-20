package com.export_table_definition.domain.service.writer.template;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.export_table_definition.domain.model.AllColumnEntity;
import com.export_table_definition.domain.model.AllConstraintEntity;
import com.export_table_definition.domain.model.AllForeignkeyEntity;
import com.export_table_definition.domain.model.AllIndexEntity;
import com.export_table_definition.domain.model.AllTableEntity;
import com.export_table_definition.domain.model.BaseInfoEntity;

/**
 * テーブル定義書き込みに利用するMarkdownのテンプレートを扱うクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class MarkdownTemplates {
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String LINE_SEPARATOR_DOUBLE = LINE_SEPARATOR + LINE_SEPARATOR;
    public static final String HORIZON = "___";;

    public static String header(String title) {
        return "# " + title + LINE_SEPARATOR_DOUBLE;
    }

    public static String baseInfo(BaseInfoEntity baseInfo) {
        return """
                ## 基本情報

                | RDBMS | データベース名 | 作成日 |
                |:---|:---|:---|
                """ + baseInfo.baseInfo() + LINE_SEPARATOR_DOUBLE;
    }

    public static String tableList() {
        return """
                ## テーブル情報

                | No. | スキーマ名 | 論理テーブル名 | 物理テーブル名 | 区分 | Link | 備考 |
                |:---|:---|:---|:---|:---|:---|:---|
                """;
    }

    public static String tableList(List<AllTableEntity> tables) {
        return tableList()
                + tables.stream().map(AllTableEntity::tableInfoList).collect(Collectors.joining(LINE_SEPARATOR))
                + LINE_SEPARATOR;
    }

    public static String tableExplanation() {
        return """
                ## テーブル説明

                """;
    }

    public static String subTableListLink(BaseInfoEntity baseInfo, int fileIndex) {
        return String.format("* [テーブル一覧_%s](./tableList_%s_%s.md)  ", fileIndex, baseInfo.dbName(), fileIndex)
                + LINE_SEPARATOR;
    }

    public static String tableInfo(AllTableEntity table) {
        return """
                ## テーブル情報

                | スキーマ名 | 論理テーブル名 | 物理テーブル名 | 区分 | 備考 |
                |:---|:---|:---|:---|:---|
                """ + table.tableInfo() + LINE_SEPARATOR_DOUBLE;
    }

    public static String columns(List<AllColumnEntity> columns, AllTableEntity table) {
        String header = """
                ## カラム情報

                | No. | 論理名 | 物理名 | データ型 | PK | Not Null | デフォルト | 備考 |
                |:---|:---|:---|:---|:---|:---|:---|:---|
                """;
        return tableSection(columns, table, header, AllColumnEntity::columnInfo, AllColumnEntity::getSchemaTableName);
    }

    public static String view(AllTableEntity table) {
        if (!table.isView()) {
            return "";
        }
        return """
                ## ソース

                ```sql
                """ + LINE_SEPARATOR + table.definition() + LINE_SEPARATOR + """

                ```

                """;
    }

    public static String indexes(List<AllIndexEntity> indexes, AllTableEntity table) {
        String header = """
                ## インデックス情報

                | No. | インデックス名 | カラムリスト |
                |:---|:---|:---|
                """;
        return tableSection(indexes, table, header, AllIndexEntity::indexInfo, AllIndexEntity::getSchemaTableName);
    }

    public static String constraints(List<AllConstraintEntity> constraints, AllTableEntity table) {
        String header = """
                ## 制約情報

                | No. | 制約名 | 種類 | 制約定義 |
                |:---|:---|:---|:---|
                """;
        return tableSection(constraints, table, header, AllConstraintEntity::constraintInfo,
                AllConstraintEntity::getSchemaTableName);
    }

    public static String foreignKeys(List<AllForeignkeyEntity> foreignkeys, AllTableEntity table) {
        String header = """
                ## 外部キー情報

                | No. | 外部キー名 | カラムリスト | 参照先 | 参照先カラムリスト |
                |:---|:---|:---|:---|:---|
                """;
        return tableSection(foreignkeys, table, header, AllForeignkeyEntity::foreignkeyInfo,
                AllForeignkeyEntity::getSchemaTableName);
    }

    public static String footer(BaseInfoEntity baseInfo) {
        return HORIZON + LINE_SEPARATOR_DOUBLE + String.format("[テーブル一覧へ](../../../tableList_%s.md)", baseInfo.dbName())
                + LINE_SEPARATOR;
    }

    public static String writeTableListFooter(BaseInfoEntity baseInfo, int maxTablesize, int tableCount,
            int fileIndex) {
        if (fileIndex != 1) {
            // 前のページが存在する場合
            return HORIZON + LINE_SEPARATOR_DOUBLE
                    + String.format("[<<前へ](./tableList_%s_%s.md) ", baseInfo.dbName(), fileIndex - 1);
        }
        if (tableCount < maxTablesize) {
            // 次のページが存在する場合
            return HORIZON + LINE_SEPARATOR_DOUBLE
                    + String.format("[次へ>>](./tableList_%s_%s.md) ", baseInfo.dbName(), fileIndex + 1);
        }
        return LINE_SEPARATOR_DOUBLE;
    }

    private static <T> String tableSection(List<T> list, AllTableEntity table, String header,
            Function<T, String> infoMapper, Function<T, String> schemaTableNameGetter) {
        return header + list.stream().filter(e -> schemaTableNameGetter.apply(e).equals(table.getSchemaTableName()))
                .map(infoMapper).collect(Collectors.joining(LINE_SEPARATOR)) + LINE_SEPARATOR_DOUBLE;
    }
}