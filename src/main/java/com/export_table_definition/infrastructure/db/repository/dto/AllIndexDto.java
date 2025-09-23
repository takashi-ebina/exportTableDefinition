package com.export_table_definition.infrastructure.db.repository.dto;

import com.export_table_definition.domain.model.AllIndexEntity;

import lombok.Data;

/**
 * インデックス情報に関してORMのデータの受け渡しに利用するDTOクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
@Data
public class AllIndexDto {
    private String schemaName;
    private String tableName;
    private String indexInfo;
    
    public AllIndexEntity toEntity() {
        return new AllIndexEntity(schemaName, tableName, indexInfo);
    }
}
