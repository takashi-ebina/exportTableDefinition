package com.export_table_definition.application.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.export_table_definition.application.ExportTableDefinitionUsecase;
import com.export_table_definition.application.dto.TableDefinitionWriterCommand;
import com.export_table_definition.domain.model.BaseInfoEntity;
import com.export_table_definition.domain.model.ColumnEntity;
import com.export_table_definition.domain.model.ConstraintEntity;
import com.export_table_definition.domain.model.ForeignkeyEntity;
import com.export_table_definition.domain.model.IndexEntity;
import com.export_table_definition.domain.model.TableEntity;
import com.export_table_definition.domain.repository.TableDefinitionRepository;
import com.export_table_definition.domain.service.writer.TableDefinitionWriterDomainService;
import com.google.inject.Inject;

/**
 * テーブル定義出力に関するユースケースクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class ExportTableDefinitionUsecaseImpl implements ExportTableDefinitionUsecase {

    private static final String OUTPUT_BASE_DIRECTORY = "./output";
    private final TableDefinitionRepository tableDefinitionRepository;
    private final TableDefinitionWriterDomainService tableDefinitionWriter;

    /**
     * コンストラクタ
     * 
     * @param tableDefinitionRepository テーブル定義出力に関するリポジトリクラス
     * @param tableDefinitionWriter テーブル定義を書き込むクラス
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
        final BaseInfoEntity baseInfoEntity = tableDefinitionRepository.selectBaseInfo();
        final List<TableEntity> tableEntityList = tableDefinitionRepository.selectTableList(targetSchemaList, targetTableList);
        final List<ColumnEntity> columnEntityList = tableDefinitionRepository.selectColumnList(targetSchemaList,targetTableList);
        final List<IndexEntity> indexEntityList = tableDefinitionRepository.selectIndexList(targetSchemaList, targetTableList);
        final List<ConstraintEntity> constraintEntityList = tableDefinitionRepository.selectConstraintList(targetSchemaList, targetTableList);
        final List<ForeignkeyEntity> foreignkeyEntityList = tableDefinitionRepository.selectForeignkeyList(targetSchemaList, targetTableList);

        // テーブル一覧出力 -> ./output/ or {設定ファイルのFileParh}/tableList_{DB名}.md
        tableDefinitionWriter.writeTableDefinitionList(tableEntityList, baseInfoEntity, outputBaseDirectoryPath);
        // テーブル定義出力 -> ./output/ or {設定ファイルのFileParh}/{DB名}/{スキーマ名}/{TBL分類}/{物理テーブル名}.md
        tableEntityList.stream()
                .filter(tableEntity -> tableEntity.needsWriteTableDefinition(targetSchemaList, targetTableList))
                .map(tableEntity -> new TableDefinitionWriterCommand(baseInfoEntity, tableEntity, columnEntityList,
                        indexEntityList, constraintEntityList, foreignkeyEntityList, outputBaseDirectoryPath))
                .forEach(tableDefinitionWriter::writeTableDefinition);
    }
}
