package com.export_table_definition.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * プロパティファイルに関するユーティリティクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class PropertyLoader {

    private static final Map<String, ResourceBundle> CACHE = new ConcurrentHashMap<>();

    /**
     * コンストラクタ（インスタンス化不可）
     */
    private PropertyLoader() {
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
        return Arrays.stream(getString(fileName, key).split(",")).filter(s -> !s.isBlank()).toList();
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
                return ResourceBundle.getBundle(f, Locale.JAPAN,
                        new URLClassLoader(new URL[] { getPropertiesFileDir().toURI().toURL() }));
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to get the URL of the property file directory.", e);
            }
        });
    }

    /**
     * プロパティファイルが存在するディレクトリを取得するメソッド
     * 
     * @return プロパティファイルが存在するディレクトリのFileオブジェクト
     * @throws FileNotFoundException プロパティファイルが存在するディレクトリが見つからない場合
     */
    private static File getPropertiesFileDir() throws FileNotFoundException {
        return Stream.of(Path.of("conf"), Path.of("src", "main", "resources", "conf")).filter(Files::exists).findFirst()
                .map(Path::toFile).orElseThrow(() -> new FileNotFoundException("conf directory does not exist."));
    }
}
