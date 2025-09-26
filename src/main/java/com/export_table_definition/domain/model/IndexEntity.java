package com.export_table_definition.domain.model;

/**
 * インデックス情報に関するrecordクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public record IndexEntity(String schemaName, String tableName, String indexInfo) {

    /**
     * スキーマ.テーブル 形式の名称を取得するメソッド
     * 
     * @return スキーマ.テーブル 形式の名称
     */
    public String getSchemaTableName() {
        return schemaName + "." + tableName;
    }
}
