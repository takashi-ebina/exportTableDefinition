package com.export_table_definition.domain.service.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;

/**
 * BufferedWriterをwrapしたクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class TableDefinitionBufferedWriterWrap extends BufferedWriter {

	/**
	 * コンストラクタ
	 * 
	 * @param out Writerオブジェクト
	 */
	public TableDefinitionBufferedWriterWrap(Writer out) {
		super(out);
	}
	
	/**
	 * writeメソッド<br>
	 * <br>
	 * Streams API で利用するために例外発生時に非検査例外にラップする
	 * 
	 * @param text 文字列
	 */
	@Override
	public void write(String text) {
		try {
			super.write(text);
		} catch (IOException e) {
			// Streams API で利用するために非検査例外にラップする
			throw new UncheckedIOException(e);
		}
	}

}
