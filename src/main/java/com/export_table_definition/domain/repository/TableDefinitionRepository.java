package com.export_table_definition.domain.repository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.export_table_definition.domain.model.AllColumnEntity;
import com.export_table_definition.domain.model.AllConstraintEntity;
import com.export_table_definition.domain.model.AllForeignkeyEntity;
import com.export_table_definition.domain.model.AllIndexEntity;
import com.export_table_definition.domain.model.AllTableEntity;
import com.export_table_definition.domain.model.BaseInfoEntity;

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
    List<AllTableEntity> selectAllTableInfo(List<String> schemaList, List<String> tableList);

    /**
     * データベースのカラム情報を取得するメソッド
     * 
     * @param schemaList テーブル定義出力対象のスキーマのリスト
     * @param tableList テーブル定義出力対象のテーブルのリスト
     * @return データベースのカラム情報
     */
    List<AllColumnEntity> selectAllColumnInfo(List<String> schemaList, List<String> tableList);

    /**
     * データベースのインデックス情報を取得するメソッド
     * 
     * @param schemaList テーブル定義出力対象のスキーマのリスト
     * @param tableList テーブル定義出力対象のテーブルのリスト
     * @return データベースのインデックス情報
     */
    List<AllIndexEntity> selectAllIndexInfo(List<String> schemaList, List<String> tableList);

    /**
     * データベースの制約情報を取得するメソッド
     * 
     * @param schemaList テーブル定義出力対象のスキーマのリスト
     * @param tableList テーブル定義出力対象のテーブルのリスト
     * @return データベースの制約情報
     */
    List<AllConstraintEntity> selectAllConstraintInfo(List<String> schemaList, List<String> tableList);

    /**
     * データベースの外部キー情報を取得するメソッド
     * 
     * @param schemaList テーブル定義出力対象のスキーマのリスト
     * @param tableList テーブル定義出力対象のテーブルのリスト
     * @return データベースの外部キー情報
     */
    List<AllForeignkeyEntity> selectAllForeignkeyInfo(List<String> schemaList, List<String> tableList);
    
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
