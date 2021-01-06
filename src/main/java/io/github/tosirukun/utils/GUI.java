package io.github.tosirukun.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GUI {
	public void OpenNormalGUI(Player player, int slots, String name) {
		Inventory inv = Bukkit.createInventory(null, slots, name);
		player.openInventory(inv);
	}

}
