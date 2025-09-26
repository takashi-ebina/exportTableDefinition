package com.export_table_definition.infrastructure.db.repository.dto;

import com.export_table_definition.domain.model.IndexEntity;

import lombok.Data;

/**
 * インデックス情報に関してORMのデータの受け渡しに利用するDTOクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
@Data
public class IndexDto {
    private String schemaName;
    private String tableName;
    private String indexInfo;
    
    /**
     * DTOからEntityへの変換メソッド
     * 
     * @return AllIndexEntityのインスタンス
     */
    public IndexEntity toEntity() {
        return new IndexEntity(schemaName, tableName, indexInfo);
    }
}
