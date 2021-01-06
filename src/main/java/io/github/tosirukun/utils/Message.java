package io.github.tosirukun.utils;

import org.bukkit.Bukkit;

public class Message {
	public void SendColorMessageToConsole(String msg) {
		Bukkit.getConsoleSender().sendMessage(msg);
	}
}
