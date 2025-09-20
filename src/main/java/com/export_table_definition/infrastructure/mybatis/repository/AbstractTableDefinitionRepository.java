package com.export_table_definition.infrastructure.mybatis.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.ibatis.session.SqlSession;

import com.export_table_definition.domain.model.AllColumnEntity;
import com.export_table_definition.domain.model.AllConstraintEntity;
import com.export_table_definition.domain.model.AllForeignkeyEntity;
import com.export_table_definition.domain.model.AllIndexEntity;
import com.export_table_definition.domain.model.AllTableEntity;
import com.export_table_definition.domain.model.BaseInfoEntity;
import com.export_table_definition.domain.repository.TableDefinitionRepository;
import com.export_table_definition.infrastructure.mybatis.MyBatisSqlSessionFactory;
import com.export_table_definition.infrastructure.mybatis.repository.dto.AllColumnDto;
import com.export_table_definition.infrastructure.mybatis.repository.dto.AllConstraintDto;
import com.export_table_definition.infrastructure.mybatis.repository.dto.AllForeignkeyDto;
import com.export_table_definition.infrastructure.mybatis.repository.dto.AllIndexDto;
import com.export_table_definition.infrastructure.mybatis.repository.dto.AllTableDto;
import com.export_table_definition.infrastructure.mybatis.repository.dto.BaseInfoDto;
import com.export_table_definition.infrastructure.mybatis.type.DatabaseType;

/**
 * テーブル定義出力に関するリポジトリの基底クラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public abstract class AbstractTableDefinitionRepository implements TableDefinitionRepository {

    private final String baseSqlPath;

    /**
     * コンストラクタ
     */
    protected AbstractTableDefinitionRepository(DatabaseType databaseType) {
        this.baseSqlPath = "com.export_table_definition.domain.repository." + databaseType.getName() + ".TableDefinitionRepository.";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BaseInfoEntity selectBaseInfo() {
        try (SqlSession session = MyBatisSqlSessionFactory.openSession()) {
            final BaseInfoDto dto = session.selectOne(baseSqlPath + "selectBaseInfo");
            return dto.toEntity();
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<AllTableEntity> selectAllTableInfo(List<String> schemaList, List<String> tableList) {
        return selectTableDefinition(schemaList, tableList, "selectAllTableInfo", AllTableDto::toEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AllColumnEntity> selectAllColumnInfo(List<String> schemaList, List<String> tableList) {
        return selectTableDefinition(schemaList, tableList, "selectAllColumnInfo", AllColumnDto::toEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AllIndexEntity> selectAllIndexInfo(List<String> schemaList, List<String> tableList) {
        return selectTableDefinition(schemaList, tableList, "selectAllIndexInfo", AllIndexDto::toEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AllConstraintEntity> selectAllConstraintInfo(List<String> schemaList, List<String> tableList) {
        return selectTableDefinition(schemaList, tableList, "selectAllConstraintInfo", AllConstraintDto::toEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AllForeignkeyEntity> selectAllForeignkeyInfo(List<String> schemaList, List<String> tableList) {
        return selectTableDefinition(schemaList, tableList, "selectAllForeignkeyInfo", AllForeignkeyDto::toEntity);
    }
    
    private <D, E> List<E> selectTableDefinition(List<String> schemaList, List<String> tableList, String sqlId,
            Function<D, E> mapper) {
        try (SqlSession session = MyBatisSqlSessionFactory.openSession()) {
            final Map<String, Object> param = new HashMap<>();
            param.put("schemaList", schemaList);
            param.put("tableList", tableList);
            final List<D> dtoList = session.selectList(baseSqlPath + sqlId, param);
            return makeEntityList(dtoList, mapper);
        }
    }
}
