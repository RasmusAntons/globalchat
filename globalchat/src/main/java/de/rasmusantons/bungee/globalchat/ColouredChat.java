package de.rasmusantons.bungee.globalchat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ColouredChat implements CommandExecutor {

	private static String CHAT_COLOURS_STRING;
	private static ChatColor[] RAINBOW_COLOURS = {
			ChatColor.RED,
			ChatColor.YELLOW,
			ChatColor.GREEN,
			ChatColor.BLUE,
			ChatColor.LIGHT_PURPLE
	};

	static {
		CHAT_COLOURS_STRING = "";
		for (ChatColor colour: ChatColor.values()) {
			switch (colour) {
				case RESET:
					break;
				case MAGIC:
					CHAT_COLOURS_STRING = CHAT_COLOURS_STRING + colour + colour.getChar() + ChatColor.RESET + "(k) ";
					break;
				default:
					CHAT_COLOURS_STRING = CHAT_COLOURS_STRING + colour + colour.getChar() + ChatColor.RESET + " ";
			}
		}
	}

	private int nextColor = 0;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("c")) {
			sender.sendMessage(CHAT_COLOURS_STRING);
			return true;
		}
		return true;
	}

	public void onPlayerChat(AsyncPlayerChatEvent event) {
		String message = ChatColor.translateAlternateColorCodes('&', event.getMessage());

		int startIndex, endIndex;
		while ((startIndex = message.indexOf("&h")) != -1) {
			endIndex = message.length();
			for (ChatColor colour : ChatColor.values()) {
				if (colour.compareTo(ChatColor.MAGIC) >= 0)
					break;
				int colourChange = message.substring(startIndex + 2, message.length()).indexOf(colour.toString());
				if (colourChange > 0)
					endIndex =  Math.min(endIndex, startIndex + 2 + colourChange);
			}
			String newPart = "";
			for (int i = startIndex + 2; i < endIndex; ++i) {
				newPart = newPart + RAINBOW_COLOURS[nextColor] + event.getMessage().charAt(i);
				nextColor = (nextColor + 1) % RAINBOW_COLOURS.length;
			}
			message = message.substring(0, startIndex) + newPart + message.substring(endIndex, message.length());
		}

		event.setMessage(message);
	}
}
