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
    private final TableDefinitionRepository repository;
    private final TableDefinitionWriterDomainService writer;

    /**
     * コンストラクタ
     * 
     * @param repository テーブル定義出力に関するリポジトリクラス
     * @param writer     テーブル定義を書き込むクラス
     */
    @Inject
    public ExportTableDefinitionUsecaseImpl(TableDefinitionRepository repository,
            TableDefinitionWriterDomainService writer) {
        this.repository = repository;
        this.writer = writer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void exportTableDefinition(List<String> targetSchemaList, List<String> targetTableList, String outputPath) {
        // ベースディレクトリパス取得
        Path outputBaseDir = Optional.ofNullable(outputPath).filter(StringUtils::isNotBlank).map(Paths::get)
                .orElse(Paths.get(OUTPUT_BASE_DIRECTORY));

        // Entity取得
        final BaseInfoEntity baseInfoEntity = repository.selectBaseInfo();
        final List<TableEntity> tableEntityList = repository.selectTableList(targetSchemaList, targetTableList);
        final Columns columns = Columns.of(repository.selectColumnList(targetSchemaList, targetTableList));
        final Indexes indexes = Indexes.of(repository.selectIndexList(targetSchemaList, targetTableList));
        final Constraints constraints = Constraints.of(repository.selectConstraintList(targetSchemaList, targetTableList));
        final ForeignKeys foreignKeys = ForeignKeys.of(repository.selectForeignKeyList(targetSchemaList, targetTableList));

        // テーブル一覧出力 -> ./output/ or {設定ファイルのFileParh}/tableList_{DB名}.md
        writer.writeTableDefinitionList(tableEntityList, baseInfoEntity, outputBaseDir);
        // テーブル定義出力 -> ./output/ or {設定ファイルのFileParh}/{DB名}/{スキーマ名}/{TBL分類}/{物理テーブル名}.md
        tableEntityList.stream()
                .filter(tableEntity -> tableEntity.needsWriteTableDefinition(targetSchemaList, targetTableList))
                .map(tableEntity -> TableDefinitionContent.assemble(baseInfoEntity, tableEntity, columns, indexes,
                        constraints, foreignKeys, outputBaseDir))
                .forEach(writer::writeTableDefinition);
    }
}
