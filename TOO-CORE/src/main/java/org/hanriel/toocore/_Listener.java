package org.hanriel.toocore;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.hanriel.toocore.playerData.PlayerDataFramework;

public class _Listener implements Listener {

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        TextComponent msg = Component.text(player.getName(), NamedTextColor.GREEN)
                .append(Component.text(" присоединилс€ к игре", NamedTextColor.WHITE));

        final Component mainTitle = Component.text("The ", NamedTextColor.GOLD, TextDecoration.BOLD)
                .append(Component.text("Obsidian ", NamedTextColor.DARK_PURPLE, TextDecoration.BOLD))
                .append(Component.text("Order", NamedTextColor.GOLD, TextDecoration.BOLD));
        final Component subtitle = Component.text("ѕри€тной игры!", NamedTextColor.AQUA);
        Title title = Title.title(mainTitle, subtitle);

        player.showTitle(title);
        e.joinMessage(msg);
        PlayerDataFramework.loadData(player);
    }

    @EventHandler
    public boolean onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        TextComponent msg = Component.text(player.getName(), NamedTextColor.RED)
                .append(Component.text(" покинул игру", NamedTextColor.WHITE));
        e.quitMessage(msg);
        //if (!player.hasMetadata("c"))
        //    return false;
        PlayerDataFramework.uploadData(player);
        return true;
    }
}
