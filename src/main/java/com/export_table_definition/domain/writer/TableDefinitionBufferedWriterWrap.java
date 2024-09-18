package com.export_table_definition.domain.writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;

public class TableDefinitionBufferedWriterWrap extends BufferedWriter {

	public TableDefinitionBufferedWriterWrap(Writer out) {
		super(out);
	}
	
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
