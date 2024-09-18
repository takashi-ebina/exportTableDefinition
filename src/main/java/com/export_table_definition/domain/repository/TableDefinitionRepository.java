package com.export_table_definition.domain.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.export_table_definition.domain.repository.vo.AllColumnVo;
import com.export_table_definition.domain.repository.vo.AllForeignkeyVo;
import com.export_table_definition.domain.repository.vo.AllIndexVo;
import com.export_table_definition.domain.repository.vo.AllTableVo;
import com.export_table_definition.domain.repository.vo.BaseInfoVo;

@Mapper
public interface TableDefinitionRepository {
	
	BaseInfoVo selectBaseInfo();
	
	List<AllTableVo> selectAllTableInfo();
	
	List<AllColumnVo> selectAllColumnInfo();
	
	List<AllIndexVo> selectAllIndexInfo();
	
	List<AllForeignkeyVo> selectAllForeignkeyInfo();
}
