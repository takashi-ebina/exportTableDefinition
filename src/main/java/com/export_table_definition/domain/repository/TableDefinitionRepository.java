package com.export_table_definition.domain.repository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.export_table_definition.domain.model.entity.BaseInfoEntity;
import com.export_table_definition.domain.model.entity.ColumnEntity;
import com.export_table_definition.domain.model.entity.ConstraintEntity;
import com.export_table_definition.domain.model.entity.ForeignkeyEntity;
import com.export_table_definition.domain.model.entity.IndexEntity;
import com.export_table_definition.domain.model.entity.TableEntity;

/**
 * テーブル定義出力に関するリポジトリインターフェース
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public interface TableDefinitionRepository {

    /**
     * データベースの基本情報を取得するメソッド
     * 
     * @return データベースの基本情報
     */
    BaseInfoEntity selectBaseInfo();

    /**
     * データベースのテーブル情報を取得するメソッド
     * 
     * @param schemaList テーブル定義出力対象のスキーマのリスト
     * @param tableList テーブル定義出力対象のテーブルのリスト
     * @return データベースのテーブル情報
     */
    List<TableEntity> selectTableList(List<String> schemaList, List<String> tableList);

    /**
     * データベースのカラム情報を取得するメソッド
     * 
     * @param schemaList テーブル定義出力対象のスキーマのリスト
     * @param tableList テーブル定義出力対象のテーブルのリスト
     * @return データベースのカラム情報
     */
    List<ColumnEntity> selectColumnList(List<String> schemaList, List<String> tableList);

    /**
     * データベースのインデックス情報を取得するメソッド
     * 
     * @param schemaList テーブル定義出力対象のスキーマのリスト
     * @param tableList テーブル定義出力対象のテーブルのリスト
     * @return データベースのインデックス情報
     */
    List<IndexEntity> selectIndexList(List<String> schemaList, List<String> tableList);

    /**
     * データベースの制約情報を取得するメソッド
     * 
     * @param schemaList テーブル定義出力対象のスキーマのリスト
     * @param tableList テーブル定義出力対象のテーブルのリスト
     * @return データベースの制約情報
     */
    List<ConstraintEntity> selectConstraintList(List<String> schemaList, List<String> tableList);

    /**
     * データベースの外部キー情報を取得するメソッド
     * 
     * @param schemaList テーブル定義出力対象のスキーマのリスト
     * @param tableList テーブル定義出力対象のテーブルのリスト
     * @return データベースの外部キー情報
     */
    List<ForeignkeyEntity> selectForeignkeyList(List<String> schemaList, List<String> tableList);
    
    /**
     * DTOのListをEntityのListに変換する共通メソッド
     * 
     * @param <D> DTOクラスの型
     * @param <E> Entityクラスの型
     * @param dtoList DTOのList
     * @param mapper DTOからEntityへの変換関数
     * @return EntityのList
     */
    default <D, E> List<E> makeEntityList(List<D> dtoList, Function<D, E> mapper) {
        return dtoList.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
}
