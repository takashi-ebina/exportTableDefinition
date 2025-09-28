package com.export_table_definition.application.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.export_table_definition.application.ExportTableDefinitionUsecase;
import com.export_table_definition.domain.model.BaseInfoEntity;
import com.export_table_definition.domain.model.ColumnEntity;
import com.export_table_definition.domain.model.ConstraintEntity;
import com.export_table_definition.domain.model.ForeignkeyEntity;
import com.export_table_definition.domain.model.IndexEntity;
import com.export_table_definition.domain.model.TableEntity;
import com.export_table_definition.domain.repository.TableDefinitionRepository;
import com.export_table_definition.domain.service.writer.TableDefinitionWriterDomainService;
import com.export_table_definition.infrastructure.log.Log4J2;
import com.google.inject.Inject;

/**
 * テーブル定義出力に関するユースケースクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class ExportTableDefinitionUsecaseImpl implements ExportTableDefinitionUsecase {

    private final String OUTPUT_BASE_DIRECTORY = "./output";
    private final static Log4J2 logger = Log4J2.getInstance();
    private final TableDefinitionRepository tableDefinitionRepository;
    private final TableDefinitionWriterDomainService tableDefinitionWriter;

    /**
     * コンストラクタ
     * 
     * @param repository テーブル定義出力に関するリポジトリクラス
     * @param writer テーブル定義を書き込むクラス
     */
    @Inject
    public ExportTableDefinitionUsecaseImpl(TableDefinitionRepository tableDefinitionRepository,
            TableDefinitionWriterDomainService tableDefinitionWriter) {
        this.tableDefinitionRepository = tableDefinitionRepository;
        this.tableDefinitionWriter = tableDefinitionWriter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exportTableDefinition(List<String> targetSchemaList, List<String> targetTableList, String outputPath) {
        // ベースディレクトリパス
        final String strOutputBaseDirectory = StringUtils.isBlank(outputPath) ? OUTPUT_BASE_DIRECTORY : outputPath;
        final Path outputBaseDirectoryPath =  Paths.get(strOutputBaseDirectory);
        
        // Entity取得
        final BaseInfoEntity baseEntity = tableDefinitionRepository.selectBaseInfo();
        final List<TableEntity> tableEntityList = tableDefinitionRepository.selectAllTableInfo(targetSchemaList, targetTableList);
        final List<ColumnEntity> columnEntityList = tableDefinitionRepository.selectAllColumnInfo(targetSchemaList,targetTableList);
        final List<IndexEntity> indexEntityList = tableDefinitionRepository.selectAllIndexInfo(targetSchemaList, targetTableList);
        final List<ConstraintEntity> constraintEntityList = tableDefinitionRepository.selectAllConstraintInfo(targetSchemaList, targetTableList);
        final List<ForeignkeyEntity> foreignkeyEntityList = tableDefinitionRepository.selectAllForeignkeyInfo(targetSchemaList, targetTableList);

        // テーブル一覧出力 -> ./output/tableList_{DB名}.md or {設定ファイルのFileParh}/tableList_{DB名}.md
        tableDefinitionWriter.writeTableDefinitionList(tableEntityList, baseEntity, outputBaseDirectoryPath);
        // テーブル定義出力
        tableEntityList.forEach(createTableExporter(targetSchemaList, targetTableList, outputBaseDirectoryPath,
                baseEntity, columnEntityList, indexEntityList, constraintEntityList, foreignkeyEntityList));
    }

    private Consumer<TableEntity> createTableExporter(List<String> targetSchemaList, List<String> targetTableList,
            Path outputBaseDirectoryPath, BaseInfoEntity baseEntity, List<ColumnEntity> columnEntityList,
            List<IndexEntity> indexEntityList, List<ConstraintEntity> constraintEntityList,
            List<ForeignkeyEntity> foreignkeyEntityList) {
        return tableEntity -> {
            if (!tableEntity.needsWriteTableDefinition(targetSchemaList, targetTableList)) {
                return;
            }
            // テーブル定義出力先ファイルパス
            // -> ./output/{DB名}/{スキーマ名}/{TBL分類}/{物理テーブル名}.md
            // or {設定ファイルのFileParh}/{DB名}/{スキーマ名}/{TBL分類}/{物理テーブル名}.md
            tableDefinitionWriter.writeTableDefinition(tableEntity, baseEntity, columnEntityList, indexEntityList,
                    constraintEntityList, foreignkeyEntityList, outputBaseDirectoryPath);

            logger.logDebug(String.format("exportTableDefinition complete. [filePath=%s]",
                    tableEntity.toTableDefinitionFile(outputBaseDirectoryPath).toString()));
        };
    }

}
