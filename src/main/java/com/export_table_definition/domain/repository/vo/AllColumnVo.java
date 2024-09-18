package com.export_table_definition.domain.repository.vo;

import lombok.Data;

@Data
public class AllColumnVo {
	private String schemaName;
	private String tableName;
	private String columnInfo;
	
	public String getSchemaTableName() {
		return schemaName + "." + tableName;
	}
}
