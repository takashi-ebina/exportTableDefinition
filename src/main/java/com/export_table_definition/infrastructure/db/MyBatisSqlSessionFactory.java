package com.export_table_definition.infrastructure.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.export_table_definition.config.PropertyLoader;
import com.export_table_definition.infrastructure.db.type.DatabaseType;

/**
 * SqlSessionFactoryクラス
 * 
 * @since 1.0
 * @version 1.0
 * @author takashi.ebina
 */
public final class MyBatisSqlSessionFactory {

    private static final Logger logger = LogManager.getLogger(MyBatisSqlSessionFactory.class);
    private static final String MYBATIS_CONFIG = "mybatis-config.xml";
    private static final String PROPERTY_BUNDLE_NAME = "mybatis";
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
    public static synchronized SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory != null) {
            return sqlSessionFactory;
        }
        logger.info("Initializing SqlSessionFactory from {} (bundle={})", MYBATIS_CONFIG, PROPERTY_BUNDLE_NAME);
        try (final InputStream inputStream = Resources.getResourceAsStream(MYBATIS_CONFIG)) {
            if (inputStream == null) {
                throw new IllegalStateException("Could not find resource: " + MYBATIS_CONFIG);
            }
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream,
                    PropertyLoader.getProperties(PROPERTY_BUNDLE_NAME));
        } catch (IOException e) {
            throw new IllegalStateException("SqlSessionFactory initialization failed.", e);
        }
        logger.info("SqlSessionFactory initialization completed.");
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
            throw new IllegalStateException("Failed to get the database name.", e);
        }
    }
}
