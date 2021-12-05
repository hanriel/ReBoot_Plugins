package com.hanriel.theobsidianorder.internal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum Variable {
	
	PLAYER("{player}") {
		public String getReplacement(Player executor) {
			return executor.getName();
		}
	},

	MAX_PLAYERS("{max_players}") {
		public String getReplacement(Player executor) {
			return String.valueOf(Bukkit.getMaxPlayers());
		}
	},
	
//	MONEY("{money}") {
//		public String getReplacement(Player executor) {
//			if (EconomyBridge.hasValidEconomy()) {
//				return EconomyBridge.formatMoney(EconomyBridge.getMoney(executor));
//			} else {
//				return "[ECONOMY PLUGIN NOT FOUND]";
//			}
//		}
//	},

	WORLD("{world}") {
		public String getReplacement(Player executor) {
			return executor.getWorld().getName();
		}
	};
	
	private String text;
	
	private Variable(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public abstract String getReplacement(Player executor);
}
