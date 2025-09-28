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
    
    private final String tableListFilePrefix = "tableList";
    private final int maxTableListTableSize = 3000;
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
        if (tables.size() > maxTableListTableSize) {
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
            final Path filePath = outputDirectoryPath.resolve(makeTableListFileName(baseInfo, fileIndex));
            final List<String> contents = new ArrayList<>();
            // ヘッダー
            contents.add(TableDefinitionListTemplates.header(baseInfo));
            // 基本情報
            contents.add(TableDefinitionListTemplates.baseInfo(baseInfo));
            // テーブル一覧
            contents.add(TableDefinitionListTemplates.tableList());
            for (int i = 0; i < maxTableListTableSize && tableCount < maxTableSize; i++) {
                // Markdownの表に表示できる最大件数分、テーブルを出力する
                contents.add(tables.get(tableCount).tableInfoList() + TableDefinitionListTemplates.LINE_SEPARATOR);
                tableCount++;
            }
            contents.add(TableDefinitionListTemplates.LINE_SEPARATOR);
            // フッター
            contents.add(TableDefinitionListTemplates.writeTableListFooter(baseInfo, maxTableSize, tableCount, fileIndex));

            fileRepository.writeFile(filePath, contents);
            fileIndex++;
        }
    }

    private void writeSummaryFile(int totalFiles, BaseInfoEntity baseInfo, Path outputDirectoryPath) {
        final Path filePath = outputDirectoryPath.resolve(makeTableListFileName(baseInfo, 0));
        final List<String> contents = new ArrayList<>();
        // ヘッダー
        contents.add(TableDefinitionListTemplates.header(baseInfo));
        // 基本情報
        contents.add(TableDefinitionListTemplates.baseInfo(baseInfo));
        // 分割したテーブル一覧のリンク
        for (int i = 1; i <= totalFiles / maxTableListTableSize
                + (totalFiles % maxTableListTableSize > 0 ? 1 : 0); i++) {
            contents.add(TableDefinitionListTemplates.subTableListLink(baseInfo, i));
        }
        fileRepository.writeFile(filePath, contents);
    }
    
    private void writeSingleTableFile(List<TableEntity> tables, BaseInfoEntity baseInfo, Path outputDirectoryPath) {
        final Path filePath = outputDirectoryPath.resolve(makeTableListFileName(baseInfo, 0));
        final List<String> contents = new ArrayList<>();
        // ヘッダー
        contents.add(TableDefinitionListTemplates.header(baseInfo));
        // 基本情報
        contents.add(TableDefinitionListTemplates.baseInfo(baseInfo));
        // テーブル一覧
        contents.add(TableDefinitionListTemplates.tableList(tables));
        fileRepository.writeFile(filePath, contents);

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
     * @param outputDirectoryPath 出力ファイル
     */
    public void writeTableDefinition(TableEntity table, BaseInfoEntity baseInfo, List<ColumnEntity> columns,
            List<IndexEntity> indexes, List<ConstraintEntity> constraints, List<ForeignkeyEntity> foreignkeys,
            Path outputDirectoryPath) {
        fileRepository.createDirectory(outputDirectoryPath);
        final Path filePath = outputDirectoryPath.resolve(table.physicalTableName() + ".md");
        final List<String> contents = new ArrayList<>();
        // ヘッダー
        contents.add(TableDefinitionTemplates.header(table));
        // 基本情報
        contents.add(TableDefinitionTemplates.baseInfo(baseInfo));
        // テーブル説明
        contents.add(TableDefinitionTemplates.tableExplanation());
        // テーブル情報
        contents.add(TableDefinitionTemplates.tableInfo(table));
        // カラム情報
        contents.add(TableDefinitionTemplates.columns(columns, table));
        // View情報
        contents.add(TableDefinitionTemplates.view(table));
        // インデックス情報
        contents.add(TableDefinitionTemplates.indexes(indexes, table));
        // 制約情報
        contents.add(TableDefinitionTemplates.constraints(constraints, table));
        // 外部キー情報
        contents.add(TableDefinitionTemplates.foreignKeys(foreignkeys, table));
        // フッター
        contents.add(TableDefinitionTemplates.footer(baseInfo));
        fileRepository.writeFile(filePath, contents);
    }
}
