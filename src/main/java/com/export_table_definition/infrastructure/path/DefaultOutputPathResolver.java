package com.export_table_definition.infrastructure.path;

import java.nio.file.Path;
import java.util.Objects;

import com.export_table_definition.domain.model.entity.BaseInfoEntity;
import com.export_table_definition.domain.model.entity.TableEntity;
import com.export_table_definition.domain.service.path.OutputPathResolver;

/**
 * 既存仕様に基づくデフォルトの出力パス解決実装。
 */
public class DefaultOutputPathResolver implements OutputPathResolver {

    private static final String TABLE_LIST_FILENAME_PATTERN = "tableList_%s.md";
    private static final String TABLE_LIST_PAGED_FILENAME_PATTERN = "tableList_%s_%d.md";

    @Override
    public Path resolveTableDefinitionDirectory(BaseInfoEntity baseInfo, TableEntity table, Path baseOutputDir) {
        Objects.requireNonNull(baseInfo);
        Objects.requireNonNull(table);
        return baseOutputDir
                .resolve(baseInfo.dbName())
                .resolve(table.schemaName())
                .resolve(table.tableType());
    }

    @Override
    public Path resolveTableDefinitionFile(BaseInfoEntity baseInfo, TableEntity table, Path baseOutputDir) {
        return resolveTableDefinitionDirectory(baseInfo, table, baseOutputDir)
                .resolve(table.physicalTableName() + ".md");
    }

    @Override
    public Path resolveTableListFile(BaseInfoEntity baseInfo, Path baseOutputDir) {
        return baseOutputDir.resolve(String.format(TABLE_LIST_FILENAME_PATTERN, baseInfo.dbName()));
    }

    @Override
    public Path resolveTableListFile(BaseInfoEntity baseInfo, Path baseOutputDir, int pageIndex) {
        return baseOutputDir.resolve(String.format(TABLE_LIST_PAGED_FILENAME_PATTERN, baseInfo.dbName(), pageIndex));
    }
}