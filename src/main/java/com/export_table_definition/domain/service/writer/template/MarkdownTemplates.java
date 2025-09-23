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

    /**
     * ヘッダー
     * 
     * @param title s
     * @return ヘッダー文字列
     */
    public static String header(String title) {
        return "# " + title + LINE_SEPARATOR_DOUBLE;
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
     * テーブル一覧セクション
     * 
     * @return テーブル一覧セクション文字列
     */
    public static String tableList() {
        return """
                ## テーブル情報

                | No. | スキーマ名 | 論理テーブル名 | 物理テーブル名 | 区分 | Link | 備考 |
                |:---|:---|:---|:---|:---|:---|:---|
                """;
    }

    /**
     * テーブル一覧セクション（テーブル情報付き）
     * 
     * @param tables テーブル情報のリスト
     * @return テーブル一覧セクション文字列
     */
    public static String tableList(List<AllTableEntity> tables) {
        return tableList()
                + tables.stream().map(AllTableEntity::tableInfoList).collect(Collectors.joining(LINE_SEPARATOR))
                + LINE_SEPARATOR;
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
     * サブテーブル一覧リンクセクション
     * 
     * @param baseInfo データベース基本情報
     * @param fileIndex ファイルインデックス
     * @return サブテーブル一覧リンクセクション文字列
     */
    public static String subTableListLink(BaseInfoEntity baseInfo, int fileIndex) {
        return String.format("* [テーブル一覧_%s](./tableList_%s_%s.md)  ", fileIndex, baseInfo.dbName(), fileIndex)
                + LINE_SEPARATOR;
    }

    /**
     * テーブル情報セクション
     * 
     * @param table テーブル情報
     * @return テーブル情報セクション文字列
     */
    public static String tableInfo(AllTableEntity table) {
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
    public static String columns(List<AllColumnEntity> columns, AllTableEntity table) {
        String header = """
                ## カラム情報

                | No. | 論理名 | 物理名 | データ型 | PK | Not Null | デフォルト | 備考 |
                |:---|:---|:---|:---|:---|:---|:---|:---|
                """;
        return tableSection(columns, table, header, AllColumnEntity::columnInfo, AllColumnEntity::getSchemaTableName);
    }

    /**
     * ビュー情報セクション
     * 
     * @param table テーブル情報
     * @return ビュー情報セクション文字列
     */
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

    /**
     * インデックス情報セクション
     * 
     * @param indexes インデックス情報のリスト
     * @param table テーブル情報
     * @return インデックス情報セクション文字列
     */
    public static String indexes(List<AllIndexEntity> indexes, AllTableEntity table) {
        String header = """
                ## インデックス情報

                | No. | インデックス名 | カラムリスト |
                |:---|:---|:---|
                """;
        return tableSection(indexes, table, header, AllIndexEntity::indexInfo, AllIndexEntity::getSchemaTableName);
    }

    /**
     * 制約情報セクション
     * 
     * @param constraints 制約情報のリスト
     * @param table テーブル情報
     * @return 制約情報セクション文字列
     */
    public static String constraints(List<AllConstraintEntity> constraints, AllTableEntity table) {
        String header = """
                ## 制約情報

                | No. | 制約名 | 種類 | 制約定義 |
                |:---|:---|:---|:---|
                """;
        return tableSection(constraints, table, header, AllConstraintEntity::constraintInfo,
                AllConstraintEntity::getSchemaTableName);
    }

    /**
     * 外部キー情報セクション
     * 
     * @param foreignkeys 外部キー情報のリスト
     * @param table テーブル情報
     * @return 外部キー情報セクション文字列
     */
    public static String foreignKeys(List<AllForeignkeyEntity> foreignkeys, AllTableEntity table) {
        String header = """
                ## 外部キー情報

                | No. | 外部キー名 | カラムリスト | 参照先 | 参照先カラムリスト |
                |:---|:---|:---|:---|:---|
                """;
        return tableSection(foreignkeys, table, header, AllForeignkeyEntity::foreignkeyInfo,
                AllForeignkeyEntity::getSchemaTableName);
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

    /**
     * テーブル一覧フッター（ページング用）
     * 
     * @param baseInfo データベース基本情報
     * @param maxTablesize 1ページあたりの最大テーブル数
     * @param tableCount テーブル数
     * @param fileIndex ファイルインデックス
     * @return テーブル一覧フッター文字列
     */
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