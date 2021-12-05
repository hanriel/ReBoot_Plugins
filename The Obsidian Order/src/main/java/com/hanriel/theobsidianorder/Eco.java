package com.hanriel.theobsidianorder;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class Eco {

    public static long getBalance(Player player) {
        return player.getMetadata("c").get(0).asLong();
    }

    public static void setBalance(Player player, long amount) {
        player.setMetadata("c", new FixedMetadataValue(TheObsidianOrder.getInstance(), amount));
    }

    public static void addBalance(Player player, long amount) {
        setBalance(player, getBalance(player) + amount);
    }

    public static boolean hasBalance(Player player, long amount) {
        return amount <= getBalance(player);
    }

    public static void takeBalance(Player player, long amount) {
        setBalance(player, getBalance(player) - amount);
    }

    public static String formatBalance(long balance) {
        return "&6" + balance + " Люф";
    }
}
