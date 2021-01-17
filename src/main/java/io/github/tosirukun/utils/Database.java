package io.github.tosirukun.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Database {

	private HikariDataSource hikari;

	public HikariDataSource Database(Config con) {
		HikariConfig config = new HikariConfig();

		config.setDriverClassName("com.mysql.jdbc.Driver");

		config.setJdbcUrl("jdbc:mysql://" + con.url + ":" + con.port + "/" + con.database);

		config.addDataSourceProperty("user", con.user);
		config.addDataSourceProperty("password", con.password);

		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		config.addDataSourceProperty("useServerPrepStmts", "true");

		config.setInitializationFailFast(true);

		config.setConnectionInitSql("SELECT 1");

		this.hikari = new HikariDataSource(config);

		return this.hikari;

	}

}
