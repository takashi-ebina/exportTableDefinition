package com.export_table_definition.domain.service.writer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.export_table_definition.domain.model.BaseInfoEntity;
import com.export_table_definition.domain.model.ColumnEntity;
import com.export_table_definition.domain.model.ConstraintEntity;
import com.export_table_definition.domain.model.ForeignkeyEntity;
import com.export_table_definition.domain.model.IndexEntity;
import com.export_table_definition.domain.model.TableEntity;
import com.export_table_definition.domain.repository.FileRepository;
import com.export_table_definition.domain.service.writer.template.TableDefinitionListTemplates;
import com.export_table_definition.domain.service.writer.template.TableDefinitionTemplates;
import com.google.inject.Inject;

/**
 * テーブル定義を書き込むクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class TableDefinitionWriterDomainService {
    
    private final int MAX_TABLE_LIST_SIZE = 3000;
    private final FileRepository fileRepository;
    
    /**
     * コンストラクタ
     * 
     * @param fileRepository ファイルリポジトリ
     */
    @Inject
    public TableDefinitionWriterDomainService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
    
    /**
     * テーブル一覧の書き込み処理を行うメソッド
     * 
     * @param tables テーブル情報リスト
     * @param baseInfo データベースの基本情報
     * @param outputDirectoryPath 出力ファイル
     */
    public void writeTableDefinitionList(List<TableEntity> tables, BaseInfoEntity baseInfo, Path outputDirectoryPath) {
        fileRepository.createDirectory(outputDirectoryPath);
         // テーブル一覧の件数がMarkdownの表に表示できる最大件数を超える場合、テーブル一覧を分割して出力する
        if (tables.size() > MAX_TABLE_LIST_SIZE) {
            // Markdownの表に表示できる最大件数を超える場合、テーブル一覧を分割して出力する
            writeMultipleTableFiles(tables, baseInfo, outputDirectoryPath);
            writeSummaryFile(tables.size(), baseInfo, outputDirectoryPath);
        } else {
            writeSingleTableFile(tables, baseInfo, outputDirectoryPath);
        }
    }
    
    private void writeMultipleTableFiles(List<TableEntity> tables, BaseInfoEntity baseInfo, Path outputDirectoryPath) {
        final int maxTableSize = tables.size();
        int tableCount = 0;
        int fileIndex = 1;
        while (tableCount < maxTableSize) {
            // Markdownの表に表示できる最大件数毎に、テーブル一覧を分割して出力する
            final List<String> contents = new ArrayList<>();
            // ヘッダー
            contents.add(TableDefinitionListTemplates.fileHeader(baseInfo));
            // 基本情報
            contents.add(TableDefinitionListTemplates.baseInfo(baseInfo));
            // テーブル一覧
            contents.add(TableDefinitionListTemplates.tableListTableHeader());
            for (int i = 0; i < MAX_TABLE_LIST_SIZE && tableCount < maxTableSize; i++) {
                // Markdownの表に表示できる最大件数分、テーブルを出力する
                contents.add(TableDefinitionListTemplates.tableListLine(tables.get(tableCount)));
                tableCount++;
            }
            contents.add(TableDefinitionListTemplates.lineSeparator());
            // フッター
            contents.add(TableDefinitionListTemplates.writeTableListFooter(baseInfo, maxTableSize, tableCount, fileIndex));

            fileRepository.writeFile(baseInfo.toTableListFile(outputDirectoryPath, fileIndex), contents);
            fileIndex++;
        }
    }

    private void writeSummaryFile(int totalFiles, BaseInfoEntity baseInfo, Path outputDirectoryPath) {
        final List<String> contents = new ArrayList<>();
        // ヘッダー
        contents.add(TableDefinitionListTemplates.fileHeader(baseInfo));
        // 基本情報
        contents.add(TableDefinitionListTemplates.baseInfo(baseInfo));
        // 分割したテーブル一覧のリンク
        for (int i = 1; i <= totalFiles / MAX_TABLE_LIST_SIZE
                + (totalFiles % MAX_TABLE_LIST_SIZE > 0 ? 1 : 0); i++) {
            contents.add(TableDefinitionListTemplates.subTableListLink(baseInfo, i));
        }
        fileRepository.writeFile(baseInfo.toTableListFile(outputDirectoryPath), contents);
    }
    
    private void writeSingleTableFile(List<TableEntity> tables, BaseInfoEntity baseInfo, Path outputDirectoryPath) {
        final List<String> contents = List.of(
                TableDefinitionListTemplates.fileHeader(baseInfo), // ヘッダー
                TableDefinitionListTemplates.baseInfo(baseInfo),   // 基本情報
                TableDefinitionListTemplates.tableList(tables)     // テーブル一覧
            );   
        fileRepository.writeFile(baseInfo.toTableListFile(outputDirectoryPath), contents);
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
     * @param outputBaseDirectoryPath 出力ファイル
     */
    public void writeTableDefinition(TableEntity table, BaseInfoEntity baseInfo, List<ColumnEntity> columns,
            List<IndexEntity> indexes, List<ConstraintEntity> constraints, List<ForeignkeyEntity> foreignkeys,
            Path outputBaseDirectoryPath) {
        final Path directoryPath = table.toTableDefinitionDirectory(outputBaseDirectoryPath, baseInfo.dbName());
        final Path filePath = table.toTableDefinitionFile(directoryPath, baseInfo.dbName());
        final List<String> contents = List.of(
                TableDefinitionTemplates.fileHeader(table), // ヘッダー
                TableDefinitionTemplates.baseInfo(baseInfo), // 基本情報
                TableDefinitionTemplates.tableExplanation(), // テーブル説明
                TableDefinitionTemplates.tableInfo(table), // テーブル情報
                TableDefinitionTemplates.columns(columns, table), // カラム情報
                TableDefinitionTemplates.view(table), // View情報
                TableDefinitionTemplates.indexes(indexes, table), // インデックス情報
                TableDefinitionTemplates.constraints(constraints, table), // 制約情報
                TableDefinitionTemplates.foreignKeys(foreignkeys, table), // 外部キー情報
                TableDefinitionTemplates.footer(baseInfo) // フッター
            );
        fileRepository.createDirectory(directoryPath);
        fileRepository.writeFile(filePath, contents);
    }
}
