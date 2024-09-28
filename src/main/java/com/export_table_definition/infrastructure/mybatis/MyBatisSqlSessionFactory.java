package com.export_table_definition.infrastructure.mybatis;

import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.export_table_definition.infrastructure.mybatis.type.DBType;
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
	 * @return 接続するデータベースの名称
	 */
	public static String getConnectionDbName() {
		String dbName = "";
		try (final SqlSession session = getSqlSessionFactory().openSession()){
			final String driverName = session.getConnection().getMetaData().getDriverName();
			System.out.println(driverName);
			dbName = DBType.getByDrivarName(driverName).getDbName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbName;
	}
}
