package de.rasmusantons.bungee.globalchat;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.rasmusantons.bungee.datapass_bukkit_api.DataPass;
import de.rasmusantons.bungee.datapass_bukkit_api.DataSender;
import de.rasmusantons.bungee.datapass_bukkit_api.events.DataPassStringEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

	private ColouredChat colouredChat;

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		colouredChat = new ColouredChat();
		getCommand("c").setExecutor(colouredChat);
		getCommand("/close");
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.isOp()) {
			sender.sendMessage(ChatColor.RED + "no permission");
			return true;
		}
		getServer().broadcastMessage(ChatColor.RED + "Server closing, connecting to lobby");
		for (Player player : getServer().getOnlinePlayers()) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("Connect");
			out.writeUTF("lobby");
			player.sendPluginMessage(this, "BungeeCord", out.toByteArray());
		}
		getServer().getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				Bukkit.shutdown();
			}
		}, 30L);
		return true;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		colouredChat.onPlayerChat(event);
		if (event.getMessage().startsWith("!")) {
			String message = String.format("<%s> %s", event.getPlayer().getPlayerListName(), event.getMessage().substring(1));
			DataPass.getDataSender().sendData(new DataPassStringEvent(message));
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		event.setJoinMessage(null);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}

	@EventHandler
	public void onDataPassString(DataPassStringEvent event) {
		getServer().broadcastMessage(event.getData());
	}
}
