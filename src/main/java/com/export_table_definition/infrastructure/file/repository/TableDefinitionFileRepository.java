package com.export_table_definition.infrastructure.file.repository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.export_table_definition.domain.repository.FileRepository;
import com.export_table_definition.infrastructure.file.TableDefinitionBufferedWriter;

/**
 * ファイル操作に関するリポジトリ実装クラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class TableDefinitionFileRepository implements FileRepository {

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeFile(Path filePath, List<String> contents) {
        try (final var bw = new TableDefinitionBufferedWriter(filePath)) {
            contents.stream().forEach(content -> bw.write(content));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createDirectory(Path filePath) {
        try {
            Files.createDirectories(filePath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
