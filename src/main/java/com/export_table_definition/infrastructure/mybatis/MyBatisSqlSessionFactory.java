package com.export_table_definition.infrastructure.mybatis;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.export_table_definition.infrastructure.mybatis.type.DatabaseType;
import com.export_table_definition.infrastructure.util.PropertyLoaderUtil;

/**
 * SqlSessionFactoryクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public final class MyBatisSqlSessionFactory {

    /** 唯一のSqlSessionFactoryインスタンス */
    private static SqlSessionFactory sqlSessionFactory;

    /**
     * コンストラクタ（インスタンス化不可）
     */
    private MyBatisSqlSessionFactory() {
    }

    /**
     * SqlSessionFactoryインスタンスの取得
     * 
     * @return SqlSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            try {
                final InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                final Properties properties = new Properties();
                final ResourceBundle res = PropertyLoaderUtil.getResourceBundle("mybatis");
                res.keySet().stream().forEach(key -> properties.setProperty(key, res.getString(key)));
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream, properties);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return sqlSessionFactory;
    }

    /**
     * SqlSession開始
     * 
     * @return SqlSession
     */
    public static SqlSession openSession() {
        return getSqlSessionFactory().openSession();
    }

    /**
     * 接続するデータベースの名称を取得する
     * 
     * @return 接続するデータベースの列挙型
     */
    public static DatabaseType getConnectionDbName() {
        try (final SqlSession session = getSqlSessionFactory().openSession()) {
            final String dbName = session.getConnection().getMetaData().getDatabaseProductName().toLowerCase();
            return DatabaseType.findByName(dbName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
