package com.hanriel.reboot_economy.listener;

import com.hanriel.reboot_economy.Eco;
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
                Eco.addBalance(player, 10);
                break;
            case IRON_ORE:
                Eco.addBalance(player, 25);
                break;
            case NETHER_QUARTZ_ORE:
                Eco.addBalance(player, 25);
                break;
            case LAPIS_ORE:
                Eco.addBalance(player, 50);
                break;
            case REDSTONE_ORE:
                Eco.addBalance(player, 50);
                break;
            case GOLD_ORE:
                Eco.addBalance(player, 100);
                break;
            case DIAMOND_ORE:
                Eco.addBalance(player, 200);
                break;
            case EMERALD_ORE:
                Eco.addBalance(player, 250);
                break;
        }
    }

}
