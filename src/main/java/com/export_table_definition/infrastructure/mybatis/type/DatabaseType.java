package com.export_table_definition.infrastructure.mybatis.type;

import java.util.Arrays;
import java.util.Objects;

import com.export_table_definition.domain.repository.TableDefinitionRepository;
import com.export_table_definition.infrastructure.mybatis.repository.OracleTableDefinitionRepository;
import com.export_table_definition.infrastructure.mybatis.repository.PostgresTableDefinitionRepository;

/**
 * Databaseの種別をもつ列挙型クラス
 */
public enum DatabaseType {
    POSTGRESQL("postgresql", PostgresTableDefinitionRepository.class),
    ORACLE("oracle", OracleTableDefinitionRepository.class);
    
    private final String name;
    private final Class<? extends TableDefinitionRepository> repositoryClass;
    
    DatabaseType(String name, Class<? extends TableDefinitionRepository> repositoryClass) {
        this.name = name;
        this.repositoryClass = repositoryClass;
    }
    
    public String getName() {
        return name;
    }
    
    public Class<? extends TableDefinitionRepository> getRepositoryClass() {
        return repositoryClass;
    }
    
    /**
     * Database名に紐づくEnumを返却する。
     * 
     * @param attribute Enum逆引きに用いる値
     * @return DatabaseTypeを返却する。
     * @throws IllegalArgumentException 対象のEnumが存在しない場合にthrowする。
     */
    public static DatabaseType findByName(String name) {
        return Arrays.stream(DatabaseType.values()).filter(e -> Objects.equals(name, e.getName())).findFirst()
                .orElseThrow(() -> new IllegalArgumentException());
    }
}
