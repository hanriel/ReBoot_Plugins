package com.hanriel.theobsidianorder;

import com.hanriel.theobsidianorder.util.Utils;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabManager {

    private TheObsidianOrder plugin;

    public TabManager(TheObsidianOrder plugin) {
        this.plugin = plugin;
    }

    public void showTab() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

            @Override
            public void run() {
                try {
                    Field a = packet.getClass().getDeclaredField("header");
                    a.setAccessible(true);

                    Field b = packet.getClass().getDeclaredField("footer");
                    b.setAccessible(true);

                    String header = "&6&lThe &5&lObsidian &6&lOrder\n&7Игроков онлайн: " + plugin.getServer().getOnlinePlayers().size()
                            + "\n&2TPS: " + Math.min(Math.round(MinecraftServer.getServer().recentTps[0] * 100.0) / 100.0, 20.0)
                            + "; PING: ";

                    b.set(packet, new ChatComponentText(Utils.addColors("\n&7Поcетите наш сайт &ewww.obsidianorder.ru")));

                    if (Bukkit.getOnlinePlayers().size() != 0) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            a.set(packet, new ChatComponentText(Utils.addColors(header + ((CraftPlayer) player).getHandle().ping) + "\n"));
                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 10, 40);
    }

//    public void addHeader(String header) {
//        headers.add(new ChatComponentText(Utils.addColors(header)));
//    }
//
//    public void addFooter(String footer) {
//        headers.add(new ChatComponentText(Utils.addColors(footer)));
//    }
}
