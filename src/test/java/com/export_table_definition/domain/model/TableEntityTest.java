package com.export_table_definition.domain.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.export_table_definition.domain.model.entity.TableEntity;

class TableEntityTest {
	
	private TableEntity tableEntity;

	@Nested
	class testNeedsWriteTableDefinition {
		
		static Stream<Arguments> testNeedsWriteTableDefinitionProvider() {
		    return Stream.of(
		    	arguments("実施する　（テーブル定義出力対象のスキーマ／TBLリストが存在しない）", Arrays.asList(), Arrays.asList(), true),
		    	arguments("実施する　（テーブル定義出力対象のTBLリストが存在 かつ 対象のTBLが存在する）", Arrays.asList(), Arrays.asList("testTable"), true),
		    	arguments("実施しない（テーブル定義出力対象のTBLリストが存在 かつ 対象のTBLが存在しない）", Arrays.asList(), Arrays.asList("mismatchTable"), false),
		    	arguments("実施する　（テーブル定義出力対象のスキーマリストが存在 かつ 対象のスキーマが存在する）", Arrays.asList("test_schema"), Arrays.asList(), true),
		    	arguments("実施しない（テーブル定義出力対象のスキーマリストが存在 かつ 対象のスキーマが存在しない）", Arrays.asList("mismatch_schema"), Arrays.asList(), false),
		    	arguments("実施する　（テーブル定義出力対象のスキーマ／TBLリストが存在 かつ 対象のスキーマ／TBLが存在する）", Arrays.asList("test_schema"), Arrays.asList("testTable"), true),
		    	arguments("実施しない（テーブル定義出力対象のスキーマ／TBLリストが存在 かつ 対象のTBLが存在しない）", Arrays.asList("test_schema"), Arrays.asList("mismatchTable"), false),
		    	arguments("実施しない（テーブル定義出力対象のスキーマ／TBLリストが存在 かつ 対象のスキーマが存在しない）",Arrays.asList("mismatch_schema"), Arrays.asList("testTable"), false),
		    	arguments("実施しない（テーブル定義出力対象のスキーマ／TBLリストが存在 かつ 対象のスキーマ／TBLが存在しない）", Arrays.asList("mismatch_schema"), Arrays.asList("mismatchTable"), false)
		    );
		}
		
		@DisplayName("【正常系】テーブル定義書の作成を行うか判定する")
		@ParameterizedTest
		@MethodSource("testNeedsWriteTableDefinitionProvider")
		void success1(String definition, List<String> targetSchemaList, List<String> targetTableList, boolean expected) {
			// Given
		    tableEntity = 
					new TableEntity(
					        "dbName",
							"test_schema", 
							"テストテーブル",
							"testTable",
							"table", 
							"|1|test_schema|テストテーブル|testTable|table|[■](./testdb/test_schema/table/testTable.md)||",
							"|test_schema|テストテーブル|testTable|table|",
							""
						);
			// When
			boolean result =  tableEntity.needsWriteTableDefinition(targetSchemaList, targetTableList);
			// Then
			assertEquals(expected, result);
		}
		
	}

}
