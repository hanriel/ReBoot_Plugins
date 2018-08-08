package com.saf.medievalcore;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class _Listener implements Listener {

    _Listener() {
        Core.plugin.getServer().getPluginManager().registerEvents(this, Core.plugin);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(!Core.hasPlayerData(p.getUniqueId().toString())) Core.createPlayerData(p.getUniqueId().toString());

        JSONObject json = Core.loadPlayerData(p.getUniqueId().toString());
        if (json != null) {
            p.setMetadata("h", new FixedMetadataValue(Core.plugin, json.get("h"))); //health
            p.setMetadata("m", new FixedMetadataValue(Core.plugin, json.get("m"))); //max health

            if(Catcher.mcoins) {
                p.setMetadata("c", new FixedMetadataValue(Core.plugin, json.get("c")));
                p.setMetadata("d", new FixedMetadataValue(Core.plugin, json.get("d")));
                p.setMetadata("lvl", new FixedMetadataValue(Core.plugin, json.get("lvl")));
                p.setMetadata("exp", new FixedMetadataValue(Core.plugin, json.get("exp")));
            }
        }
        Core.writeDataLogin(p.getDisplayName(), '+');
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(!p.hasMetadata("c")) return;
        HashMap<String, Object> saveData = new HashMap<>();
        if(Catcher.mcoins) {
            saveData.put("c", p.getMetadata("c").get(0).asInt());
            saveData.put("d", p.getMetadata("d").get(0).asInt());
        }
        saveData.put("h", p.getMetadata("h").get(0).asDouble());
        saveData.put("m", p.getMetadata("m").get(0).asDouble());
        saveData.put("lvl", p.getMetadata("lvl").get(0).asInt());
        saveData.put("exp", p.getMetadata("exp").get(0).asInt());
                Core.savePlayerData(saveData, p.getUniqueId().toString());
        Core.writeDataLogin(p.getDisplayName(), '-');
    }
}
