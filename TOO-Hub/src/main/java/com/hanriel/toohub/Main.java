package com.hanriel.toohub;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {


    private static Main instance;
    public TabManager tab;
    FileConfiguration config = getConfig();


    @Override
    public void onEnable() {
        if (instance != null) return;
        instance = this;

        this.saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new EventListen(), instance);

        this.tab = new TabManager(this);
        tab.showTab();

    }

    @Override
    public void onDisable() {
        //if(Main.getInstance() != null) Main.getInstance().unload();
    }

    public static Main getInstance() {
        return instance;
    }

}
