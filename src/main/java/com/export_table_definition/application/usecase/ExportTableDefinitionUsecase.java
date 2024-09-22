package com.export_table_definition.application.usecase;

import java.io.File;
import java.util.List;

import com.export_table_definition.domain.model.AllColumnEntity;
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

	private final String OUTPUT_TABLES_DIRECTORY = "./output/tables";
	private final String OUTPUT_TABLE_LIST_FILE = "./output/tableList.md";

	private final Log4J2 logger = Log4J2.getInstance();
	
	private final TableDefinitionRepository tableDefinitionRepository;
	private final TableDefinitionWriter tableDefinitionWriter;

	/**
	 * コンストラクタ
	 * 
	 * @param repository テーブル定義出力に関するリポジトリクラス
	 * @param writer
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
		
		// テーブル定義の出力先ディレクトリ作成
		FileUtil.createDirectory(OUTPUT_TABLES_DIRECTORY);

		// Entity取得
		final BaseInfoEntity baseEntity = tableDefinitionRepository.selectBaseInfo();
		final List<AllTableEntity> tableEntityList = tableDefinitionRepository.selectAllTableInfo();
		final List<AllColumnEntity> columnEntityList = tableDefinitionRepository.selectAllColumnInfo(schemaList, tableList);
		final List<AllIndexEntity> indexEntityList = tableDefinitionRepository.selectAllIndexInfo(schemaList, tableList);
		final List<AllForeignkeyEntity> foreignkeyEntityList = tableDefinitionRepository.selectAllForeignkeyInfo(schemaList, tableList);

		// テーブル一覧出力
		tableDefinitionWriter.writeTableDefinitionList(tableEntityList, baseEntity, new File(OUTPUT_TABLE_LIST_FILE));

		// テーブル定義出力
		tableEntityList.forEach(tableVo -> {
			final String directoryPath = OUTPUT_TABLES_DIRECTORY + "/" + tableVo.schemaName();
			final String filePath = directoryPath + "/" + tableVo.physicalTableName() + ".md";
			
			FileUtil.createDirectory(directoryPath);
			tableDefinitionWriter.writeTableDefinition(tableVo, baseEntity, columnEntityList, indexEntityList, foreignkeyEntityList, new File(filePath));
			
			logger.logDebug(String.format("exportTableDefinition complete. [filePath=%s]", filePath));
		});
	}
}
