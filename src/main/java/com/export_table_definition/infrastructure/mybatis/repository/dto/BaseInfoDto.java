package com.export_table_definition.infrastructure.mybatis.repository.dto;

import com.export_table_definition.domain.model.BaseInfoEntity;

import lombok.Data;

/**
 * データベースの基本情報に関してORMのデータの受け渡しに利用するDTOクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
@Data
public class BaseInfoDto {
    private String dbName;
    private String baseInfo;
    
    public BaseInfoEntity toEntity() {
        return new BaseInfoEntity(dbName, baseInfo);
    }
}
