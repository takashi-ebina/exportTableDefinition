package com.export_table_definition.infrastructure.mybatis;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * セッションファクトリ.
 */
public final class MyBatisSqlSessionFactory {
    /**
     * シングルトン　インスタンス.
     */
    private static SqlSessionFactory sqlSessionFactory;

    /**
     * ファクトリのインスタンスの取得.
     * @return ファクトリ
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
            return sqlSessionFactory;
    }

    /**
     * セッション開始.
     * @return セッション
     */
    public static SqlSession openSession() {
        return getSqlSessionFactory().openSession();
    }
}
