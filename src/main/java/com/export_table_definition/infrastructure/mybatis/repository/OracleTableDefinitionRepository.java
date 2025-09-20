package com.export_table_definition.infrastructure.mybatis.repository;

import com.export_table_definition.infrastructure.mybatis.type.DatabaseType;

/**
 * [oracle]テーブル定義出力に関するリポジトリクラス
 * 
 * OracleのLong型をJDBCDriverでは扱えないため、Oracleにおいてデフォルト、制約、View/materialized_viewのソースは表示不可
 * 参考リンク：https://support.oracle.com/knowledge/Middleware/832903_1.html
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class OracleTableDefinitionRepository extends AbstractTableDefinitionRepository {

    /**
     * コンストラクタ
     */
    public OracleTableDefinitionRepository() {
        super(DatabaseType.ORACLE);
    }
}
