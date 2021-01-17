package io.github.tosirukun;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.zaxxer.hikari.HikariDataSource;

import io.github.tosirukun.commands.Menu;
import io.github.tosirukun.utils.Config;
import io.github.tosirukun.utils.Database;
import io.github.tosirukun.utils.Message;
import io.github.tosirukun.utils.PlayerDataModel;

public class Seichisystem extends JavaPlugin implements Listener {

	Variables variables = new Variables();
	Message message = new Message();
	Config con = new Config();
	HikariDataSource hikari;


	String prefix = variables.prefix;
	String version = variables.version;

	Map<String, Long> players_breaks = new HashMap<>();

	@Override
	public void onEnable() {


		message.SendColorMessageToConsole(ChatColor.WHITE + prefix + "Config Loading...");
		saveDefaultConfig();
		reloadConfig();
		FileConfiguration config = getConfig();

		con.setConfig(config);

		message.SendColorMessageToConsole(ChatColor.GREEN + prefix + "Connecting Database...");
		Database db = new Database();
		hikari = db.Database(con);
		message.SendColorMessageToConsole(ChatColor.GREEN + prefix + "Success Connected!");

		message.SendColorMessageToConsole(ChatColor.GREEN + prefix + "RegisterEvent");
		getServer().getPluginManager().registerEvents(this, this);

		message.SendColorMessageToConsole(ChatColor.WHITE + prefix + "Starting Command Regist");
		getCommand("menu").setExecutor(new Menu());
		message.SendColorMessageToConsole(ChatColor.GREEN + prefix + "Register " + ChatColor.AQUA + "Menu " + ChatColor.GREEN + "Command");
		message.SendColorMessageToConsole(ChatColor.GREEN + prefix + "Completed Plugin Enable");
		SendInfoToConsole();
		createTable_Account();


	}


	private void SendInfoToConsole() {
		message.SendColorMessageToConsole(ChatColor.WHITE + "[SeichiSystem Info]=========================");
		message.SendColorMessageToConsole(ChatColor.WHITE + "Version: " + version);
		message.SendColorMessageToConsole(ChatColor.WHITE + "=============================================");
	}

	private boolean doHave_Account(Player p) {
		try (Connection con = hikari.getConnection()) {
			con.setAutoCommit(false);
			try (PreparedStatement statement = con.prepareStatement(
					"SELECT `uuid` FROM `accounts` WHERE `uuid`=?"
					)) {
				statement.setString(1, p.getUniqueId().toString());
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						con.commit();
						if (resultSet.getString(1).toString().equals(p.getUniqueId().toString())) {
							message.SendColorMessageToConsole(ChatColor.GREEN + prefix + "[INFO]参加してきたユーザーにはすでにアカウントが存在しています " + ChatColor.GRAY + "(" + resultSet.getString(1) + ")" );
							return true;
						} else {
							message.SendColorMessageToConsole(ChatColor.GREEN + prefix + "[INFO] アカウント作成しました" + resultSet.getString(1));
							return false;
						}
					}
				}
			} catch (SQLException e) {
				con.rollback();
				throw e;
			} finally {
				con.setAutoCommit(true);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	private void createData_Account(Player p) {
		try (Connection con = hikari.getConnection();
				PreparedStatement statement = con.prepareStatement(
						"INSERT INTO `accounts`(uuid, username, breaks, level, moon) VALUES(?,?,?,?,?)"
						)) {
			statement.setString(1, p.getUniqueId().toString());
			statement.setString(2, p.getName());
			statement.setLong(3, 0);
			statement.setLong(4, 1);
			statement.setInt(5, 0);
			statement.executeUpdate();


		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public PlayerDataModel getData_Account(Player p) {
		try (Connection con = hikari.getConnection();
				PreparedStatement statement = con.prepareStatement(
						"SELECT `id`, `uuid`, `username`, `breaks`, `level`, `moon` FROM `accounts` WHERE `uuid`=?"
						)) {
			statement.setString(1, p.getUniqueId().toString());
			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					PlayerDataModel player_data = new PlayerDataModel();
					player_data.setDatas(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getLong(4), resultSet.getLong(5), resultSet.getInt(6));
					return player_data;
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return new PlayerDataModel();
	}

	private void createTable_Account() {
		try(Connection con = hikari.getConnection();
				Statement statement = con.createStatement()) {
			statement.executeUpdate(
					"CREATE TABLE IF NOT EXISTS `accounts` (" +
						"`id` INT(10) UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT," +
						"`uuid` VARCHAR(255) NOT NULL UNIQUE KEY," +
						"`username` VARCHAR(255) NOT NULL," +
						"`breaks` BIGINT(100) NOT NULL," +
						"`level` BIGINT(10) NOT NULL," +
						"`moon` INT(10) NOT NULL" +
						")"
			);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}



	@EventHandler
	public void PlayerJoin(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		boolean ha = doHave_Account(p);
		if (ha != true) {
			message.SendColorMessageToConsole("Create Account");
			createData_Account(p);
		} else {
			PlayerDataModel player_data = getData_Account(p);
			players_breaks.put(player_data.uuid, player_data.breaks);
		}




	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective objective = board.registerNewObjective("Stats", "dummy");
		objective.setDisplayName(ChatColor.DARK_GRAY + "==" + ChatColor.GRAY + "SeichiSystem" + ChatColor.DARK_GRAY + "==");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		PlayerDataModel player_data = this.getData_Account(p);
		Score breaks = objective.getScore(ChatColor.WHITE + "壊した数 :" + player_data.breaks);
		breaks.setScore(0);
		p.setScoreboard(board);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		players_breaks.remove(e.getPlayer().getUniqueId().toString());
	}

}