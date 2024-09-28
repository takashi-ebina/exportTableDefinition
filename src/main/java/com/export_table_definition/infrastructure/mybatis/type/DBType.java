package com.export_table_definition.infrastructure.mybatis.type;

import java.util.Arrays;

/**
 * データベースの種別をもつ列挙型クラス
 */
public enum DBType {
	
	POSTGRESQL("postgresql", "postgresql jdbc driver"),
	ORACLE("oracle", "oracle jdbc driver");
	
	private final String dbName;
	private final String driverName;
	
	DBType(String dbName, String driverName) {
		this.dbName = dbName;
		this.driverName = driverName;
	}
	
	/**
	 * データベースの名称を返却するメソッド
	 * 
	 * @return データベースの名称
	 */
	public String getDbName() {
		return this.dbName;
	}
	
	/**
	 * ドライバーの名称を返却するメソッド
	 * 
	 * @return ドライバーの名称
	 */
	public String getDrivarName() {
		return this.driverName;
	}
	
    /**
     * ドライバーの名称からEnumを取得する
     *
     * @param dbName
     * @return　データベースの種別をもつ列挙型クラス
     */
	public static DBType getByDrivarName(String driverName) {
		return Arrays.stream(DBType.values())
				.filter(v -> v.getDrivarName().equals(driverName.toLowerCase())).findFirst().orElse(null);
	}
}
