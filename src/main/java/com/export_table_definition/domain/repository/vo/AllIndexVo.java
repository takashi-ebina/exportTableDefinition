package com.export_table_definition.domain.repository.vo;

import lombok.Data;

@Data
public class AllIndexVo {
	private String schemaName;
	private String tableName;
	private String indexInfo;
	
	public String getSchemaTableName() {
		return schemaName + "." + tableName;
	}
}
