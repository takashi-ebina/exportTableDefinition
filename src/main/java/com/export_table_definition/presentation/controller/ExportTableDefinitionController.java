package com.export_table_definition.presentation.controller;

import java.util.List;

import com.export_table_definition.application.usecase.ExportTableDefinitionUsecase;
import com.export_table_definition.domain.service.writer.TableDefinitionWriter;
import com.export_table_definition.infrastructure.log.Log4J2;
import com.export_table_definition.infrastructure.mybatis.repository.MyBatisTableDefinitionRepository;
import com.export_table_definition.presentation.controller.dto.ResultDto;
import com.export_table_definition.presentation.controller.type.ProcessResult;

/**
 * テーブル定義出力処理のコントローラークラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class ExportTableDefinitionController {
	
	private final String lineSeparator = System.getProperty("line.separator");
	
	private final ExportTableDefinitionUsecase exportTableDefinitionUsecase;
	private final Log4J2 logger = Log4J2.getInstance();
	
	public ExportTableDefinitionController() {
		exportTableDefinitionUsecase = new ExportTableDefinitionUsecase(
				new MyBatisTableDefinitionRepository(), new TableDefinitionWriter());
	}
	
	/**
	 * コントローラーメソッド
	 * 
	 * @param schemaList テーブル定義出力対象のスキーマのリスト
	 * @param tableList テーブル定義出力対象のテーブルのリスト
	 */
	public ResultDto execute(List<String> schemaList, List<String> tableList, String outputPath) {
		logger.logInfo("[START] exportTableDefinition");
		try {
			exportTableDefinitionUsecase.exportTableDefinition(schemaList, tableList, outputPath);
		} catch (Exception e) {
			logger.logError(e);
			return new ResultDto(ProcessResult.FAIL, 
					String.format("Failed to output table definition document. %s [errmsg]:%s", lineSeparator, e.getMessage()));
		} 
		logger.logInfo("[ END ] exportTableDefinition");
		return new ResultDto(ProcessResult.SUCCESS, "Table definition output is complete.");
	};

}
