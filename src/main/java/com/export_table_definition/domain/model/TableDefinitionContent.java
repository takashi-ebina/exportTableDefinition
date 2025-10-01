package com.export_table_definition.domain.model;

import java.nio.file.Path;
import java.util.List;

import com.export_table_definition.domain.model.entity.BaseInfoEntity;
import com.export_table_definition.domain.model.entity.ColumnEntity;
import com.export_table_definition.domain.model.entity.ConstraintEntity;
import com.export_table_definition.domain.model.entity.ForeignkeyEntity;
import com.export_table_definition.domain.model.entity.IndexEntity;
import com.export_table_definition.domain.model.entity.TableEntity;
/**
 * テーブル定義出力に必要な情報をまとめたレコード
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public record TableDefinitionContent(BaseInfoEntity baseInfo, TableEntity table, List<ColumnEntity> columns,
        List<IndexEntity> indexes, List<ConstraintEntity> constraints, List<ForeignkeyEntity> foreignKeys,
        Path outputBaseDir) {

}