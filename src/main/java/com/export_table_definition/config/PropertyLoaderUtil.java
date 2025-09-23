package com.export_table_definition.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * プロパティファイルに関するユーティリティクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class PropertyLoaderUtil {

    /**
     * コンストラクタ（インスタンス化不可）
     */
    private PropertyLoaderUtil() {
    }

    /**
     * プロパティファイルの読み込みを行うメソッド
     * 
     * @param fileName プロパティファイルのファイル名
     * @return プロパティファイルを読み込んだResourceBundleオブジェクト
     */
    public static ResourceBundle getResourceBundle(String fileName) {
        final URLClassLoader urlLoader;
        try {
            urlLoader = new URLClassLoader(new URL[] { getPropertiesFileDir().toURI().toURL() });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResourceBundle.getBundle(fileName, Locale.JAPAN, urlLoader);

    }

    private static File getPropertiesFileDir() throws FileNotFoundException {
        Path p1 = Paths.get("conf");
        if (Files.exists(p1)) {
            // 実行可能Jarファイルから実行した場合
            return p1.toFile();
        }
        Path p2 = Paths.get("src\\main\\resources\\conf");
        if (Files.exists(p2)) {
            // プロジェクトから実行した場合
            return p2.toFile();
        }
        throw new FileNotFoundException("conf directory dose not exist");
    }
}
