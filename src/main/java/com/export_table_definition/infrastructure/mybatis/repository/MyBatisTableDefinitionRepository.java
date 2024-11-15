package com.export_table_definition.infrastructure.mybatis.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

/**
 * テーブル定義出力に関するリポジトリクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class MyBatisTableDefinitionRepository implements TableDefinitionRepository {

    private final String connectionDbName;

    /**
     * コンストラクタ
     */
    public MyBatisTableDefinitionRepository() {
        this.connectionDbName = MyBatisSqlSessionFactory.getConnectionDbName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseInfoEntity selectBaseInfo() {
        try (SqlSession session = MyBatisSqlSessionFactory.openSession()) {
            final BaseInfoDto dto = session.selectOne(
                    "com.export_table_definition.domain.repository." + connectionDbName
                            + ".TableDefinitionRepository.selectBaseInfo");
            return makeBaseInfoEntity(dto);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AllTableEntity> selectAllTableInfo(List<String> schemaList, List<String> tableList) {
        try (SqlSession session = MyBatisSqlSessionFactory.openSession()) {
            // FIXME OracleのLong型をJDBCDriverでは扱えないため、OracleにおいてView/materialized_viewのソースは表示不可
            // 参考リンク：https://support.oracle.com/knowledge/Middleware/832903_1.html
            final Map<String, Object> param = new HashMap<>();
            param.put("schemaList", schemaList);
            param.put("tableList", tableList);
            final List<AllTableDto> dtoList = session.selectList(
                    "com.export_table_definition.domain.repository." + connectionDbName
                            + ".TableDefinitionRepository.selectAllTableInfo",
                    param);
            return makeAllTableEntityList(dtoList);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AllColumnEntity> selectAllColumnInfo(List<String> schemaList, List<String> tableList) {
        try (SqlSession session = MyBatisSqlSessionFactory.openSession()) {
            // FIXME OracleのLong型をJDBCDriverでは扱えないため、Oracleにおいてデフォルト定義は表示不可
            // 参考リンク：https://support.oracle.com/knowledge/Middleware/832903_1.html
            final Map<String, Object> param = new HashMap<>();
            param.put("schemaList", schemaList);
            param.put("tableList", tableList);
            final List<AllColumnDto> dtoList = session.selectList(
                    "com.export_table_definition.domain.repository." + connectionDbName
                            + ".TableDefinitionRepository.selectAllColumnInfo",
                    param);
            return makeAllColumnEntityList(dtoList);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AllIndexEntity> selectAllIndexInfo(List<String> schemaList, List<String> tableList) {
        try (SqlSession session = MyBatisSqlSessionFactory.openSession()) {
            final Map<String, Object> param = new HashMap<>();
            param.put("schemaList", schemaList);
            param.put("tableList", tableList);
            final List<AllIndexDto> dtoList = session.selectList(
                    "com.export_table_definition.domain.repository." + connectionDbName
                            + ".TableDefinitionRepository.selectAllIndexInfo",
                    param);
            return makeAllIndexEntityList(dtoList);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AllConstraintEntity> selectAllConstraintInfo(List<String> schemaList, List<String> tableList) {
        try (SqlSession session = MyBatisSqlSessionFactory.openSession()) {
            final Map<String, Object> param = new HashMap<>();
            param.put("schemaList", schemaList);
            param.put("tableList", tableList);
            // FIXME OracleのLong型をJDBCDriverでは扱えないため、Oracleにおいて制約定義は表示不可
            // 参考リンク：https://support.oracle.com/knowledge/Middleware/832903_1.html
            final List<AllConstraintDto> dtoList = session.selectList(
                    "com.export_table_definition.domain.repository." + connectionDbName
                            + ".TableDefinitionRepository.selectAllConstraintInfo",
                    param);
            return makeAllConstraintEntityList(dtoList);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AllForeignkeyEntity> selectAllForeignkeyInfo(List<String> schemaList, List<String> tableList) {
        try (SqlSession session = MyBatisSqlSessionFactory.openSession()) {
            final Map<String, Object> param = new HashMap<>();
            param.put("schemaList", schemaList);
            param.put("tableList", tableList);
            final List<AllForeignkeyDto> dtoList = session.selectList(
                    "com.export_table_definition.domain.repository." + connectionDbName
                            + ".TableDefinitionRepository.selectAllForeignkeyInfo",
                    param);
            return makeAllForeignkeyEntityList(dtoList);
        }
    }

    private BaseInfoEntity makeBaseInfoEntity(BaseInfoDto dto) {
        return new BaseInfoEntity(dto.getDbName(), dto.getBaseInfo());
    }

    private List<AllTableEntity> makeAllTableEntityList(List<AllTableDto> dtoList) {
        return dtoList.stream()
                .map(dto -> new AllTableEntity(dto.getSchemaName(), dto.getLogicalTableName(),
                        dto.getPhysicalTableName(), dto.getTableType(), dto.getTableInfoList(),
                        dto.getTableInfo(), dto.getDefinition()))
                .collect(Collectors.toList());
    }

    private List<AllColumnEntity> makeAllColumnEntityList(List<AllColumnDto> dtoList) {
        return dtoList.stream()
                .map(dto -> new AllColumnEntity(dto.getSchemaName(), dto.getTableName(),
                        dto.getColumnInfo()))
                .collect(Collectors.toList());
    }

    private List<AllIndexEntity> makeAllIndexEntityList(List<AllIndexDto> dtoList) {
        return dtoList.stream()
                .map(dto -> new AllIndexEntity(dto.getSchemaName(), dto.getTableName(),
                        dto.getIndexInfo()))
                .collect(Collectors.toList());
    }

    private List<AllConstraintEntity> makeAllConstraintEntityList(List<AllConstraintDto> dtoList) {
        return dtoList.stream()
                .map(dto -> new AllConstraintEntity(dto.getSchemaName(), dto.getTableName(),
                        dto.getConstraintInfo()))
                .collect(Collectors.toList());
    }

    private List<AllForeignkeyEntity> makeAllForeignkeyEntityList(List<AllForeignkeyDto> dtoList) {
        return dtoList.stream()
                .map(dto -> new AllForeignkeyEntity(dto.getSchemaName(), dto.getTableName(),
                        dto.getForeignkeyInfo()))
                .collect(Collectors.toList());
    }
}
