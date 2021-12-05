package com.hanriel.toohub;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import java.util.List;

public class TabManager {

    private final Main plugin;
    private final String pattern_footer;
    private final String pattern_header;

    public TabManager(Main plugin) {
        this.plugin = plugin;
        pattern_header = loadStringList("tab.header");
        pattern_footer = loadStringList("tab.footer");
    }

    public void showTab() {
        Component header = getHeader();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            try {
                if (Bukkit.getOnlinePlayers().size() != 0) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendPlayerListHeaderAndFooter(header, getFooter(player));
                        player.playerListName(Component.text(ChatColor.translateAlternateColorCodes('&', PlaceholderAPI.setPlaceholders(player, "%luckperms_prefix%" + player.getName() + " %clans_colored_clan_tag%"))));
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 100);
    }

    public TextComponent getHeader() {
        return Component.text(pattern_header);
    }

    public TextComponent getFooter(Player player) {
        int time = player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 1200;
        int days = time / 1440;
        time %= 1440;
        int hours = time / 60;
        time %= 60;
        int minutes = time;

        String footer = pattern_footer;
        footer = footer.replace("%pd%", String.valueOf(days));
        footer = footer.replace("%ph%", String.valueOf(hours));
        footer = footer.replace("%pm%", String.valueOf(minutes));
        footer = PlaceholderAPI.setPlaceholders(player, footer);

        return Component.text(footer);
    }

    private String loadStringList(String configListPath) {
        List<String> lines = plugin.config.getStringList(configListPath);
        StringBuilder sb = new StringBuilder();
        for (String line : lines)
            sb.append(line).append('\n');
        sb.deleteCharAt(sb.length() - 1);
        return ChatColor.translateAlternateColorCodes('&', sb.toString());
    }
}