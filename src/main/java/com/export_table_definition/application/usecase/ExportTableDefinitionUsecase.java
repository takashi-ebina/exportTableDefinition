package com.export_table_definition.application.usecase;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;

import com.export_table_definition.domain.model.AllColumnEntity;
import com.export_table_definition.domain.model.AllConstraintEntity;
import com.export_table_definition.domain.model.AllForeignkeyEntity;
import com.export_table_definition.domain.model.AllIndexEntity;
import com.export_table_definition.domain.model.AllTableEntity;
import com.export_table_definition.domain.model.BaseInfoEntity;
import com.export_table_definition.domain.repository.TableDefinitionRepository;
import com.export_table_definition.domain.service.writer.TableDefinitionWriterDomainService;
import com.export_table_definition.infrastructure.log.Log4J2;
import com.export_table_definition.infrastructure.util.FileUtil;
import com.google.inject.Inject;

/**
 * テーブル定義出力に関するユースケースクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class ExportTableDefinitionUsecase {

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
    public ExportTableDefinitionUsecase(TableDefinitionRepository repository, TableDefinitionWriterDomainService writer) {
        this.tableDefinitionRepository = repository;
        this.tableDefinitionWriter = writer;
    }

    /**
     * テーブル定義出力のユースケースを扱うメソッド
     * 
     * @param targetSchemaList テーブル定義出力対象のスキーマのリスト
     * @param targetTableList テーブル定義出力対象のテーブルのリスト
     * @param outputPath テーブル定義出力の出力先のパス
     */
    public void exportTableDefinition(List<String> targetSchemaList, List<String> targetTableList, String outputPath) {

        // ベースディレクトリパス
        final String strOutputBaseDirectory = StringUtils.isBlank(outputPath) ? OUTPUT_BASE_DIRECTORY : outputPath;
        final Path outputBaseDirectoryPath =  Paths.get(strOutputBaseDirectory);
        
        // Entity取得
        final BaseInfoEntity baseEntity = tableDefinitionRepository.selectBaseInfo();
        final List<AllTableEntity> tableEntityList = tableDefinitionRepository.selectAllTableInfo(targetSchemaList, targetTableList);
        final List<AllColumnEntity> columnEntityList = tableDefinitionRepository.selectAllColumnInfo(targetSchemaList,targetTableList);
        final List<AllIndexEntity> indexEntityList = tableDefinitionRepository.selectAllIndexInfo(targetSchemaList, targetTableList);
        final List<AllConstraintEntity> constraintEntityList = tableDefinitionRepository.selectAllConstraintInfo(targetSchemaList, targetTableList);
        final List<AllForeignkeyEntity> foreignkeyEntityList = tableDefinitionRepository.selectAllForeignkeyInfo(targetSchemaList, targetTableList);

        // テーブル定義の出力先ディレクトリ作成 -> ./output or {設定ファイルのFileParh}
        FileUtil.createDirectory(outputBaseDirectoryPath.toString());
        // テーブル一覧出力 -> ./output/tableList_{DB名}.md or {設定ファイルのFileParh}/tableList_{DB名}.md
        tableDefinitionWriter.writeTableDefinitionList(tableEntityList, baseEntity, outputBaseDirectoryPath);
        // テーブル定義出力
        tableEntityList.forEach(createTableExporter(targetSchemaList, targetTableList, outputBaseDirectoryPath,
                baseEntity, columnEntityList, indexEntityList, constraintEntityList, foreignkeyEntityList));
    }

    private Path createOutputTablesDirectoryPath(Path outputBaseDirectoryPath, BaseInfoEntity baseEntity, AllTableEntity tableVo) {
        return outputBaseDirectoryPath.resolve(baseEntity.dbName()).resolve(tableVo.schemaName()).resolve(tableVo.tableType());
    }
    
    private Consumer<AllTableEntity> createTableExporter(List<String> targetSchemaList, List<String> targetTableList,
            Path outputBaseDirectoryPath, BaseInfoEntity baseEntity, List<AllColumnEntity> columnEntityList,
            List<AllIndexEntity> indexEntityList, List<AllConstraintEntity> constraintEntityList,
            List<AllForeignkeyEntity> foreignkeyEntityList) {
        return tableVo -> {
            if (!tableVo.needsWriteTableDefinition(targetSchemaList, targetTableList)) {
                return;
            }
            // テーブル定義出力先ディレクトリパス 
            //  -> ./output/{DB名}/{スキーマ名}/{TBL分類}
            //     or {設定ファイルのFileParh}/{DB名}/{スキーマ名}/{TBL分類}
            final Path directoryPath = createOutputTablesDirectoryPath(outputBaseDirectoryPath, baseEntity, tableVo);
            // テーブル定義出力先ファイルパス
            //  -> ./output/{DB名}/{スキーマ名}/{TBL分類}/{物理テーブル名}.md 
            //     or {設定ファイルのFileParh}/{DB名}/{スキーマ名}/{TBL分類}/{物理テーブル名}.md
            final Path filePath = directoryPath.resolve(tableVo.physicalTableName() + ".md");

            FileUtil.createDirectory(directoryPath.toString());
            tableDefinitionWriter.writeTableDefinition(tableVo, baseEntity, columnEntityList,
                    indexEntityList, constraintEntityList, foreignkeyEntityList, filePath);

            logger.logDebug(String.format("exportTableDefinition complete. [filePath=%s]", filePath.toString()));
        };
    }

}
