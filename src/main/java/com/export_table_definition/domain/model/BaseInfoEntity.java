package com.export_table_definition.domain.model;

import java.nio.file.Path;

/**
 * データベースの基本情報に関するrecordクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public record BaseInfoEntity(String dbName, String baseInfo) {
    
    /**
     * テーブル一覧ファイルのパスを取得するメソッド
     * 
     * @param outputDirectoryPath 出力ディレクトリのパス
     * @param fileIndex ファイルインデックス（0の場合はインデックスを付与しない）
     * @return テーブル一覧ファイルのパス
     */
    public Path toTableListFile(Path outputDirectoryPath, int fileIndex) {
        return outputDirectoryPath.resolve("tableList" + "_" + dbName + (fileIndex > 0 ? "_" + fileIndex : "") + ".md");
    }
    
    /**
     * テーブル一覧ファイルのパスを取得するメソッド
     * 
     * @param outputDirectoryPath 出力ディレクトリのパス
     * @return テーブル一覧ファイルのパス
     */
    public Path toTableListFile(Path outputDirectoryPath) {
        return toTableListFile(outputDirectoryPath, 0);
    }

}
