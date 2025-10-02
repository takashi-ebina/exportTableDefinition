package com.export_table_definition.application.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.export_table_definition.application.ExportTableDefinitionUsecase;
import com.export_table_definition.domain.model.TableDefinitionContent;
import com.export_table_definition.domain.model.collection.Columns;
import com.export_table_definition.domain.model.collection.Constraints;
import com.export_table_definition.domain.model.collection.ForeignKeys;
import com.export_table_definition.domain.model.collection.Indexes;
import com.export_table_definition.domain.model.entity.BaseInfoEntity;
import com.export_table_definition.domain.model.entity.TableEntity;
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
        final Path outputBaseDir = Optional.ofNullable(outputPath)
                .filter(StringUtils::isNotBlank)
                .map(Paths::get)
                .orElse(Paths.get(OUTPUT_BASE_DIRECTORY));
        
        // Entity取得
        final BaseInfoEntity baseInfoEntity = tableDefinitionRepository.selectBaseInfo();
        final List<TableEntity> tableEntityList = tableDefinitionRepository.selectTableList(targetSchemaList, targetTableList);
        final Columns columns = Columns.of(tableDefinitionRepository.selectColumnList(targetSchemaList,targetTableList));
        final Indexes indexes = Indexes.of(tableDefinitionRepository.selectIndexList(targetSchemaList, targetTableList));
        final Constraints constraints = Constraints.of(tableDefinitionRepository.selectConstraintList(targetSchemaList, targetTableList));
        final ForeignKeys foreignKeys = ForeignKeys.of(tableDefinitionRepository.selectForeignKeyList(targetSchemaList, targetTableList));

        // テーブル一覧出力 -> ./output/ or {設定ファイルのFileParh}/tableList_{DB名}.md
        tableDefinitionWriter.writeTableDefinitionList(tableEntityList, baseInfoEntity, outputBaseDir);
        // テーブル定義出力 -> ./output/ or {設定ファイルのFileParh}/{DB名}/{スキーマ名}/{TBL分類}/{物理テーブル名}.md
        tableEntityList.stream()
                .filter(tableEntity -> tableEntity.needsWriteTableDefinition(targetSchemaList, targetTableList))
                .map(tableEntity -> TableDefinitionContent.assemble(
                        baseInfoEntity, tableEntity, columns, indexes, constraints, foreignKeys, outputBaseDir))
                .forEach(tableDefinitionWriter::writeTableDefinition);
    }
}
