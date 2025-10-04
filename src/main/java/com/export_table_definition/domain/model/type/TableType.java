package com.export_table_definition.domain.model.type;

public enum TableType {
    TABLE, VIEW, MATERIALIZED_VIEW;

    public static boolean isViewType(String raw) {
        if (raw == null) {
            return false;
        }
        return switch (raw.toLowerCase()) {
        case "view", "materialized_view" -> true;
        default -> false;
        };
    }
}
