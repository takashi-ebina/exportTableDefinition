package com.export_table_definition;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import com.export_table_definition.infrastructure.util.PropertyLoaderUtil;
import com.export_table_definition.presentation.controller.ExportTableDefinitionController;
import com.export_table_definition.presentation.controller.dto.ResultDto;

/**
 * テーブル定義出力処理を呼び出すクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class ExportTableDefinition {
	
	/**
	 * テーブル定義出力処理のエントリーポイントメソッド
	 * 
	 * @param args コマンドライン引数
	 */
	public static void main(String[] args) {
		final ResourceBundle res = PropertyLoaderUtil.getResourceBundle("ExportTableDefinition");
		final List<String> schemaList = convertStringToList(res.getString("schema"));
		final List<String> tableList = convertStringToList(res.getString("table"));
		final String outputPath = res.getString("outputPath");
		
		System.out.println("Starting output of table definition document.");
		System.out.println("Please wait a moment ...");
		System.out.println("");
		
		final ResultDto resultDto = new ExportTableDefinitionController().execute(schemaList, tableList, outputPath);
		
		System.out.println(resultDto.getResultMessage());
	}

	private static List<String> convertStringToList(String input) {
		if (StringUtils.isEmpty(input)) {
			return List.of();
		}
		return Arrays.asList(input.split(","));
	}
}
