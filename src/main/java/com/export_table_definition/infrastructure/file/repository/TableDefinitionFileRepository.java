package com.export_table_definition.infrastructure.file.repository;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.export_table_definition.domain.repository.FileRepository;
import com.export_table_definition.infrastructure.file.TableDefinitionBufferedWriter;

public class TableDefinitionFileRepository implements FileRepository {

    @Override
    public void writeFile(Path filePath, List<String> contents) {
        try (final var bw = new TableDefinitionBufferedWriter(filePath)) {
            contents.stream().forEach(content -> bw.write(content));
        }
    }

    @Override
    public void createDirectory(String targetFilePath) {
        try {
            Files.createDirectories(Paths.get(targetFilePath));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
