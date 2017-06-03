package de.rasmusantons.bungee.globalchat_proxy;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Plugin;

import de.rasmusantons.bungee.datapass_proxy_api.DataPass;
import de.rasmusantons.bungee.datapass_proxy_api.events.DataPassStringEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class Main extends Plugin implements Listener {
	@Override
	public void onEnable() {
		getProxy().getPluginManager().registerListener(this, this);
		getProxy().getPluginManager().registerCommand(this, new CommandHub());
	}

	@EventHandler
	public void onLogin(PostLoginEvent event) {
		DataPass.getDataSender().broadcastData(new DataPassStringEvent(null, String.format(ChatColor.YELLOW + "%s logged in", event.getPlayer().getName())));
	}

	@EventHandler
	public void onLogout(PlayerDisconnectEvent event) {
		DataPass.getDataSender().broadcastData(new DataPassStringEvent(null, String.format(ChatColor.YELLOW + "%s logged out", event.getPlayer().getName())));
	}

	@EventHandler
	public void onDataPassString(DataPassStringEvent event) {
		//getLogger().info(event.getData());
		DataPass.getDataSender().broadcastData(new DataPassStringEvent(event.getServer(), "[" + event.getServer().getName() + "] " + event.getData()));
	}
}
