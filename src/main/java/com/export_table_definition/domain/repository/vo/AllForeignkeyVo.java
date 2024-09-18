package com.export_table_definition.domain.repository.vo;

import lombok.Data;

@Data
public class AllForeignkeyVo {
	private String schemaName;
	private String tableName;
	private String foreignkeyInfo;
	
	public String getSchemaTableName() {
		return schemaName + "." + tableName;
	}
}
