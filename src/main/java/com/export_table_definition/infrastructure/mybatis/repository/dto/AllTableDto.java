package com.export_table_definition.infrastructure.mybatis.repository.dto;

import lombok.Data;

/**
 * テーブル情報に関してORMのデータの受け渡しに利用するDTOクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
@Data
public class AllTableDto {
	private String schemaName;
	private String logicalTableName;
	private String physicalTableName;
	private String tableType;
	private String tableInfoList;
	private String tableInfo;
	private String definition;
}
