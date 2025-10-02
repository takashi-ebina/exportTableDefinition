package com.export_table_definition.domain.model.collection;

import java.util.List;
import java.util.Map;

import com.export_table_definition.domain.model.entity.ForeignkeyEntity;
import com.export_table_definition.domain.model.value.TableKey;
/**
 * 外部キー情報の集合を扱うクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public final class Foreignkeys extends AbstractEntities<ForeignkeyEntity> {
    private Foreignkeys(Map<TableKey, List<ForeignkeyEntity>> byKey) {
        super(byKey);
    }

    public static Foreignkeys of(List<ForeignkeyEntity> list) {
        return new Foreignkeys(index(list, c -> TableKey.of(c.schemaName(), c.tableName())));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected TableKey extractKey(ForeignkeyEntity e) {
        return TableKey.of(e.schemaName(), e.tableName());
    }
}
