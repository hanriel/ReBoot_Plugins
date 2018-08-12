package com.skyandforest.reboot_shop;

import com.skyandforest.reboot_economy.Eco;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class EconomyBridge {
	
	private static Eco economy;

	static boolean setupEconomy() {

        if (Bukkit.getPluginManager().getPlugin("RBEconomy") == null) {
        	Bukkit.getLogger().warning("Eco not found!");
            return false;
        }
        RegisteredServiceProvider<Eco> rsp = Bukkit.getServicesManager().getRegistration(Eco.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
	
	private static boolean hasValidEconomy() {
		return economy != null;
	}

//	public static Economy getEconomy() {
//		if (!hasValidEconomy()) throw new IllegalStateException("Economy plugin was not found!");
//		return economy;
//	}
//	public static double getMoney(Player player) {
//		if (!hasValidEconomy()) throw new IllegalStateException("Economy plugin was not found!");
//		return economy.getBalance(player.getName(), player.getWorld().getName());
//	}
	
	public static boolean hasMoney(Player player, double minimum) {
		if (!hasValidEconomy()) throw new IllegalStateException("Economy plugin was not found!");
		if (minimum < 0.0) throw new IllegalArgumentException("Invalid amount of money: " + minimum);
		return economy.hasBalance(player, "copper", (long)minimum);
	}
	
	/**
	 * @return true if the operation was successful.
	 */
	public static void takeMoney(Player player, long amount) {
		if (!hasValidEconomy()) throw new IllegalStateException("Economy plugin was not found!");
		if (amount < 0) throw new IllegalArgumentException("Invalid amount of money: " + amount);
		
		economy.addBalance(player, -amount);
	}
	
	public static void giveMoney(Player player, long amount) {
		if (!hasValidEconomy()) throw new IllegalStateException("Economy plugin was not found!");
		if (amount < 0.0) throw new IllegalArgumentException("Invalid amount of money: " + amount);

		economy.addBalance(player, amount);
	}
	
//	public static String formatMoney(double amount) {
//		if (hasValidEconomy()) {
//			return economy.format(amount);
//		} else {
//			return Double.toString(amount);
//		}
//	}
}
