package com.export_table_definition.infrastructure.mybatis.repository.dto;

import lombok.Data;

/**
 * 外部キー情報に関してORMのデータの受け渡しに利用するDTOクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
@Data
public class AllForeignkeyDto {
    private String schemaName;
    private String tableName;
    private String foreignkeyInfo;
}
