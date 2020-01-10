package com.skyandforest.reboot_tech;

import com.skyandforest.reboot_core.command.CommandFramework;
import com.skyandforest.reboot_tech.command.TechCommandHandler;
import com.skyandforest.reboot_tech.listener.PlaceBlockListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tech extends JavaPlugin {

    public static final String CHAT_PREFIX = ChatColor.AQUA + "[" + ChatColor.GREEN + "Tech" + ChatColor.AQUA + "] " + ChatColor.GREEN;

    private static Tech instance;

    public static List<Machine> machines;

    @Override
    public void onEnable() {
        if (instance != null) {
            getLogger().warning("Please do not use /reload or plugin reloaders. Do \"/tech reload\" instead.");
            return;
        }

        instance = this;
        machines = new ArrayList<>();

        CommandFramework.register(this, new TechCommandHandler("tech"));

        Tech.getInstance().getServer().getPluginManager().registerEvents(new PlaceBlockListener(), instance);

        load();
        hrecipe();
    }

    public void load() {


        Bukkit.getServicesManager().register(Tech.class, this, this, ServicePriority.Normal);

        if (!Bukkit.getMessenger().isOutgoingChannelRegistered(this, "BungeeCord")) {
            Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        }
    }

    public static void addMachine(Machine machine){
        machines.add(machine);
    }

    public static Tech getInstance() {
        return instance;
    }

    private void hrecipe() {
        ItemStack query = new ItemStack(Material.FURNACE, 1);
        ItemMeta meta = query.getItemMeta();
        meta.setDisplayName(ChatColor.GRAY + "Int. query");
        meta.setLore(Arrays.asList("Query", "1 lvl", "Use with caution."));
        query.setItemMeta(meta);

        NamespacedKey key = new NamespacedKey(this, "query");
        ShapedRecipe shapedRecipe = new ShapedRecipe(key, query);
        shapedRecipe.shape(
                "RDR",
                "TFT",
                "UOU");
        shapedRecipe.setIngredient('R', Material.REDSTONE_BLOCK);
        shapedRecipe.setIngredient('D', Material.DIAMOND_BLOCK);
        shapedRecipe.setIngredient('T', Material.REDSTONE_TORCH);
        shapedRecipe.setIngredient('F', Material.FURNACE);
        shapedRecipe.setIngredient('U', Material.DISPENSER);
        shapedRecipe.setIngredient('O', Material.OBSERVER);
        Bukkit.getServer().addRecipe(shapedRecipe);
    }
}
