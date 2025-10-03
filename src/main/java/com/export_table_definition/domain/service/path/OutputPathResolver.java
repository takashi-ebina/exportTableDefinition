package com.export_table_definition.domain.service.path;

import java.nio.file.Path;

import com.export_table_definition.domain.model.entity.BaseInfoEntity;
import com.export_table_definition.domain.model.entity.TableEntity;

/**
 * テーブル定義および一覧出力用のパス生成戦略インタフェース <br>
 * 物理レイアウト（ディレクトリ構造・ファイル命名規則）を抽象化する
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public interface OutputPathResolver {

    /**
     * テーブル定義書の出力ディレクトリを返す。 <br>
     * 例: {base}/{DB名}/{スキーマ名}/{テーブル種別}/
     * 
     * @param baseInfo 基本情報エンティティ
     * @param table テーブルエンティティ
     * @param baseOutputDir 基本出力ディレクトリ
     * @return テーブル定義書の出力ディレクトリパス
     */
    Path resolveTableDefinitionDirectory(BaseInfoEntity baseInfo, TableEntity table, Path baseOutputDir);

    /**
     * テーブル定義書の出力ファイルパスを返す。 <br>
     * 例: {base}/{DB名}/{スキーマ名}/{テーブル種別}/{物理テーブル名}.md
     * 
     * @param baseInfo 基本情報エンティティ
     * @param table テーブルエンティティ
     * @param baseOutputDir 基本出力ディレクトリ
     * @return テーブル定義書の出力ファイルパス
     */
    Path resolveTableDefinitionFile(BaseInfoEntity baseInfo, TableEntity table, Path baseOutputDir);

    /**
     * テーブル一覧（単一ファイルモード）のパス。 <br>
     * 例: {base}/tableList_{DB名}.md
     * 
     * @param baseInfo 基本情報エンティティ
     * @param baseOutputDir 基本出力ディレクトリ
     * @return テーブル一覧ファイルのパス
     */
    Path resolveTableListFile(BaseInfoEntity baseInfo, Path baseOutputDir);

    /**
     * テーブル一覧（分割ページモード）のパス。 <br>
     * 例: {base}/tableList_{DB名}_{pageIndex}.md
     * 
     * @param baseInfo 基本情報エンティティ
     * @param baseOutputDir 基本出力ディレクトリ
     * @param pageIndex ページインデックス（1始まり）
     * @return テーブル一覧ファイルのパス
     */
    Path resolveTableListFile(BaseInfoEntity baseInfo, Path baseOutputDir, int pageIndex);
}