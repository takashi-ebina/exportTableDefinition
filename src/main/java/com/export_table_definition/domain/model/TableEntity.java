package com.export_table_definition.domain.model;

import java.nio.file.Path;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * テーブル情報に関するrecordクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public record TableEntity(String schemaName, String logicalTableName, String physicalTableName, String tableType,
        String tableInfoList, String tableInfo, String definition) {

    /**
     * スキーマ.テーブル 形式の名称を取得するメソッド
     * 
     * @return スキーマ.テーブル 形式の名称
     */
    public String getSchemaTableName() {
        return schemaName + "." + physicalTableName;
    }

    /**
     * 論理テーブル名（物理テーブル名） 形式の名称を取得するメソッド
     * 
     * @return 物理テーブル名（論理テーブル名） 形式の名称を返却。<br>
     *         論理テーブル名が存在しない場合は 物理テーブル名 形式の名称を返却
     */
    public String getHeaderTableName() {
        if (StringUtils.isEmpty(logicalTableName)) {
            return physicalTableName;
        }
        return physicalTableName + "（" + logicalTableName + "）";
    }

    /**
     * view または materialized viewであるか判定するメソッド
     * 
     * @return view または materialized viewの場合はture。それ以外の場合はfalseを返却
     */
    public boolean isView() {
        return "materialized_view".equals(tableType) || "view".equals(tableType);
    }

    /**
     *  テーブル定義書の作成を行うか判定するメソッド<br>
     *  <br>
     *  条件を整理すると以下となる
     *  
     * +------------------+-----------------+------------------+-----------------+---------------------------+ <br>
     * | 条件                                                                    | 結果                      | <br>
     * +------------------+-----------------+------------------+-----------------+                           + <br>
     * | リストの中身が1件以上か            | 対象のスキーマ／TBLが存在するか    |                           | <br>
     * +------------------+-----------------+------------------+-----------------+---------------------------+ <br>
     * | targetSchemaList | targetTableList | targetSchemaList | targetTableList | TBL定義の書込を実施するか | <br>
     * +------------------+-----------------+------------------+-----------------+---------------------------+ <br>
     * | ×               | ×              | ―               | ―              | TRUE                      | <br>
     * | ×               | ○              | ―               | ○              | TRUE                      | <br>
     * | ×               | ○              | ―               | ×              | FALSE                     | <br>
     * | ○               | ×              | ○               | ―              | TRUE                      | <br>
     * | ○               | ×              | ×               | ―              | FALSE                     | <br>
     * | ○               | ○              | ○               | ○              | TRUE                      | <br>
     * | ○               | ○              | ○               | ×              | FALSE                     | <br>
     * | ○               | ○              | ×               | ○              | FALSE                     | <br>
     * | ○               | ○              | ×               | ×              | FALSE                     | <br>
     * +------------------+-----------------+------------------+-----------------+---------------------------+ <br>
     *
     * @param targetSchemaList スキーマ名のリスト
     * @param targetTableList テーブル名のリスト
     * @return テーブル定義書の書き込みが必要かどうか
     */
    public boolean needsWriteTableDefinition(List<String> targetSchemaList, List<String> targetTableList) {
        final boolean hasSchemaList = targetSchemaList != null && !targetSchemaList.isEmpty();
        final boolean hasTableList = targetTableList != null && !targetTableList.isEmpty();

        final boolean isSchemaMatch = hasSchemaList && targetSchemaList.contains(schemaName);
        final boolean isTableMatch = hasTableList && targetTableList.contains(physicalTableName);

        if (!hasSchemaList && !hasTableList) {
            return true;
        }
        if (!hasSchemaList) {
            return isTableMatch;
        }
        if (!hasTableList) {
            return isSchemaMatch;
        }
        return isSchemaMatch && isTableMatch;
    }
    
    /**
     * テーブル定義書の出力先ディレクトリパスを取得するメソッド<br>
     * <br>
     * 出力先ディレクトリパスは以下の形式となる<br>
     * {base}/{DB名}/{スキーマ名}/{TBL分類}/
     * 
     * @param base ベースディレクトリパス
     * @param dbName DB名
     * @return テーブル定義書の出力先ディレクトリパス
     */
    public Path toTableDefinitionDirectory(Path base, String dbName) {
        return base.resolve(dbName)
                   .resolve(schemaName)
                   .resolve(tableType);
    }
    
    /**
     * テーブル定義書の出力先ファイルパスを取得するメソッド<br>
     * <br>
     * 出力先ファイルパスは以下の形式となる<br>
     * {base}/{DB名}/{スキーマ名}/{TBL分類}/{物理テーブル名}.md
     * 
     * @param base ベースディレクトリパス
     * @param dbName DB名
     * @return テーブル定義書の出力先ファイルパス
     */
    public Path toTableDefinitionFile(Path base, String dbName) {
        return base.resolve(physicalTableName + ".md");
    }
}
