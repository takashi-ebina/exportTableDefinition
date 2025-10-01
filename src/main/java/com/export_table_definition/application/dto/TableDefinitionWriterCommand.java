package com.export_table_definition.application.dto;

import java.nio.file.Path;
import java.util.List;

import com.export_table_definition.domain.model.BaseInfoEntity;
import com.export_table_definition.domain.model.ColumnEntity;
import com.export_table_definition.domain.model.ConstraintEntity;
import com.export_table_definition.domain.model.ForeignkeyEntity;
import com.export_table_definition.domain.model.IndexEntity;
import com.export_table_definition.domain.model.TableEntity;

public record TableDefinitionWriterCommand(BaseInfoEntity baseInfo, TableEntity table, List<ColumnEntity> columns,
        List<IndexEntity> indexes, List<ConstraintEntity> constraints, List<ForeignkeyEntity> foreignKeys,
        Path outputBaseDir) {

}