package com.hanriel.reboot_npc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class _Listener implements Listener {

    _Listener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, NPC.getInstance());
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e){
        if(DefNPC.getNPCs() == null)
            return;
        if(DefNPC.getNPCs().isEmpty())
            return;
        Player player = e.getPlayer();
        DefNPC.addJoinPacket(player);

        PacketReader reader = new PacketReader();
        reader.inject(player);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent e){
        PacketReader reader = new PacketReader();
        reader.uninject(e.getPlayer());
    }

    @EventHandler
    public void onPlayerRcNPC(RightClickNPC e){
        Exchanger ex = new Exchanger();
        ex.showExchanger(e.getPlayer());
    }

}

