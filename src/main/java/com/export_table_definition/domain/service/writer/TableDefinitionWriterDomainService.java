package com.export_table_definition.domain.service.writer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.export_table_definition.domain.model.AllColumnEntity;
import com.export_table_definition.domain.model.AllConstraintEntity;
import com.export_table_definition.domain.model.AllForeignkeyEntity;
import com.export_table_definition.domain.model.AllIndexEntity;
import com.export_table_definition.domain.model.AllTableEntity;
import com.export_table_definition.domain.model.BaseInfoEntity;
import com.export_table_definition.domain.repository.FileRepository;
import com.export_table_definition.domain.service.writer.template.MarkdownTemplates;
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
    public void writeTableDefinitionList(List<AllTableEntity> tables, BaseInfoEntity baseInfo, Path outputDirectoryPath) {
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
    
    private void writeMultipleTableFiles(List<AllTableEntity> tables, BaseInfoEntity baseInfo, Path outputDirectoryPath) {
        final int maxTableSize = tables.size();
        int tableCount = 0;
        int fileIndex = 1;
        while (tableCount < maxTableSize) {
            // Markdownの表に表示できる最大件数毎に、テーブル一覧を分割して出力する
            final Path filePath = outputDirectoryPath.resolve(makeTableListFileName(baseInfo, fileIndex));
            final List<String> contents = new ArrayList<>();
            // ヘッダー
            contents.add(MarkdownTemplates.header(String.format("テーブル一覧（DB名：%s）", baseInfo.dbName())));
            // 基本情報
            contents.add(MarkdownTemplates.baseInfo(baseInfo));
            // テーブル一覧
            contents.add(MarkdownTemplates.tableList());
            for (int i = 0; i < maxTableListTableSize && tableCount < maxTableSize; i++) {
                // Markdownの表に表示できる最大件数分、テーブルを出力する
                contents.add(tables.get(tableCount).tableInfoList() + MarkdownTemplates.LINE_SEPARATOR);
                tableCount++;
            }
            contents.add(MarkdownTemplates.LINE_SEPARATOR);
            // フッター
            contents.add(MarkdownTemplates.writeTableListFooter(baseInfo, maxTableSize, tableCount, fileIndex));

            fileRepository.writeFile(filePath, contents);
            fileIndex++;
        }
    }

    private void writeSummaryFile(int totalFiles, BaseInfoEntity baseInfo, Path outputDirectoryPath) {
        final Path filePath = outputDirectoryPath.resolve(makeTableListFileName(baseInfo, 0));
        final List<String> contents = new ArrayList<>();
        // ヘッダー
        contents.add(MarkdownTemplates.header(String.format("テーブル一覧（DB名：%s）", baseInfo.dbName())));
        // 基本情報
        contents.add(MarkdownTemplates.baseInfo(baseInfo));
        // 分割したテーブル一覧のリンク
        for (int i = 1; i <= totalFiles / maxTableListTableSize
                + (totalFiles % maxTableListTableSize > 0 ? 1 : 0); i++) {
            contents.add(MarkdownTemplates.subTableListLink(baseInfo, i));
        }
        fileRepository.writeFile(filePath, contents);
    }
    
    private void writeSingleTableFile(List<AllTableEntity> tables, BaseInfoEntity baseInfo, Path outputDirectoryPath) {
        final Path filePath = outputDirectoryPath.resolve(makeTableListFileName(baseInfo, 0));
        final List<String> contents = new ArrayList<>();
        // ヘッダー
        contents.add(MarkdownTemplates.header(String.format("テーブル一覧（DB名：%s）", baseInfo.dbName())));
        // 基本情報
        contents.add(MarkdownTemplates.baseInfo(baseInfo));
        // テーブル一覧
        contents.add(MarkdownTemplates.tableList(tables));
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
    public void writeTableDefinition(AllTableEntity table, BaseInfoEntity baseInfo, List<AllColumnEntity> columns,
            List<AllIndexEntity> indexes, List<AllConstraintEntity> constraints, List<AllForeignkeyEntity> foreignkeys,
            Path outputDirectoryPath) {
        fileRepository.createDirectory(outputDirectoryPath);
        final Path filePath = outputDirectoryPath.resolve(table.physicalTableName() + ".md");
        final List<String> contents = new ArrayList<>();
        // ヘッダー
        contents.add(MarkdownTemplates.header(table.getHeaderTableName()));
        // 基本情報
        contents.add(MarkdownTemplates.baseInfo(baseInfo));
        // テーブル説明
        contents.add(MarkdownTemplates.tableExplanation());
        // テーブル情報
        contents.add(MarkdownTemplates.tableInfo(table));
        // カラム情報
        contents.add(MarkdownTemplates.columns(columns, table));
        // View情報
        contents.add(MarkdownTemplates.view(table));
        // インデックス情報
        contents.add(MarkdownTemplates.indexes(indexes, table));
        // 制約情報
        contents.add(MarkdownTemplates.constraints(constraints, table));
        // 外部キー情報
        contents.add(MarkdownTemplates.foreignKeys(foreignkeys, table));
        // フッター
        contents.add(MarkdownTemplates.footer(baseInfo));
        fileRepository.writeFile(filePath, contents);
    }
}
