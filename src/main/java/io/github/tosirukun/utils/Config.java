package io.github.tosirukun.utils;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	public String url;
	public String port;
	public String database;
	public String user;
	public String password;

	public void setConfig(FileConfiguration con) {
		this.url = con.getString("Database.MysqlUrl");
		this.port = con.getString("Database.MysqlPort");
		this.database = con.getString("Database.MysqlDatabase");
		this.user = con.getString("Database.MysqlUsername");
		this.password = con.getString("Database.MysqlPassword");
	}
}
