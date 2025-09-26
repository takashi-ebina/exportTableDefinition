package com.export_table_definition.domain.model;

/**
 * 外部キー情報に関するrecordクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public record ForeignkeyEntity(String schemaName, String tableName, String foreignkeyInfo) {

    /**
     * スキーマ.テーブル 形式の名称を取得するメソッド
     * 
     * @return スキーマ.テーブル 形式の名称
     */
    public String getSchemaTableName() {
        return schemaName + "." + tableName;
    }
}
