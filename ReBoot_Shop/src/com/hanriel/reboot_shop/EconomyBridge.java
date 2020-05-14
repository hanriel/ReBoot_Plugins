package com.hanriel.reboot_shop;

import com.hanriel.reboot_economy.Eco;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class EconomyBridge {

	private static Eco economy;

	private static HashMap<Player, Long> transList;

	static {
		transList = new HashMap<>();
	}

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
		runnable();
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
		return economy.hasBalance(player, "copper", (long) minimum);
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

		transList.put(player, amount);

	}

	static void runnable() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for (Map.Entry<Player, Long> entry : transList.entrySet()) {
					if (entry.getKey().isOnline()) {
						economy.addBalance(entry.getKey(), entry.getValue());
						transList.remove(entry.getKey());
						break;
					}
				}
			}

		}.runTaskTimer(Eco.getInstance(), 0, 20);
	}
}