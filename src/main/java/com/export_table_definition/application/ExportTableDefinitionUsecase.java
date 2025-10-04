package com.export_table_definition.application;

import java.util.List;

/**
 * テーブル定義出力のユースケースを扱うインターフェース
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public interface ExportTableDefinitionUsecase {

    /**
     * テーブル定義出力のユースケースを扱うメソッド
     * 
     * @param targetSchemaList テーブル定義出力対象のスキーマのリスト
     * @param targetTableList  テーブル定義出力対象のテーブルのリスト
     * @param outputPath       テーブル定義出力の出力先のパス
     */
    public void exportTableDefinition(List<String> targetSchemaList, List<String> targetTableList, String outputPath);
}
