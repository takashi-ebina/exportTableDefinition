package com.export_table_definition.domain.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import com.export_table_definition.domain.repository.vo.AllColumnVo;
import com.export_table_definition.domain.repository.vo.AllForeignkeyVo;
import com.export_table_definition.domain.repository.vo.AllIndexVo;
import com.export_table_definition.domain.repository.vo.AllTableVo;
import com.export_table_definition.domain.repository.vo.BaseInfoVo;

public class TableDefinitionWriter {

	private final String lineSeparator = System.getProperty("line.separator");
	private final String lineSeparator2 = lineSeparator + lineSeparator;

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
	private final String foreignkeyInfoTableHeader = """
			## 外部キー情報
			
			| No. | 外部キー名 | カラムリスト | 参照先 | 参照先カラムリスト |
			|:---|:---|:---|:---|:---|
			""";
	

	public void writeTableDefinitionList(List<AllTableVo> tables, BaseInfoVo baseInfo, File outputFile) {
		try (final TableDefinitionBufferedWriterWrap bw = new TableDefinitionBufferedWriterWrap(new FileWriter(outputFile, false))) {
			// ヘッダー
			writeHeader(bw, "テーブル一覧");
			// 基本情報
			writeBaseInfo(bw, baseInfo);
			// テーブル一覧
			writeTableInfoList(bw, tables);
			// フッター
			writeFooter(bw);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	public void writeTableDefinition(AllTableVo table, BaseInfoVo baseInfo, 
			List<AllColumnVo> columns, List<AllIndexVo> indexes, List<AllForeignkeyVo> foreignkeys, File outputFile) {
		try (final TableDefinitionBufferedWriterWrap bw = new TableDefinitionBufferedWriterWrap(new FileWriter(outputFile, false))) {
			// ヘッダー
			writeHeader(bw, table.getHeaderTableName());
			// 基本情報
			writeBaseInfo(bw, baseInfo);
			// テーブル情報
			writeTableInfo(bw, table);
			// カラム情報
			writeColumnInfo(bw, columns, table);
			// View情報
			writeViewInfo(bw, table);
			// インデックス情報
			writeIndexInfo(bw, indexes, table);
			// 外部キー情報
			writeForeignkeyInfo(bw, foreignkeys, table);
			// フッター
			writeFooter(bw);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	private void writeHeader(TableDefinitionBufferedWriterWrap bw, String headerName) {
		bw.write("# " + headerName + lineSeparator2);
	}
	
	private void writeBaseInfo(TableDefinitionBufferedWriterWrap bw, BaseInfoVo baseInfo) {
		bw.write(baseInfoTableHeader);
		bw.write(baseInfo.getBaseInfo() + lineSeparator2);
	}
	
	private void writeTableInfoList(TableDefinitionBufferedWriterWrap bw, List<AllTableVo> tables) {
		bw.write(tableInfoListTableHeader);
		tables.stream().forEach(table -> bw.write(table.getTableInfoList() + lineSeparator));
		bw.write(lineSeparator);
	}
	
	private void writeTableInfo(TableDefinitionBufferedWriterWrap bw, AllTableVo table) {
		bw.write(tableInfoTableHeader);
		bw.write(table.getTableInfo() + lineSeparator2);
	}
	
	private void writeColumnInfo(TableDefinitionBufferedWriterWrap bw, List<AllColumnVo> columns, AllTableVo table) {
		bw.write(columnInfoTableHeader);
		columns.stream()
			.filter(column -> column.getSchemaTableName().equals(table.getSchemaTableName()))
			.forEach(column -> bw.write(column.getColumnInfo() + lineSeparator));
		bw.write(lineSeparator);
	}
	
	private void writeViewInfo(TableDefinitionBufferedWriterWrap bw, AllTableVo table) {
		if (!table.isView()) {
			return;
		}
		bw.write(viewInfoHeader);
		bw.write(table.getDefinition());
		bw.write(viewInfoFooter);
	}
	
	private void writeIndexInfo(TableDefinitionBufferedWriterWrap bw, List<AllIndexVo> indexes, AllTableVo table) {
		bw.write(indexInfoTableHeader);
		indexes.stream()
			.filter(index -> index.getSchemaTableName().equals(table.getSchemaTableName()))
			.forEach(index -> bw.write(index.getIndexInfo() + lineSeparator));
		bw.write(lineSeparator);
	}
	private void writeForeignkeyInfo(TableDefinitionBufferedWriterWrap bw, List<AllForeignkeyVo> foreignkeys, AllTableVo table) {
		bw.write(foreignkeyInfoTableHeader);
		foreignkeys.stream()
			.filter(foreignkey -> foreignkey.getSchemaTableName().equals(table.getSchemaTableName()))
			.forEach(foreignkey -> bw.write(foreignkey.getForeignkeyInfo() + lineSeparator));
		bw.write(lineSeparator);
	}
	
	private void writeFooter(TableDefinitionBufferedWriterWrap bw) {
		bw.write(horizonLine);
		bw.write(lineSeparator);
		bw.write("[テーブル一覧へ](../../tableList.md)  ");
		bw.write(lineSeparator);
	}
}
