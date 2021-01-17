package io.github.tosirukun.utils;

public class PlayerDataModel {
	public int id;
	public long level;
	public long breaks;
	public int moon;
	public String username;
	public String uuid;

	public void setDatas(int id, String uuid, String username, long breaks, long level, int moon ) {
		this.level = level;
		this.id = id;
		this.breaks = breaks;
		this.moon = moon;
		this.username = username;
		this.uuid = uuid;

	}
}
