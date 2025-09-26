package com.export_table_definition.config.module;

import com.export_table_definition.application.ExportTableDefinitionUsecase;
import com.export_table_definition.domain.repository.FileRepository;
import com.export_table_definition.domain.repository.TableDefinitionRepository;
import com.export_table_definition.domain.service.writer.TableDefinitionWriterDomainService;
import com.export_table_definition.infrastructure.db.MyBatisSqlSessionFactory;
import com.export_table_definition.infrastructure.file.repository.TableDefinitionFileRepository;
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
        bind(TableDefinitionRepository.class).to(MyBatisSqlSessionFactory.getConnectionDbName().getRepositoryClass());
        bind(FileRepository.class).to(TableDefinitionFileRepository.class);
        bind(TableDefinitionWriterDomainService.class);
        bind(ExportTableDefinitionUsecase.class);
    }
}
