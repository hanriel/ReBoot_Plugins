package com.skyandforest.reboot_tech.machines;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class QueryGUI implements Listener {

    // Create a new inventory, with no owner, a size of nine, called example
    private final Inventory inv;

    public QueryGUI() {
        inv = Bukkit.createInventory(null, 9, "Example");
    }
    // You can call this whenever you want to put the items in
    public void initializeItems() {
        inv.addItem(createGuiItem("Example Sword", new ArrayList<String>(Arrays.asList("This is an example!")), Material.DIAMOND_SWORD));
        inv.addItem(createGuiItem("Example Shovel", new ArrayList<String>(Arrays.asList("This is an example!")), Material.DIAMOND_BOOTS));
    }

    // Nice little method to create a gui item with a custom name, and description
    public ItemStack createGuiItem(String name, ArrayList<String> desc, Material mat) {
        ItemStack i = new ItemStack(mat, 1);
        ItemMeta iMeta = i.getItemMeta();
        iMeta.setDisplayName(name);
        iMeta.setLore(desc);
        i.setItemMeta(iMeta);
        return i;
    }

    // You can open the inventory with this
    public void openInventory(Player p) {
        p.openInventory(inv);
        return;
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory invName = e.getInventory();
        if (!invName.equals(inv)) {
            return;
        }
        if (e.getClick().equals(ClickType.NUMBER_KEY)){
            e.setCancelled(true);
        }
        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().equals(Material.AIR)) return;

        // Using slots click is a best option for your inventory click's
        if (e.getRawSlot() == 10) p.sendMessage("You clicked at slot " + 10);
    }
}
