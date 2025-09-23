package com.export_table_definition.infrastructure.db.repository.dto;

import com.export_table_definition.domain.model.AllTableEntity;

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
    
    /**
     * DTOからEntityへの変換メソッド
     * 
     * @return AllTableEntityのインスタンス
     */
    public AllTableEntity toEntity() {
        return new AllTableEntity(schemaName, logicalTableName, physicalTableName, tableType, tableInfoList, tableInfo, definition);
    }
}
