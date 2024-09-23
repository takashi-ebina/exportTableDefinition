package com.export_table_definition.application.usecase;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.export_table_definition.domain.model.AllColumnEntity;
import com.export_table_definition.domain.model.AllConstraintEntity;
import com.export_table_definition.domain.model.AllForeignkeyEntity;
import com.export_table_definition.domain.model.AllIndexEntity;
import com.export_table_definition.domain.model.AllTableEntity;
import com.export_table_definition.domain.model.BaseInfoEntity;
import com.export_table_definition.domain.repository.TableDefinitionRepository;
import com.export_table_definition.domain.service.writer.TableDefinitionWriter;
import com.export_table_definition.infrastructure.log.Log4J2;
import com.export_table_definition.infrastructure.util.FileUtil;

/**
 * テーブル定義出力に関するユースケースクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class ExportTableDefinitionUsecase {

	private final String OUTPUT_DIRECTORY = "./output";
	private final String TABLES_DIRECTORY_NAME = "tables";
	private final String TABLE_LIST_FILE_PREFIX = "tableList";

	private final Log4J2 logger = Log4J2.getInstance();
	
	private final TableDefinitionRepository tableDefinitionRepository;
	private final TableDefinitionWriter tableDefinitionWriter;

	/**
	 * コンストラクタ
	 * 
	 * @param repository テーブル定義出力に関するリポジトリクラス
	 * @param writer テーブル定義を書き込むクラス
	 */
	public ExportTableDefinitionUsecase(TableDefinitionRepository repository, TableDefinitionWriter writer) {
		this.tableDefinitionRepository = repository;
		this.tableDefinitionWriter = writer;
	}

	/**
	 * テーブル定義出力のユースケースを扱うメソッド
	 * 
	 * @param schemaList テーブル定義出力対象のスキーマのリスト
	 * @param tableList テーブル定義出力対象のテーブルのリスト
	 */
	public void exportTableDefinition(List<String> schemaList, List<String> tableList) {

		// Entity取得
		final BaseInfoEntity baseEntity = tableDefinitionRepository.selectBaseInfo();
		final List<AllTableEntity> tableEntityList = tableDefinitionRepository.selectAllTableInfo();
		final List<AllColumnEntity> columnEntityList = tableDefinitionRepository.selectAllColumnInfo(schemaList, tableList);
		final List<AllIndexEntity> indexEntityList = tableDefinitionRepository.selectAllIndexInfo(schemaList, tableList);
		final List<AllConstraintEntity> constraintEntityList = tableDefinitionRepository.selectAllConstraintInfo(schemaList, tableList);
		final List<AllForeignkeyEntity> foreignkeyEntityList = tableDefinitionRepository.selectAllForeignkeyInfo(schemaList, tableList);

		// テーブル定義の出力先ディレクトリ作成 -> ./output/{DB名}/tables/
		final Path tablesDirectoryPath = createOutputTablesDirectory(baseEntity);
		FileUtil.createDirectory(tablesDirectoryPath.toString());
		
		// テーブル一覧出力 -> ./output/tableList_{DB名}.md
		final Path tableListFilePath = createTableListFilePath(baseEntity);
		tableDefinitionWriter.writeTableDefinitionList(tableEntityList, baseEntity, tableListFilePath.toFile());

		// テーブル定義出力
		tableEntityList.forEach(tableVo -> {
			if (!needsWriteTableDefinition(tableVo, columnEntityList)) {
				return;
			}
			// ./output/{DB名}/tables/{スキーマ名}
			final Path directoryPath = tablesDirectoryPath.resolve(tableVo.schemaName());
			// ./output/{DB名}/tables/{スキーマ名}/{物理テーブル名}.md
			final Path filePath = directoryPath.resolve(tableVo.physicalTableName() + ".md");

			FileUtil.createDirectory(directoryPath.toString());
			tableDefinitionWriter.writeTableDefinition(tableVo, baseEntity, columnEntityList, indexEntityList, constraintEntityList, foreignkeyEntityList, filePath.toFile());
			
			logger.logDebug(String.format("exportTableDefinition complete. [filePath=%s]", filePath));
		});
	}
	
	private Path createOutputTablesDirectory(BaseInfoEntity baseEntity) {
		return Paths.get(OUTPUT_DIRECTORY, baseEntity.dbName(), TABLES_DIRECTORY_NAME);
	}
	
	private Path createTableListFilePath(BaseInfoEntity baseEntity) {
		return Paths.get(OUTPUT_DIRECTORY, TABLE_LIST_FILE_PREFIX + "_" + baseEntity.dbName() + ".md");
	}
	
	private boolean needsWriteTableDefinition(AllTableEntity table, List<AllColumnEntity> columns) {
		return columns.stream().anyMatch(column -> table.getSchemaTableName().equals(column.getSchemaTableName()));
	}
}
