package com.export_table_definition.domain.service.path;

import java.nio.file.Path;

import com.export_table_definition.domain.model.entity.BaseInfoEntity;
import com.export_table_definition.domain.model.entity.TableEntity;

/**
 * テーブル定義および一覧出力用のパス生成戦略インタフェース。
 * 物理レイアウト（ディレクトリ構造・ファイル命名規則）を抽象化する。
 */
public interface OutputPathResolver {

    /**
     * テーブル定義書の出力ディレクトリを返す。
     * 例: {base}/{DB名}/{スキーマ名}/{テーブル種別}/
     */
    Path resolveTableDefinitionDirectory(BaseInfoEntity baseInfo, TableEntity table, Path baseOutputDir);

    /**
     * テーブル定義書の出力ファイルパスを返す。
     * 例: {base}/{DB名}/{スキーマ名}/{テーブル種別}/{物理テーブル名}.md
     */
    Path resolveTableDefinitionFile(BaseInfoEntity baseInfo, TableEntity table, Path baseOutputDir);

    /**
     * テーブル一覧（単一ファイルモード）のパス。
     * 例: {base}/tableList_{DB名}.md
     */
    Path resolveTableListFile(BaseInfoEntity baseInfo, Path baseOutputDir);

    /**
     * テーブル一覧（分割ページモード）のパス。
     * 例: {base}/tableList_{DB名}_{pageIndex}.md
     */
    Path resolveTableListFile(BaseInfoEntity baseInfo, Path baseOutputDir, int pageIndex);
}