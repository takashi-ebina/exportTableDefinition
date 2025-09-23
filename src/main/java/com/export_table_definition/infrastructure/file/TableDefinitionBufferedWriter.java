package com.export_table_definition.infrastructure.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;

/**
 * テーブル定義書き込み用のBufferedWriterを扱うクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class TableDefinitionBufferedWriter implements AutoCloseable {

    private final BufferedWriter bw;

    /**
     * コンストラクタ
     * @param path 書き込み先のファイルパス
     */
    public TableDefinitionBufferedWriter(Path path) {
        try {
            this.bw = new BufferedWriter(new FileWriter(path.toFile(), false));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 書き込み処理を行うメソッド<br>
     * {@link BufferedWriter#write(String)}をラップし、例外時にUncheckedIOExceptionをスローする
     * 
     * @param content 書き込む内容
     */
    public void write(String content) {
        try {
            bw.write(content);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * クローズ処理を行うメソッド
     */
    @Override
    public void close() {
        try {
            bw.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
