package com.hanriel.reboot_dungeons;

import com.hanriel.reboot_core.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Map;

public class _Listener  implements Listener, CommandExecutor {

    _Listener() {
        Dungeons.getInstance().getServer().getPluginManager().registerEvents(this, Dungeons.getInstance());
    }

    private static Map<Player, Location> check;


    @EventHandler
    public void onPlayerMove(PlayerInteractEvent e) {

        if(e.getAction().equals(Action.PHYSICAL)){
            Location loc = e.getClickedBlock().getLocation();
            Player player = e.getPlayer();
            if(e.getClickedBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE){

//                if(check.containsKey(player)){
//                    check.replace(player, 1);
//                } else {
//                    check.put(player, 0);
//                }

                Bukkit.getWorld(player.getWorld().getName()).createExplosion(player.getLocation(), 0);
            }

            if(e.getClickedBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE){
                Bukkit.getWorld(player.getWorld().getName()).createExplosion(player.getLocation(), 0);
            }

            if(e.getClickedBlock().getType() == Material.STONE_PRESSURE_PLATE){
                Bukkit.getWorld(player.getWorld().getName()).createExplosion(player.getLocation(), 0);
            }
        }

    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        boolean error = false;
        switch (cmd.getName()) {
            case "dun":
                switch (args.length){
                    case 0:

                        break;
                    case 1:
                        switch (args[0]) {
                            case "reload":
                                error = true;
                                break;
                            case "start":

                                Location loc = new Location(Bukkit.getWorld("DUNGEONS"), 196d,47d,230d);
                                s.sendMessage("Starting generate castl...");
                                s.sendMessage("Starting generate castl...Layer 1");
                                for(int i=0; i<32; i++){
                                    for(int j=0; j<66; j++){
                                        for(int k=0; k<53; k++){
                                            loc.add(i,j,k);
                                            BlockSave bs = new BlockSave(loc.getBlock());
                                            if(bs.getLayer()!=1) continue;
                                            loc.add(0,0,100);
                                            try {
                                                bs.restoreBlock(loc.getBlock());
                                            } catch (Exception e){
                                                e.printStackTrace();
                                            }
                                            loc.subtract(i,j,100+k);
                                        }
                                    }
                                    s.sendMessage(String.valueOf(i));
                                }

                                s.sendMessage("Starting generate castl...Layer 2");
                                for(int i=0; i<32; i++){
                                    for(int j=0; j<66; j++){
                                        for(int k=0; k<53; k++){
                                            loc.add(i,j,k);
                                            BlockSave bs = new BlockSave(loc.getBlock());
                                            if(bs.getLayer()!=2) continue;
                                            loc.add(0,0,100);
                                            bs.restoreBlock(loc.getBlock());
                                            loc.subtract(i,j,100+k);
                                        }
                                    }
                                    s.sendMessage(String.valueOf(i));
                                }
                                return true;
                            default:
                                error = true;
                                break;
                        }
                        break;
                    default:
                        error = true;
                        break;
                }

            default:
                break;
        }
        if(error) s.sendMessage(Utils.addColors("&ePlease type \"&f/? MedievalCore\" &efor help."));
        return true;
    }

}
