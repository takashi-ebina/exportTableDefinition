package com.export_table_definition.domain.service.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import com.export_table_definition.domain.model.AllColumnEntity;
import com.export_table_definition.domain.model.AllConstraintEntity;
import com.export_table_definition.domain.model.AllForeignkeyEntity;
import com.export_table_definition.domain.model.AllIndexEntity;
import com.export_table_definition.domain.model.AllTableEntity;
import com.export_table_definition.domain.model.BaseInfoEntity;

/**
 * テーブル定義を書き込むクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class TableDefinitionWriter {

	private final String lineSeparator = System.getProperty("line.separator");
	private final String lineSeparatorDouble = lineSeparator + lineSeparator;

	private final String horizonLine = "___";
	private final String baseInfoTableHeader = """
			## 基本情報
			
			| RDBMS | データベース名| 作成日 |
			|:---|:---|:---|
			""";
	private final String tableInfoListTableHeader = """
			## テーブル情報
			
			| No. | スキーマ名 | 論理テーブル名 | 物理テーブル名 | 区分 | Link | 備考 |
			|:---|:---|:---|:---|:---|:---|:---|
			""";
	private final String tableExplanationHeader = """
			## テーブル説明
			
			""";
	private final String tableInfoTableHeader = """
			## テーブル情報
			
			| スキーマ名 | 論理テーブル名 | 物理テーブル名 | 区分 | 備考 |
			|:---|:---|:---|:---|:---|
			""";
	private final String columnInfoTableHeader = """
			## カラム情報
			
			| No. | 論理名 | 物理名 | データ型 | PK | Not Null | デフォルト | 備考 |
			|:---|:---|:---|:---|:---|:---|:---|:---|
			""";
	private final String viewInfoHeader = """
			## ソース
			
			```sql
			""";
	private final String viewInfoFooter = """
			
			```
			
			""";
	private final String indexInfoTableHeader = """
			## インデックス情報
			
			| No. | インデックス名 | カラムリスト |
			|:---|:---|:---|
			""";
	private final String constraintInfoTableHeader = """
			## 制約情報
			
			| No. | 制約名 | 種類 | 制約定義 |
			|:---|:---|:---|:---|
			""";
	private final String foreignkeyInfoTableHeader = """
			## 外部キー情報
			
			| No. | 外部キー名 | カラムリスト | 参照先 | 参照先カラムリスト |
			|:---|:---|:---|:---|:---|
			""";
	
	private final String tableListLink = "[テーブル一覧へ](../../../tableList_%s.md)  ";
	

	/**
	 * テーブル一覧の書き込み処理を行うメソッド
	 * 
	 * @param tables テーブル情報リスト
	 * @param baseInfo データベースの基本情報
	 * @param outputFile 出力ファイル
	 */
	public void writeTableDefinitionList(List<AllTableEntity> tables, BaseInfoEntity baseInfo, File outputFile) {
		try (final TableDefinitionBufferedWriterWrap bw = new TableDefinitionBufferedWriterWrap(new FileWriter(outputFile, false))) {
			// ヘッダー
			writeHeader(bw, "テーブル一覧" + "（DB名：" + baseInfo.dbName() + "）");
			// 基本情報
			writeBaseInfo(bw, baseInfo);
			// テーブル一覧
			writeTableInfoList(bw, tables);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * テーブル定義の書き込み処理を行うメソッド
	 * 
	 * @param table テーブル情報
	 * @param baseInfo データベースの基本情報
	 * @param columns カラム情報
	 * @param indexes インデックス情報
	 * @param constraints 制約情報
	 * @param foreignkeys 外部キー情報
	 * @param outputFile 出力ファイル
	 */
	public void writeTableDefinition(AllTableEntity table, BaseInfoEntity baseInfo, List<AllColumnEntity> columns, 
			List<AllIndexEntity> indexes, List<AllConstraintEntity> constraints, List<AllForeignkeyEntity> foreignkeys, File outputFile) {
		try (final TableDefinitionBufferedWriterWrap bw = new TableDefinitionBufferedWriterWrap(new FileWriter(outputFile, false))) {
			// ヘッダー
			writeHeader(bw, table.getHeaderTableName());
			// 基本情報
			writeBaseInfo(bw, baseInfo);
			// テーブル説明
			writeTableExplanation(bw);
			// テーブル情報
			writeTableInfo(bw, table);
			// カラム情報
			writeColumnInfo(bw, columns, table);
			// View情報
			writeViewInfo(bw, table);
			// インデックス情報
			writeIndexInfo(bw, indexes, table);
			// 制約情報
			writeConstraintInfo(bw, constraints, table);
			// 外部キー情報
			writeForeignkeyInfo(bw, foreignkeys, table);
			// フッター
			writeFooter(bw, baseInfo);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	private void writeHeader(TableDefinitionBufferedWriterWrap bw, String headerName) {
		bw.write("# " + headerName + lineSeparatorDouble);
	}
	
	private void writeBaseInfo(TableDefinitionBufferedWriterWrap bw, BaseInfoEntity baseInfo) {
		bw.write(baseInfoTableHeader);
		bw.write(baseInfo.baseInfo() + lineSeparatorDouble);
	}
	
	private void writeTableInfoList(TableDefinitionBufferedWriterWrap bw, List<AllTableEntity> tables) {
		bw.write(tableInfoListTableHeader);
		tables.stream().forEach(table -> bw.write(table.tableInfoList() + lineSeparator));
		bw.write(lineSeparator);
	}
	
	private void writeTableExplanation(TableDefinitionBufferedWriterWrap bw) {
		bw.write(tableExplanationHeader);
	}
	
	private void writeTableInfo(TableDefinitionBufferedWriterWrap bw, AllTableEntity table) {
		bw.write(tableInfoTableHeader);
		bw.write(table.tableInfo() + lineSeparatorDouble);
	}
	
	private void writeColumnInfo(TableDefinitionBufferedWriterWrap bw, List<AllColumnEntity> columns, AllTableEntity table) {
		bw.write(columnInfoTableHeader);
		columns.stream()
			.filter(column -> column.getSchemaTableName().equals(table.getSchemaTableName()))
			.forEach(column -> bw.write(column.columnInfo() + lineSeparator));
		bw.write(lineSeparator);
	}
	
	private void writeViewInfo(TableDefinitionBufferedWriterWrap bw, AllTableEntity table) {
		if (!table.isView()) {
			return;
		}
		bw.write(viewInfoHeader);
		bw.write(table.definition());
		bw.write(viewInfoFooter);
	}
	
	private void writeIndexInfo(TableDefinitionBufferedWriterWrap bw, List<AllIndexEntity> indexes, AllTableEntity table) {
		bw.write(indexInfoTableHeader);
		indexes.stream()
			.filter(index -> index.getSchemaTableName().equals(table.getSchemaTableName()))
			.forEach(index -> bw.write(index.indexInfo() + lineSeparator));
		bw.write(lineSeparator);
	}
	
	private void writeConstraintInfo(TableDefinitionBufferedWriterWrap bw, List<AllConstraintEntity> constraints, AllTableEntity table) {
		bw.write(constraintInfoTableHeader);
		constraints.stream()
			.filter(constraint -> constraint.getSchemaTableName().equals(table.getSchemaTableName()))
			.forEach(constraint -> bw.write(constraint.constraintInfo() + lineSeparator));
		bw.write(lineSeparator);
	}
	
	private void writeForeignkeyInfo(TableDefinitionBufferedWriterWrap bw, List<AllForeignkeyEntity> foreignkeys, AllTableEntity table) {
		bw.write(foreignkeyInfoTableHeader);
		foreignkeys.stream()
			.filter(foreignkey -> foreignkey.getSchemaTableName().equals(table.getSchemaTableName()))
			.forEach(foreignkey -> bw.write(foreignkey.foreignkeyInfo() + lineSeparator));
		bw.write(lineSeparator);
	}
	
	private void writeFooter(TableDefinitionBufferedWriterWrap bw, BaseInfoEntity baseInfo) {
		bw.write(horizonLine);
		bw.write(lineSeparator);
		bw.write(String.format(tableListLink, baseInfo.dbName()));
		bw.write(lineSeparator);
	}
}
