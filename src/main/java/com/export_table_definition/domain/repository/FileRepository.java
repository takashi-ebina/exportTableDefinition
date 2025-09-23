package com.export_table_definition.domain.repository;

import java.nio.file.Path;
import java.util.List;

/**
 * ファイル操作に関するリポジトリインターフェース
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public interface FileRepository {
    /**
     * ファイルに書き込むメソッド
     * 
     * @param filePath 書き込み先のファイルパス
     * @param contents 書き込む内容のリスト
     */
    public void writeFile(Path filePath, List<String> contents);
    
    /**
     * ディレクトリを作成するメソッド
     * 
     * @param filePath 作成するディレクトリのパス
     */
    public void createDirectory(Path filePath);

}
