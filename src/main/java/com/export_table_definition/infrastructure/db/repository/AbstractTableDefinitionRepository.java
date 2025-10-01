package com.export_table_definition.infrastructure.db.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.ibatis.session.SqlSession;

import com.export_table_definition.domain.model.BaseInfoEntity;
import com.export_table_definition.domain.model.ColumnEntity;
import com.export_table_definition.domain.model.ConstraintEntity;
import com.export_table_definition.domain.model.ForeignkeyEntity;
import com.export_table_definition.domain.model.IndexEntity;
import com.export_table_definition.domain.model.TableEntity;
import com.export_table_definition.domain.repository.TableDefinitionRepository;
import com.export_table_definition.infrastructure.db.MyBatisSqlSessionFactory;
import com.export_table_definition.infrastructure.db.repository.dto.BaseInfoDto;
import com.export_table_definition.infrastructure.db.repository.dto.ColumnDto;
import com.export_table_definition.infrastructure.db.repository.dto.ConstraintDto;
import com.export_table_definition.infrastructure.db.repository.dto.ForeignkeyDto;
import com.export_table_definition.infrastructure.db.repository.dto.IndexDto;
import com.export_table_definition.infrastructure.db.repository.dto.TableDto;
import com.export_table_definition.infrastructure.db.type.DatabaseType;

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
     * 
     * @param databaseType データベースの種類
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
    public List<TableEntity> selectTableList(List<String> schemaList, List<String> tableList) {
        return selectTableDefinition(schemaList, tableList, "selectAllTableInfo", TableDto::toEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ColumnEntity> selectColumnList(List<String> schemaList, List<String> tableList) {
        return selectTableDefinition(schemaList, tableList, "selectAllColumnInfo", ColumnDto::toEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<IndexEntity> selectIndexList(List<String> schemaList, List<String> tableList) {
        return selectTableDefinition(schemaList, tableList, "selectAllIndexInfo", IndexDto::toEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ConstraintEntity> selectConstraintList(List<String> schemaList, List<String> tableList) {
        return selectTableDefinition(schemaList, tableList, "selectAllConstraintInfo", ConstraintDto::toEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignkeyEntity> selectForeignkeyList(List<String> schemaList, List<String> tableList) {
        return selectTableDefinition(schemaList, tableList, "selectAllForeignkeyInfo", ForeignkeyDto::toEntity);
    }
    
    private <D, E> List<E> selectTableDefinition(List<String> schemaList, List<String> tableList, String sqlId,
            Function<D, E> mapper) {
        final String sqlPath = baseSqlPath + sqlId;
        try (SqlSession session = MyBatisSqlSessionFactory.openSession()) {
            final Map<String, Object> param = new HashMap<>();
            param.put("schemaList", schemaList);
            param.put("tableList", tableList);
            final List<D> dtoList = session.selectList(sqlPath, param);
            return makeEntityList(dtoList, mapper);
        } catch (Exception e) {
            throw new RuntimeException("Failed to select: " + sqlPath, e);
        }
    }
}
