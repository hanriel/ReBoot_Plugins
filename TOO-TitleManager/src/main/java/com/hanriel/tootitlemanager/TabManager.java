package com.hanriel.tootitlemanager;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public class TabManager {

    private TitleManager plugin;

    public TabManager(TitleManager plugin) {
        this.plugin = plugin;
    }

    public void showTab() {
        Component header = Component.text(ChatColor.translateAlternateColorCodes('&', "\n&6&lThe &5&lObsdian &6&lOrder\n&d&lРежим: &e&lВЫЖИВАНИЕ\n"));
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    if (Bukkit.getOnlinePlayers().size() != 0) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            int time = player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 1200;
                            int days = time / 1440;
                            time %= 1440;
                            int hours = time / 60;
                            time %= 60;
                            int minutes = time;

                            Component footer = Component.text(ChatColor.translateAlternateColorCodes('&',
                                     "\n&7Ping: &a" + player.getPing() + "ms &6| &7TPS: &a" + MinecraftServer.TPS + " &6| &7Онлайн: &a" + plugin.getServer().getOnlinePlayers().size() + "\n" +
                                             "&7Наиграно: &9" + days + "&7д &9" + hours + "&7ч &9" + minutes + "&7мин\n" +
                                             "&7Посетите наш сайт &eobsidianorder.ru"));
                            player.sendPlayerListHeaderAndFooter(header, footer);
                            player.playerListName(Component.text(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, "%luckperms_prefix%" + player.getName() + " %clans_colored_clan_tag%"))));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10);
    }

//    public void addHeader(String header) {
//        headers.add(new ChatComponentText(Utils.addColors(header)));
//    }
//
//    public void addFooter(String footer) {
//        headers.add(new ChatComponentText(Utils.addColors(footer)));
//    }
}
