package com.export_table_definition.presentation;

import java.util.List;

import com.export_table_definition.application.ExportTableDefinitionUsecase;
import com.export_table_definition.infrastructure.log.Log4J2;
import com.export_table_definition.presentation.dto.ResultDto;
import com.export_table_definition.presentation.type.ProcessResult;
import com.google.inject.Inject;

/**
 * テーブル定義出力処理のコントローラークラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class ExportTableDefinitionController {

    private final static Log4J2 logger = Log4J2.getInstance();
    private final ExportTableDefinitionUsecase exportTableDefinitionUsecase;

    /**
     * コンストラクタ
     * 
     * @param exportTableDefinitionUsecase テーブル定義出力に関するユースケースクラス
     */
    @Inject
    public ExportTableDefinitionController(ExportTableDefinitionUsecase exportTableDefinitionUsecase) {
        this.exportTableDefinitionUsecase = exportTableDefinitionUsecase;
    }

    /**
     * コントローラーメソッド
     * 
     * @param schemaList テーブル定義出力対象のスキーマのリスト
     * @param tableList テーブル定義出力対象のテーブルのリスト
     * @param outputPath テーブル定義出力の出力先のパス
     * @return 処理結果
     */
    public ResultDto execute(List<String> schemaList, List<String> tableList, String outputPath) {
        logger.logInfo("[START] exportTableDefinition");
        try {
            exportTableDefinitionUsecase.exportTableDefinition(schemaList, tableList, outputPath);
        } catch (Exception e) {
            logger.logError(e);
            return new ResultDto(ProcessResult.FAIL,
                    String.format("Failed to output table definition document. %s [errmsg]:%s", 
                            System.getProperty("line.separator"), e.getMessage()));
        }
        logger.logInfo("[ END ] exportTableDefinition");
        return new ResultDto(ProcessResult.SUCCESS, "Table definition output is complete.");
    }

}
