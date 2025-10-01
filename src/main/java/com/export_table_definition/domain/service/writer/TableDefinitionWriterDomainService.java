package com.export_table_definition.domain.service.writer;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.export_table_definition.domain.model.TableDefinitionContent;
import com.export_table_definition.domain.model.entity.BaseInfoEntity;
import com.export_table_definition.domain.model.entity.TableEntity;
import com.export_table_definition.domain.repository.FileRepository;
import com.export_table_definition.domain.service.writer.template.TableDefinitionListTemplates;
import com.export_table_definition.domain.service.writer.template.TableDefinitionTemplates;
import com.export_table_definition.infrastructure.log.Log4J2;
import com.google.inject.Inject;

/**
 * テーブル定義を書き込むクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class TableDefinitionWriterDomainService {
    
    private static final int MAX_TABLE_LIST_SIZE = 3000;
    private static final Log4J2 logger = Log4J2.getInstance();
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
     * @param outputDirectoryPath 出力ディレクトリのパス
     */
    public void writeTableDefinitionList(List<TableEntity> tables, BaseInfoEntity baseInfo, Path outputDirectoryPath) {
        fileRepository.createDirectory(outputDirectoryPath);
        if (tables.size() > MAX_TABLE_LIST_SIZE) {
            // テーブル一覧の件数がMarkdownの表に表示できる最大件数を超える場合、テーブル一覧を分割して出力する
            writeMultipleTableFiles(tables, baseInfo, outputDirectoryPath);
            writeSummaryFile(tables.size(), baseInfo, outputDirectoryPath);
        } else {
            writeSingleTableFile(tables, baseInfo, outputDirectoryPath);
        }
    }
    
    /**
     * テーブル一覧を分割して出力するメソッド（3000テーブル毎）
     * 
     * @param tables テーブル情報リスト
     * @param baseInfo データベースの基本情報
     * @param outputDirectoryPath 出力ディレクトリのパス
     */
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

    /**
     * テーブル一覧のサマリーファイルを書き込むメソッド<br>
     * 分割したテーブル一覧のファイルへのリンクを記載する
     * 
     * @param totalFiles 分割したテーブル一覧ファイルの総数
     * @param baseInfo データベースの基本情報
     * @param outputDirectoryPath 出力ディレクトリのパス
     */
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

    /**
     * テーブル一覧を1ファイルにまとめて出力するメソッド
     * 
     * @param tables テーブル情報リスト
     * @param baseInfo データベースの基本情報
     * @param outputDirectoryPath 出力ディレクトリのパス
     */
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
     * @param content テーブル定義出力に必要な情報をまとめたレコード
     */
    public void writeTableDefinition(TableDefinitionContent content) {
        final Path directoryPath = content.table().toTableDefinitionDirectory(content.outputBaseDir());
        final Path filePath = content.table().toTableDefinitionFile(directoryPath);
        final List<String> contents = List.of(
                TableDefinitionTemplates.fileHeader(content.table()),       // ヘッダー
                TableDefinitionTemplates.baseInfo(content.baseInfo()),      // 基本情報
                TableDefinitionTemplates.tableExplanation(),                // テーブル説明
                TableDefinitionTemplates.tableInfo(content.table()),        // テーブル情報
                TableDefinitionTemplates.columns(content.columns(), content.table()), // カラム情報
                TableDefinitionTemplates.view(content.table()),             // View情報
                TableDefinitionTemplates.indexes(content.indexes(), content.table()), // インデックス情報
                TableDefinitionTemplates.constraints(content.constraints(), content.table()), // 制約情報
                TableDefinitionTemplates.foreignKeys(content.foreignKeys(), content.table()), // 外部キー情報
                TableDefinitionTemplates.footer(content.baseInfo())         // フッター
            );
        fileRepository.createDirectory(directoryPath);
        fileRepository.writeFile(filePath, contents);
        logger.logDebug(String.format("exportTableDefinition complete. [filePath=%s]", filePath.toString()));
    }
}
