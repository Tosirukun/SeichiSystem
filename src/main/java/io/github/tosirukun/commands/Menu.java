package io.github.tosirukun.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.tosirukun.Variables;
import io.github.tosirukun.utils.GUI;
import io.github.tosirukun.utils.Message;

public final class Menu implements CommandExecutor {

	Message message = new Message();
	Variables variables = new Variables();

	String prefix = variables.prefix;


	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			GUI gui = new GUI();
			gui.OpenNormalGUI(player, 36, ChatColor.DARK_GRAY + "MENU");
			return true;
		} else {
			message.SendColorMessageToConsole(ChatColor.RED + prefix + "このコマンドはプレイヤーからのみ使用できます");
			return true;
		}
	}
}
