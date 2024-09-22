package com.export_table_definition.domain.model;

import org.apache.commons.lang3.StringUtils;

/**
 * テーブル情報に関するrecordクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public record AllTableEntity(String schemaName, String logicalTableName, String physicalTableName, String tableType,
		String tableInfoList, String tableInfo, String definition) {

	/**
	 * スキーマ.テーブル 形式の名称を取得するメソッド
	 * 
	 * @return スキーマ.テーブル 形式の名称
	 */
	public String getSchemaTableName() {
		return schemaName + "." + physicalTableName;
	}

	/**
	 * 論理テーブル名（物理テーブル名） 形式の名称を取得するメソッド
	 * 
	 * @return 物理テーブル名（論理テーブル名） 形式の名称を返却。<br>
	 *         論理テーブル名が存在しない場合は 物理テーブル名 形式の名称を返却
	 */
	public String getHeaderTableName() {
		if (StringUtils.isEmpty(logicalTableName)) {
			return physicalTableName;
		}
		return physicalTableName + "（" + logicalTableName + "）";
	}

	/**
	 * view または materialized viewであるか判定するメソッド
	 * 
	 * @return view または materialized viewの場合はture。それ以外の場合はfalseを返却
	 */
	public boolean isView() {
		return "materialized view".equals(tableType) || "view".equals(tableType);
	}
}
