package com.export_table_definition.domain.service.writer.template;

import java.util.List;
import java.util.stream.Collectors;

import com.export_table_definition.domain.model.BaseInfoEntity;
import com.export_table_definition.domain.model.TableEntity;

/**
 * テーブル定義一覧書き込みに利用するMarkdownのテンプレートを扱うクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class TableDefinitionListTemplates {
    private static final String LINE_SEPARATOR = System.lineSeparator();
    private static final String LINE_SEPARATOR_DOUBLE = LINE_SEPARATOR + LINE_SEPARATOR;
    private static final String HORIZON = "___";

    /**
     * テーブル定義一覧ヘッダー
     * 
     * @param baseInfo データベース基本情報
     * @return ヘッダー文字列
     */
    public static String header(BaseInfoEntity baseInfo) {
        return "# " + String.format("テーブル一覧（DB名：%s）", baseInfo.dbName()) + LINE_SEPARATOR_DOUBLE;
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
    public static String tableList(List<TableEntity> tables) {
        return tableList()
                + tables.stream().map(TableEntity::tableInfoList).collect(Collectors.joining(LINE_SEPARATOR))
                + LINE_SEPARATOR;
    }
    
    /**
     * テーブル一覧セクション（テーブル情報1行分）
     * 
     * @param table テーブル情報
     * @return テーブル一覧セクション文字列
     */
    public static String tableInfoListLine(TableEntity table) {
        return table.tableInfoList() + LINE_SEPARATOR;
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
    
    /**
     * 改行コードを取得するメソッド
     * 
     * @return 改行コード
     */
    public static String lineSeparator() {
        return LINE_SEPARATOR;
    }
}