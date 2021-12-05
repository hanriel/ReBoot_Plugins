package com.hanriel.tootitlemanager;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class TitleManager extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private static TitleManager instance;
    private TabManager tab;

    @Override
    public void onEnable() {
        if (instance != null) return;
        instance = this;

        this.tab = new TabManager(this);
        tab.showTab();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
        } else {
            log.warning("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        //TAB.setInstance(new TAB(new BukkitPlatform(this, NMSStorage.getInstance()), new BukkitPacketBuilder(NMSStorage.getInstance()), ProtocolVersion.fromFriendlyName(Bukkit.getBukkitVersion().split("-")[0])));
        //Bukkit.getPluginManager().registerEvents(new BukkitEventListener(), this);
        //TAB.getInstance().load();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static TitleManager getInstance() {
        return instance;
    }
}
