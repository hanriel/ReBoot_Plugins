package com.skyandforest.reboot_gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.skyandforest.reboot_core.Core;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class _Listener implements Listener {

    _Listener() {
        GUI.plugin.getServer().getPluginManager().registerEvents(this, GUI.plugin);
    }

    private static HashMap<String, BossBar> hBars = new HashMap<>();

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player) e.getEntity();
        if (p.getGameMode() == GameMode.ADVENTURE) e.setDamage(0);

        double h = p.getMetadata("h").get(0).asDouble() - e.getDamage();
        if ((h <= 0.0) || p.getPlayer().getLocation().getY()<=0) respawn(p);
        else {
            p.setMetadata("h", new FixedMetadataValue(Core.getInstance(), h));
            p.setHealth((h * 20) / p.getMetadata("m").get(0).asDouble());
        }

        setBarH(p);
        e.setDamage(0.0);
    }


    @EventHandler
    public void onHeal(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            double damage = e.getAmount();
            double h = p.getMetadata("h").get(0).asDouble() + e.getAmount();
            double m = p.getMetadata("m").get(0).asDouble();

            if (h>=m) h=m;
            p.setMetadata("h", new FixedMetadataValue(Core.getInstance(), h));
            p.setHealth((h * 20) / p.getMetadata("m").get(0).asDouble());
            setBarH(p);
            e.setCancelled(true);
        }

    }


    private static void setBarH(Player p) {
        if (!hBars.containsKey(p.getDisplayName())) {
            BossBar b = Bukkit.createBossBar("", BarColor.RED, BarStyle.SEGMENTED_10);
            hBars.put(p.getDisplayName(), b);
        }
        BossBar bar = hBars.get(p.getDisplayName());
        double m = p.getMetadata("m").get(0).asDouble();
        double h = p.getMetadata("h").get(0).asDouble();
        bar.setProgress(h / m);
        bar.setTitle(ChatColor.RED+"Health: " + String.format(Locale.ENGLISH, "%(.1f", h) + "/" + m);
        bar.addPlayer(p);
    }

    private void respawn(Player p) {
        double m = p.getMetadata("m").get(0).asDouble();
        p.setMetadata("h", new FixedMetadataValue(Core.getInstance(), m));
        p.setHealth(20.0);

        p.setGameMode(GameMode.ADVENTURE);
        p.teleport(p.getBedSpawnLocation());
        p.resetPlayerWeather();

        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, true, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 3, true, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 100, 3, true, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 100, 1, true, false));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 10, true, false));

        AtomicInteger d_timer = new AtomicInteger(5);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(GUI.plugin, () -> {
            if (d_timer.get() != -1) {
                if (d_timer.get() != 0) {
                    p.sendTitle("YOU DIED", "Wait " + d_timer + " seconds", 5, 50, 5);
                    d_timer.getAndDecrement();
                } else {
                    p.setGameMode(GameMode.SURVIVAL);
                    d_timer.getAndDecrement();
                }
            }
        }, 0, 20);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(GUI.plugin, () -> {

        }, 0, 1);

    }
}
