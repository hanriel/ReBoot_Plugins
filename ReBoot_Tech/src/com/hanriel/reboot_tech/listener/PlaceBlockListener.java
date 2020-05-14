package com.hanriel.reboot_tech.listener;

import com.hanriel.reboot_tech.Tech;
import com.hanriel.reboot_tech.machines.Query;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlaceBlockListener implements Listener {

    @EventHandler
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        if(event.getItemInHand().getItemMeta().hasLore()){
            if(event.getItemInHand().getItemMeta().getLore().get(0).equals("Query")){
                Location loc = event.getBlock().getLocation().clone();
                Query q = new Query(loc.setDirection(event.getPlayer().getLocation().getDirection()));
                Tech.addMachine(q);
            }
        }

    }

}
