package com.export_table_definition.domain.service.writer.template;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.export_table_definition.domain.model.BaseInfoEntity;
import com.export_table_definition.domain.model.ColumnEntity;
import com.export_table_definition.domain.model.ConstraintEntity;
import com.export_table_definition.domain.model.ForeignkeyEntity;
import com.export_table_definition.domain.model.IndexEntity;
import com.export_table_definition.domain.model.TableEntity;

/**
 * テーブル定義書き込みに利用するMarkdownのテンプレートを扱うクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class TableDefinitionTemplates {
    public static final String LINE_SEPARATOR = System.lineSeparator();
    public static final String LINE_SEPARATOR_DOUBLE = LINE_SEPARATOR + LINE_SEPARATOR;
    public static final String HORIZON = "___";

    /**
     * テーブル定義ヘッダー
     * 
     * @param table テーブル情報
     * @return ヘッダー文字列
     */
    public static String header(TableEntity table) {
        return "# " + table.getHeaderTableName() + LINE_SEPARATOR_DOUBLE;
    }

    /**
     * 基本情報セクション
     * 
     * @param baseInfo データベース基本情報
     * @return 基本情報セクション文字列
     */
    public static String baseInfo(BaseInfoEntity baseInfo) {
        return """
                ## 基本情報

                | RDBMS | データベース名 | 作成日 |
                |:---|:---|:---|
                """ + baseInfo.baseInfo() + LINE_SEPARATOR_DOUBLE;
    }

    /**
     * テーブル説明セクション
     * 
     * @return テーブル説明セクション文字列
     */
    public static String tableExplanation() {
        return """
                ## テーブル説明

                """;
    }

    /**
     * テーブル情報セクション
     * 
     * @param table テーブル情報
     * @return テーブル情報セクション文字列
     */
    public static String tableInfo(TableEntity table) {
        return """
                ## テーブル情報

                | スキーマ名 | 論理テーブル名 | 物理テーブル名 | 区分 | 備考 |
                |:---|:---|:---|:---|:---|
                """ + table.tableInfo() + LINE_SEPARATOR_DOUBLE;
    }

    /**
     * カラム情報セクション
     * 
     * @param columns カラム情報のリスト
     * @param table テーブル情報
     * @return カラム情報セクション文字列
     */
    public static String columns(List<ColumnEntity> columns, TableEntity table) {
        String header = """
                ## カラム情報

                | No. | 論理名 | 物理名 | データ型 | PK | Not Null | デフォルト | 備考 |
                |:---|:---|:---|:---|:---|:---|:---|:---|
                """;
        return tableSection(columns, table, header, ColumnEntity::columnInfo, ColumnEntity::getSchemaTableName);
    }

    /**
     * ビュー情報セクション
     * 
     * @param table テーブル情報
     * @return ビュー情報セクション文字列
     */
    public static String view(TableEntity table) {
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

    /**
     * インデックス情報セクション
     * 
     * @param indexes インデックス情報のリスト
     * @param table テーブル情報
     * @return インデックス情報セクション文字列
     */
    public static String indexes(List<IndexEntity> indexes, TableEntity table) {
        String header = """
                ## インデックス情報

                | No. | インデックス名 | カラムリスト |
                |:---|:---|:---|
                """;
        return tableSection(indexes, table, header, IndexEntity::indexInfo, IndexEntity::getSchemaTableName);
    }

    /**
     * 制約情報セクション
     * 
     * @param constraints 制約情報のリスト
     * @param table テーブル情報
     * @return 制約情報セクション文字列
     */
    public static String constraints(List<ConstraintEntity> constraints, TableEntity table) {
        String header = """
                ## 制約情報

                | No. | 制約名 | 種類 | 制約定義 |
                |:---|:---|:---|:---|
                """;
        return tableSection(constraints, table, header, ConstraintEntity::constraintInfo,
                ConstraintEntity::getSchemaTableName);
    }

    /**
     * 外部キー情報セクション
     * 
     * @param foreignkeys 外部キー情報のリスト
     * @param table テーブル情報
     * @return 外部キー情報セクション文字列
     */
    public static String foreignKeys(List<ForeignkeyEntity> foreignkeys, TableEntity table) {
        String header = """
                ## 外部キー情報

                | No. | 外部キー名 | カラムリスト | 参照先 | 参照先カラムリスト |
                |:---|:---|:---|:---|:---|
                """;
        return tableSection(foreignkeys, table, header, ForeignkeyEntity::foreignkeyInfo,
                ForeignkeyEntity::getSchemaTableName);
    }

    /**
     * フッター
     * 
     * @param baseInfo データベース基本情報
     * @return フッター文字列
     */
    public static String footer(BaseInfoEntity baseInfo) {
        return HORIZON + LINE_SEPARATOR_DOUBLE + String.format("[テーブル一覧へ](../../../tableList_%s.md)", baseInfo.dbName())
                + LINE_SEPARATOR;
    }

    private static <T> String tableSection(List<T> list, TableEntity table, String header,
            Function<T, String> infoMapper, Function<T, String> schemaTableNameGetter) {
        return header + list.stream().filter(e -> schemaTableNameGetter.apply(e).equals(table.getSchemaTableName()))
                .map(infoMapper).collect(Collectors.joining(LINE_SEPARATOR)) + LINE_SEPARATOR_DOUBLE;
    }
}
