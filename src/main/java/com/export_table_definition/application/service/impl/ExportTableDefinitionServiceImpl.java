package com.export_table_definition.application.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.export_table_definition.application.service.ExportTableDefinitionService;
import com.export_table_definition.domain.repository.TableDefinitionRepository;
import com.export_table_definition.domain.repository.vo.AllColumnVo;
import com.export_table_definition.domain.repository.vo.AllForeignkeyVo;
import com.export_table_definition.domain.repository.vo.AllIndexVo;
import com.export_table_definition.domain.repository.vo.AllTableVo;
import com.export_table_definition.domain.repository.vo.BaseInfoVo;
import com.export_table_definition.domain.writer.TableDefinitionWriter;
import com.export_table_definition.infrastructure.log.Log4J2;
import com.export_table_definition.infrastructure.mybatis.MyBatisSqlSessionFactory;

public class ExportTableDefinitionServiceImpl implements ExportTableDefinitionService {
	private final SqlSessionFactory sqlSessionFactory = MyBatisSqlSessionFactory.getSqlSessionFactory();
	private final Log4J2 logger = Log4J2.getInstance();
	private final TableDefinitionWriter writer;

	
	public ExportTableDefinitionServiceImpl(TableDefinitionWriter writer) {
		this.writer = writer;
	}
	
	@Override
	public void exportTableDefinition() {
		logger.logInfo("[START]exportTableDefinition");
		
		makeDirectory("./output");
		makeDirectory("./output/tables");

		try (final SqlSession session = sqlSessionFactory.openSession()) {
			final TableDefinitionRepository mapper = session.getMapper(TableDefinitionRepository.class);
			final BaseInfoVo baseVo = mapper.selectBaseInfo();
			final List<AllTableVo> tableVoList = mapper.selectAllTableInfo();
			final List<AllColumnVo> columnVoList = mapper.selectAllColumnInfo();
			final List<AllIndexVo> indexVoList = mapper.selectAllIndexInfo();
			final List<AllForeignkeyVo> foreignkeyVoList = mapper.selectAllForeignkeyInfo();
			
			writer.writeTableDefinitionList(tableVoList, baseVo, new File("./output/tableList.md"));
			
			for (AllTableVo tableVo: tableVoList) {
				makeDirectory("./output/tables/" + tableVo.getSchemaName());
				final String filePath = "./output/tables/" + tableVo.getSchemaName() + "/" + tableVo.getPhysicalTableName()  + ".md";
				writer.writeTableDefinition(tableVo, baseVo, columnVoList, indexVoList, foreignkeyVoList, new File(filePath));
				logger.logDebug(String.format("exportTableDefinition complete. [filePath=%s]", filePath));
			}
		}
		logger.logInfo("[End]exportTableDefinition");
	}
	
	private void makeDirectory(String strFilePath) {
		final Path path = Paths.get(strFilePath);
		if (Files.exists(path)) return;
		try {
			Files.createDirectory(path);
		} catch (IOException e) {
			logger.logError(e);;
		}
	}
}
