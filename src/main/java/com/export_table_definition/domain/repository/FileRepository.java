package com.export_table_definition.domain.repository;

import java.nio.file.Path;
import java.util.List;

public interface FileRepository {
    void writeFile(Path filePath, List<String> contents);
    
    void createDirectory(String targetFilePath);

}
