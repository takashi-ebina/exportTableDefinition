package com.export_table_definition.domain.model.collection;

import java.util.List;
import java.util.Map;

import com.export_table_definition.domain.model.entity.ForeignKeyEntity;
import com.export_table_definition.domain.model.value.TableKey;
/**
 * 外部キー情報の集合を扱うクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public final class ForeignKeys extends AbstractEntities<ForeignKeyEntity> {
    private ForeignKeys(Map<TableKey, List<ForeignKeyEntity>> byKey) {
        super(byKey);
    }

    public static ForeignKeys of(List<ForeignKeyEntity> list) {
        return new ForeignKeys(index(list, c -> TableKey.of(c.schemaName(), c.tableName())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TableKey extractKey(ForeignKeyEntity e) {
        return TableKey.of(e.schemaName(), e.tableName());
    }
}
