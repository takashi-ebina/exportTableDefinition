package com.export_table_definition;

import java.util.List;

import com.export_table_definition.config.PropertyLoader;
import com.export_table_definition.config.module.ExportTableDefinitionModule;
import com.export_table_definition.presentation.ExportTableDefinitionController;
import com.export_table_definition.presentation.dto.ResultDto;
import com.google.inject.Guice;

/**
 * テーブル定義出力処理を呼び出すクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public class ExportTableDefinition {

    /**
     * テーブル定義出力処理のエントリーポイントメソッド
     * 
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        // プロパティファイルの読み込み
        final List<String> schemaList = PropertyLoader.getList("ExportTableDefinition", "schema");
        final List<String> tableList = PropertyLoader.getList("ExportTableDefinition", "table");
        final String outputPath = PropertyLoader.getString("ExportTableDefinition", "outputPath");

        // 処理開始メッセージ出力
        System.out.println("Starting output of table definition document.");
        System.out.println("Please wait a moment ...");
        System.out.println("");

        // テーブル定義出力処理実行
        final ExportTableDefinitionController controller = Guice.createInjector(new ExportTableDefinitionModule())
                .getInstance(ExportTableDefinitionController.class);
        final ResultDto resultDto = controller.execute(schemaList, tableList, outputPath);

        // 処理終了メッセージ出力
        System.out.println(resultDto.getResultMessage());
    }
}
