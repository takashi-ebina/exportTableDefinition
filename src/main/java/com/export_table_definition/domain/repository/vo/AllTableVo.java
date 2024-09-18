package com.export_table_definition.domain.repository.vo;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class AllTableVo {
	private String schemaName;
	private String logicalTableName;
	private String physicalTableName;
	private String tableType;
	private String tableInfoList;
	private String tableInfo;
	private String definition;
	
	public String getSchemaTableName() {
		return schemaName + "." + physicalTableName;
	}
	
	public String getHeaderTableName() {
		if (StringUtils.isEmpty(logicalTableName)) {
			return physicalTableName;
		}
		return physicalTableName + "(" + logicalTableName + ")";
	}
	
	public boolean isView() {
		return "materialized view".equals(tableType) || "view".equals(tableType);
	}
}
