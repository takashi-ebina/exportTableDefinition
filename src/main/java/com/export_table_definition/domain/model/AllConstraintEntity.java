package com.export_table_definition.domain.model;

/**
 * 制約情報に関するrecordクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public record AllConstraintEntity(String schemaName, String tableName, String constraintInfo) {
	
	/**
	 * スキーマ.テーブル 形式の名称を取得するメソッド
	 * 
	 * @return スキーマ.テーブル 形式の名称
	 */
	public String getSchemaTableName() {
		return schemaName + "." + tableName;
	}
	
}
