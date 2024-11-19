package com.export_table_definition.infrastructure.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * ファイルに関するユーティリティクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class FileUtil {

    /**
     * コンストラクタ（インスタンス化不可）
     */
    private FileUtil() {
    }

    /**
     * ディレクトリ作成メソッド<br>
     * <br>
     * 親ディレクトリを含めて指定したパスの全てのディレクトリを作成する
     * 
     * @param targetFilePath ディレクトリ作成対象のファイルパス
     * @return ディレクトリを作成できたらtrue。それ以外の場合はfalseを返却
     */
    public static boolean createDirectory(String targetFilePath) {
        try {
            Files.createDirectories(Paths.get(targetFilePath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * ファイル作成メソッド<br>
     * <br>
     * 新しい空のファイルを作成する
     * 
     * @param filePath ファイル作成対象のファイルパス
     * @return 空ファイルを作成できたらtrue。それ以外の場合はfalseを返却
     */
    public static boolean safedCreateFile(String targetFilePath) {
        try {
            Path filePath = Paths.get(targetFilePath);
            if (Files.isDirectory(filePath)) {
                return false;
            }
            if (Files.exists(filePath)) {
                return false;
            }
            Files.createFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
