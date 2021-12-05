package com.hanriel.theobsidianorder.internal;

import org.bukkit.Bukkit;

public class CachedGetters {
	
	private static long lastOnlinePlayersRefresh;
	private static int onlinePlayers;

	public static int getOnlinePlayers() {
		long now = System.currentTimeMillis();
		if (lastOnlinePlayersRefresh == 0 || now - lastOnlinePlayersRefresh > 1000) {
			// getOnlinePlayers() could be expensive if called frequently
			lastOnlinePlayersRefresh = now;
			onlinePlayers = Bukkit.getOnlinePlayers().size();
		}
		
		return onlinePlayers;
	}
	
}
