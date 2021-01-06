package io.github.tosirukun;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.tosirukun.commands.Menu;
import io.github.tosirukun.utils.Message;

public class Seichisystem extends JavaPlugin {

	Variables variables = new Variables();
	Message message = new Message();

	String prefix = variables.prefix;
	String version = variables.version;


	@Override
	public void onEnable() {
		message.SendColorMessageToConsole(ChatColor.WHITE + prefix + "Starting Command Regist");
		getCommand("menu").setExecutor(new Menu());
		message.SendColorMessageToConsole(ChatColor.GREEN + prefix + "Register " + ChatColor.AQUA + "Menu " + ChatColor.GREEN + "Command");
		message.SendColorMessageToConsole(ChatColor.GREEN + prefix + "Completed Plugin Enable");
		SendInfoToConsole();

	}

	private void SendInfoToConsole() {
		message.SendColorMessageToConsole(ChatColor.WHITE + "[SeichiSystem Info]=========================");
		message.SendColorMessageToConsole(ChatColor.WHITE + "Version: " + version);
		message.SendColorMessageToConsole(ChatColor.WHITE + "=============================================");
	}


}
