package com.export_table_definition.domain.model.type;

/**
 * テーブルの種類を表す列挙型
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public enum TableType {
    /** テーブル */
    TABLE,
    /** ビュー */
    VIEW,
    /** マテリアライズドビュー */
    MATERIALIZED_VIEW;

    /**
     * 指定された文字列がビュータイプかどうかを判定する
     * 
     * @param raw 判定対象の文字列
     * @return ビュータイプの場合はtrue、そうでない場合はfalse
     */
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
