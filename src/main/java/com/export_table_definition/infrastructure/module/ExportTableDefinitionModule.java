package com.export_table_definition.infrastructure.module;

import com.export_table_definition.application.usecase.ExportTableDefinitionUsecase;
import com.export_table_definition.domain.repository.TableDefinitionRepository;
import com.export_table_definition.domain.service.writer.TableDefinitionWriter;
import com.export_table_definition.infrastructure.mybatis.repository.MyBatisTableDefinitionRepository;
import com.google.inject.AbstractModule;

/**
 * 依存関係を管理するクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class ExportTableDefinitionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TableDefinitionRepository.class).to(MyBatisTableDefinitionRepository.class);
        bind(TableDefinitionWriter.class);
        bind(ExportTableDefinitionUsecase.class);
    }
}
