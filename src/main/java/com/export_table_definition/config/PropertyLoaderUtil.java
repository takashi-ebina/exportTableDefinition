package com.export_table_definition.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

/**
 * プロパティファイルに関するユーティリティクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class PropertyLoaderUtil {

    private static final Map<String, ResourceBundle> CACHE = new ConcurrentHashMap<>();
    
    /**
     * コンストラクタ（インスタンス化不可）
     */
    private PropertyLoaderUtil() {
    }

    /**
     * プロパティファイルの読み込みを行うメソッド
     * 
     * @param fileName プロパティファイルのファイル名
     * @param key      取得するキー
     * @return キーに対応する値
     */
    public static String getString(String fileName, String key) {
        return getResourceBundle(fileName).getString(key);
    }
    
    /**
     * プロパティファイルの読み込みを行うメソッド（カンマ区切りの値をリストで取得）
     * 
     * @param fileName プロパティファイルのファイル名
     * @param key      取得するキー
     * @return キーに対応するカンマ区切りの値を分割したリスト
     */
    public static List<String> getList(String fileName, String key) {
        return Arrays.stream(getString(fileName, key).split(","))
                .filter(s -> !s.isBlank())
                .toList();
    }
    
    /**
     * プロパティファイルの読み込みを行うメソッド（キャッシュを利用）
     * 
     * @param fileName プロパティファイルのファイル名
     * @return プロパティファイルを読み込んだResourceBundleオブジェクト
     */
    public static ResourceBundle getResourceBundle(String fileName) {
        return CACHE.computeIfAbsent(fileName, f -> {
            try {
                URLClassLoader urlLoader = new URLClassLoader(
                        new URL[]{getPropertiesFileDir().toURI().toURL()});
                return ResourceBundle.getBundle(f, Locale.JAPAN, urlLoader);
            } catch (IOException e) {
                throw new RuntimeException("Failed to get the URL of the property file directory.", e);
            }
        });
    }

    private static File getPropertiesFileDir() throws FileNotFoundException {
        Path p1 = Paths.get("conf");
        if (Files.exists(p1)) {
            // 実行可能Jarファイルから実行した場合
            return p1.toFile();
        }
        Path p2 = Paths.get("src", "main", "resources", "conf");
        if (Files.exists(p2)) {
            // プロジェクトから実行した場合
            return p2.toFile();
        }
        throw new FileNotFoundException("conf directory does not exist.");
    }
}
