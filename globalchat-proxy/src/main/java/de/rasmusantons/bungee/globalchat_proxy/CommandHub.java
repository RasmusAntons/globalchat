package de.rasmusantons.bungee.globalchat_proxy;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandHub extends Command {

	public CommandHub(){
		super("hub","permission.hub", "lobby");
	}

	public void execute(CommandSender sender, String[] args){
		if (!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(new ComponentBuilder("kek").create());
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) sender;
		if(player.getServer().getInfo().getName().equalsIgnoreCase("lobby")) {
			player.sendMessage(new ComponentBuilder("You are already in the lobby.").color(ChatColor.RED).create());
			return;
		}
		ServerInfo target = ProxyServer.getInstance().getServerInfo("lobby");
		player.connect(target);
	}
}
