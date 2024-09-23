package com.export_table_definition.infrastructure.mybatis.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.export_table_definition.domain.model.AllColumnEntity;
import com.export_table_definition.domain.model.AllForeignkeyEntity;
import com.export_table_definition.domain.model.AllIndexEntity;
import com.export_table_definition.domain.model.AllTableEntity;
import com.export_table_definition.domain.model.BaseInfoEntity;
import com.export_table_definition.domain.repository.TableDefinitionRepository;
import com.export_table_definition.infrastructure.mybatis.MyBatisSqlSessionFactory;
import com.export_table_definition.infrastructure.mybatis.repository.dto.AllColumnDto;
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

	private final SqlSessionFactory sqlSessionFactory;

	/**
	 * コンストラクタ
	 */
	public MyBatisTableDefinitionRepository() {
		this.sqlSessionFactory = MyBatisSqlSessionFactory.getSqlSessionFactory();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BaseInfoEntity selectBaseInfo() {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			final BaseInfoDto dto = session.selectOne(
					"com.export_table_definition.domain.repository.TableDefinitionRepository.selectBaseInfo");
			return makeBaseInfoEntity(dto);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AllTableEntity> selectAllTableInfo() {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			final List<AllTableDto> dtoList = session.selectList(
					"com.export_table_definition.domain.repository.TableDefinitionRepository.selectAllTableInfo");
			return makeAllTableEntityList(dtoList);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AllColumnEntity> selectAllColumnInfo(List<String> schemaList, List<String> tableList) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> param = new HashMap<>();
			param.put("schemaList", schemaList);
			param.put("tableList", tableList);
			List<AllColumnDto> dtoList = session.selectList(
					"com.export_table_definition.domain.repository.TableDefinitionRepository.selectAllColumnInfo", param);
			return makeAllColumnEntityList(dtoList);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AllIndexEntity> selectAllIndexInfo(List<String> schemaList, List<String> tableList) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> param = new HashMap<>();
			param.put("schemaList", schemaList);
			param.put("tableList", tableList);
			List<AllIndexDto> dtoList = session.selectList(
					"com.export_table_definition.domain.repository.TableDefinitionRepository.selectAllIndexInfo", param);
			return makeAllIndexEntityList(dtoList);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<AllForeignkeyEntity> selectAllForeignkeyInfo(List<String> schemaList, List<String> tableList) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			Map<String, Object> param = new HashMap<>();
			param.put("schemaList", schemaList);
			param.put("tableList", tableList);
			List<AllForeignkeyDto> dtoList = session.selectList(
					"com.export_table_definition.domain.repository.TableDefinitionRepository.selectAllForeignkeyInfo", param);
			return makeBAllForeignkeyEntityList(dtoList);
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

	private List<AllForeignkeyEntity> makeBAllForeignkeyEntityList(List<AllForeignkeyDto> dtoList) {
	    return dtoList.stream()
	            .map(dto -> new AllForeignkeyEntity(dto.getSchemaName(), dto.getTableName(),
	                    dto.getForeignkeyInfo()))
	            .collect(Collectors.toList());
	}

}
