package com.skyandforest.reboot_tech;

import com.skyandforest.reboot_tech.util.Holograms;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public abstract class Machine implements Listener {

    protected Holograms holo;
    protected Location location;
    public int mainCycle;

    public abstract void onPlace();
    public abstract void onDestroy();

    public Machine(String title, Location loc){
        this.location = loc;
        this.holo = new Holograms(new String[]{title, "..."}, loc.clone());
        Tech.getInstance().getServer().getPluginManager().registerEvents(this, Tech.getInstance());
        onPlace();

        final int[] i = {0};

        mainCycle = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Tech.getInstance(), new Runnable() {
            @Override
            public void run() {

                holo.hideAll();
                holo = new Holograms(new String[]{title, ".." + i[0]++ + ".."}, location);
                holo.showAll();

            }
        }, 0L,1L);
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerJoinEvent(PlayerJoinEvent e) {
        onPlace();
    }

    @EventHandler (ignoreCancelled = true)
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        if(event.getBlock().equals(location.getBlock())){
            onDestroy();
        }
    }

    @EventHandler (ignoreCancelled = true)
    public void onExplodeBlock(EntityExplodeEvent event) {
        if(event.blockList().contains(location.getBlock())){
            onDestroy();
        }
    }
}
