package com.hanriel.reboot_auth.listener;

import com.hanriel.reboot_auth.Shop;
import com.hanriel.reboot_auth.Permissions;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (Shop.getLastReloadErrors() > 0 && event.getPlayer().hasPermission(Permissions.SEE_ERRORS)) {
            event.getPlayer().sendMessage(Shop.CHAT_PREFIX + ChatColor.RED + "The plugin found " + Shop.getLastReloadErrors() + " error(s) last time it was loaded. You can see them by doing \"/rba reload\" in the console.");
        }

        //if (Shop.hasNewVersion() && Shop.getSettings().update_notifications && event.getPlayer().hasPermission(Permissions.UPDATE_NOTIFICATIONS)) {
        //    event.getPlayer().sendMessage(Shop.CHAT_PREFIX + "Found an update: " + Shop.getNewVersion() + ". Download:");
        //    event.getPlayer().sendMessage(ChatColor.DARK_GREEN + ">> " + ChatColor.GREEN + "http://dev.bukkit.org/bukkit-plugins/chest-commands");
        //}
    }
}
