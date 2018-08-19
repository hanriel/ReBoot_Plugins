package com.skyandforest.reboot_economy.listener;

import com.skyandforest.reboot_economy.Eco;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockListener implements Listener {

    @EventHandler
    public void onPlayerBreakBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material roto = block.getType();
        switch (roto){
            case COAL_ORE:
                Eco.addBalance(player, 10, true);
                break;
            case IRON_ORE:
                Eco.addBalance(player, 25, true);
                break;
            case QUARTZ_ORE:
                Eco.addBalance(player, 25, true);
                break;
            case LAPIS_ORE:
                Eco.addBalance(player, 50, true);
                break;
            case GLOWING_REDSTONE_ORE:
                Eco.addBalance(player, 50, true);
                break;
            case GOLD_ORE:
                Eco.addBalance(player, 100, true);
                break;
            case DIAMOND_ORE:
                Eco.addBalance(player, 200, true);
                break;
            case EMERALD_ORE:
                Eco.addBalance(player, 250, true);
                break;
        }
    }

}
