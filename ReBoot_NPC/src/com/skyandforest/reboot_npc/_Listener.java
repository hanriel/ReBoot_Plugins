package com.skyandforest.reboot_npc;

import com.skyandforest.reboot_core.util.Utils;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Map;

import static java.lang.Math.*;

public class _Listener implements Listener {

    _Listener() {
        Bukkit.getServer().getPluginManager().registerEvents(this, NPC.plugin);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e){
        Player player = e.getPlayer();
        new BukkitRunnable() {
            String currentPos = "";
            public void run() {
                if(currentPos != ""){
                    if(NPC.places.containsKey(currentPos)){
                        if(player.getLocation().distance(NPC.places.get(currentPos).getLocation()) < 5){
                            partMain(player);
                            return;
                        } else {
                            NPC.places.get(currentPos).getHolo().hidePlayer(player);
                            currentPos = "";
                        }
                    }
                }


                for (Map.Entry<String, Place> entry: NPC.places.entrySet()){
                    if(player.getLocation().distance(entry.getValue().getLocation()) < 64){
                        entry.getValue().getHolo().showPlayer(player);
                    }
                    if(player.getLocation().distance(entry.getValue().getLocation()) < 5){
                        currentPos = entry.getKey();
                        player.sendMessage(Utils.addColors("&aWelcome to region: ") + entry.getKey());

                        partMain(player);
                        break;
                    }

                }
                //if (!enabled) return;//this.cancel();
            }
        }.runTaskTimer(NPC.plugin, 0, 20);
    }

    public  void partMain(Player player){
        new BukkitRunnable() {
            double t = 0;
            public void run() {
                Location loc = player.getLocation();
                t = t + Math.PI / 20;
                double x = 1 * cos(t);
                double z = 1 * sin(t);

                postPart(loc, x,0.1,  z, player);
                postPart(loc, x,0.5,  z, player);
                postPart(loc, x,1.0,  z, player);
                postPart(loc, x,1.5,  z, player);
                postPart(loc, x,2.0,  z, player);
                postPart(loc, x,2.5,  z, player);

                if (t > Math.PI*2) {
                    this.cancel();
                }
            }
        }.runTaskTimer(NPC.plugin, 0, 1);
    }

    public void postPart(Location loc,  Double x, Double y,Double z, Player player){
        loc.add(x, y, z);
        PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), (float) 0.5, (float) 0.5, 1, 1, 0, 0);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(particles);
        loc.subtract(x, y, z);
    }

}

