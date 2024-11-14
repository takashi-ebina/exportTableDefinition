package com.export_table_definition.infrastructure.util;

import java.io.IOException;
import java.nio.file.Files;
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
	 * @param filePath ディレクトリ作成対象のファイルパス
	 */
	public static void createDirectory(String filePath) {
		try {
			Files.createDirectories(Paths.get(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
