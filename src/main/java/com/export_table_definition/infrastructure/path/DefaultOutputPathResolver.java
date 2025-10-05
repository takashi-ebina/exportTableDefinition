package com.export_table_definition.infrastructure.path;

import java.nio.file.Path;

import com.export_table_definition.domain.model.entity.BaseInfoEntity;
import com.export_table_definition.domain.model.entity.TableEntity;
import com.export_table_definition.domain.service.path.OutputPathResolver;

/**
 * 出力パス解決のデフォルト実装
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class DefaultOutputPathResolver implements OutputPathResolver {

    private static final String TABLE_LIST_FILENAME_PATTERN = "tableList_%s.md";
    private static final String TABLE_LIST_PAGED_FILENAME_PATTERN = "tableList_%s_%d.md";

    /**
     * {@inheritDoc}
     */
    @Override
    public Path resolveTableDefinitionDirectory(BaseInfoEntity baseInfo, TableEntity table, Path baseOutputDir) {
        return baseOutputDir.resolve(baseInfo.dbName()).resolve(table.schemaName()).resolve(table.tableType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path resolveTableDefinitionFile(BaseInfoEntity baseInfo, TableEntity table, Path baseOutputDir) {
        return resolveTableDefinitionDirectory(baseInfo, table, baseOutputDir)
                .resolve(table.physicalTableName() + ".md");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path resolveTableListFile(BaseInfoEntity baseInfo, Path baseOutputDir) {
        return baseOutputDir.resolve(String.format(TABLE_LIST_FILENAME_PATTERN, baseInfo.dbName()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path resolveTableListFile(BaseInfoEntity baseInfo, Path baseOutputDir, int pageIndex) {
        return baseOutputDir.resolve(String.format(TABLE_LIST_PAGED_FILENAME_PATTERN, baseInfo.dbName(), pageIndex));
    }

}