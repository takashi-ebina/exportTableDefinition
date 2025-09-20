package com.export_table_definition.domain.service.writer;

import java.nio.file.Path;
import java.util.List;

import com.export_table_definition.domain.model.AllColumnEntity;
import com.export_table_definition.domain.model.AllConstraintEntity;
import com.export_table_definition.domain.model.AllForeignkeyEntity;
import com.export_table_definition.domain.model.AllIndexEntity;
import com.export_table_definition.domain.model.AllTableEntity;
import com.export_table_definition.domain.model.BaseInfoEntity;
import com.export_table_definition.domain.service.writer.template.MarkdownTemplates;

/**
 * テーブル定義を書き込むクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class TableDefinitionWriterDomainService {
    
    private final String tableListFilePrefix = "tableList";
    private final int maxTableListTableSize = 3000;
    
    /**
     * テーブル一覧の書き込み処理を行うメソッド
     * 
     * @param tables テーブル情報リスト
     * @param baseInfo データベースの基本情報
     * @param outputFilePath 出力ファイル
     */
    public void writeTableDefinitionList(List<AllTableEntity> tables, BaseInfoEntity baseInfo, Path outputFilePath) {
        if (tables.size() > maxTableListTableSize) {
            // Markdownの表に表示できる最大件数を超える場合、テーブル一覧を分割して出力する
            writeMultipleTableFiles(tables, baseInfo, outputFilePath);
            writeSummaryFile(tables.size(), baseInfo, outputFilePath);
        } else {
            writeSingleTableFile(tables, baseInfo, outputFilePath);
        }
    }
    
    private void writeMultipleTableFiles(List<AllTableEntity> tables, BaseInfoEntity baseInfo, Path outputFilePath) {
        final int maxTableSize = tables.size();
        int tableCount = 0;
        int fileIndex = 1;
        final Path filePath = outputFilePath.resolve(makeTableListFileName(baseInfo, fileIndex));
        while (tableCount < maxTableSize) {
            // Markdownの表に表示できる最大件数毎に、テーブル一覧を分割して出力する
            try (final var bw = new TableDefinitionBufferedWriter(filePath)) {
                // ヘッダー
                bw.write(MarkdownTemplates.header("テーブル一覧" + "（DB名：" + baseInfo.dbName() + "）"));
                // 基本情報
                bw.write(MarkdownTemplates.baseInfo(baseInfo));
                // テーブル一覧
                bw.write(MarkdownTemplates.tableList());
                for (int i = 0; i < maxTableListTableSize && tableCount < maxTableSize; i++) {
                    // Markdownの表に表示できる最大件数分、テーブルを出力する
                    bw.write(tables.get(tableCount).tableInfoList() + MarkdownTemplates.LINE_SEPARATOR);
                    tableCount++;
                }
                bw.write(MarkdownTemplates.LINE_SEPARATOR);
                // フッター
                bw.write(MarkdownTemplates.writeTableListFooter(baseInfo, maxTableSize, tableCount, fileIndex));
            }
            fileIndex++;
        }
    }

    private void writeSummaryFile(int totalFiles, BaseInfoEntity baseInfo, Path outputFilePath) {
        final Path filePath = outputFilePath.resolve(makeTableListFileName(baseInfo, 0));
        try (final var bw = new TableDefinitionBufferedWriter(filePath)) {
            // ヘッダー
            bw.write(MarkdownTemplates.header("テーブル一覧" + "（DB名：" + baseInfo.dbName() + "）"));
            // 基本情報
            bw.write(MarkdownTemplates.baseInfo(baseInfo));
            // 分割したテーブル一覧のリンク
            for (int i = 1; i <= totalFiles / maxTableListTableSize + (totalFiles % maxTableListTableSize > 0 ? 1 : 0); i++) {
                bw.write(MarkdownTemplates.subTableListLink(baseInfo, i));
            }
        }
    }
    
    private void writeSingleTableFile(List<AllTableEntity> tables, BaseInfoEntity baseInfo, Path outputFilePath) {
        final Path filePath = outputFilePath.resolve(makeTableListFileName(baseInfo, 0));
        try (final var bw = new TableDefinitionBufferedWriter(filePath)) {
            // ヘッダー
            bw.write(MarkdownTemplates.header("テーブル一覧" + "（DB名：" + baseInfo.dbName() + "）"));
            // 基本情報
            bw.write(MarkdownTemplates.baseInfo(baseInfo));
            // テーブル一覧
            bw.write(MarkdownTemplates.tableList(tables));
        }
    }

    private String makeTableListFileName(BaseInfoEntity baseInfo, int fileIndex) {
        return tableListFilePrefix + "_" + baseInfo.dbName() + (fileIndex > 0 ? "_" + fileIndex : "") + ".md";
    }
    
    /**
     * テーブル定義の書き込み処理を行うメソッド
     * 
     * @param table テーブル情報
     * @param baseInfo データベースの基本情報
     * @param columns カラム情報
     * @param indexes インデックス情報
     * @param constraints 制約情報
     * @param foreignkeys 外部キー情報
     * @param outputFile 出力ファイル
     */
    public void writeTableDefinition(AllTableEntity table, BaseInfoEntity baseInfo, List<AllColumnEntity> columns,
            List<AllIndexEntity> indexes, List<AllConstraintEntity> constraints, List<AllForeignkeyEntity> foreignkeys,
            Path outputFilePath) {
        try (final var bw = new TableDefinitionBufferedWriter(outputFilePath)) {
            // ヘッダー
            bw.write(MarkdownTemplates.header(table.getHeaderTableName()));
            // 基本情報
            bw.write(MarkdownTemplates.baseInfo(baseInfo));
            // テーブル説明
            bw.write(MarkdownTemplates.tableExplanation());
            // テーブル情報
            bw.write(MarkdownTemplates.tableInfo(table));
            // カラム情報
            bw.write(MarkdownTemplates.columns(columns, table));
            // View情報
            bw.write(MarkdownTemplates.view(table));
            // インデックス情報
            bw.write(MarkdownTemplates.indexes(indexes, table));
            // 制約情報
            bw.write(MarkdownTemplates.constraints(constraints, table));
            // 外部キー情報
            bw.write(MarkdownTemplates.foreignKeys(foreignkeys, table));
            // フッター
            bw.write(MarkdownTemplates.footer(baseInfo));
        } 
    }
}
