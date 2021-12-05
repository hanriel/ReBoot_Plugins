package com.hanriel.theobsidianorder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.UUID;

public class GameGUI implements Listener {

    private String name;
    private String uuid;
    private int size;
    private OptionClickEventHandler handler;
    private Plugin plugin;
    private Player player;
    private ItemStack[] optionIcons;

    public GameGUI(String name, int size, OptionClickEventHandler handler, Plugin plugin) {
        this.name = name;
        this.size = size;
        this.handler = handler;
        this.plugin = plugin;
        this.uuid = UUID.randomUUID().toString();
        this.optionIcons = new ItemStack[size];
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public GameGUI setOption(int position, ItemStack itemStack) {
        optionIcons[position] = itemStack;
        return this;
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(player, size, uuid);
        for (int i = 0; i < optionIcons.length; i++) {
            if (optionIcons[i] != null) {
                inventory.setItem(i, optionIcons[i]);
            }
        }
        player.openInventory(inventory);
    }

    public void destroy() {
        HandlerList.unregisterAll(this);
        handler = null;
        plugin = null;
        optionIcons = null;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(uuid)) {
            event.setCancelled(true);
            if (event.getClick() != ClickType.LEFT)
                return;
            int slot = event.getRawSlot();
            if (slot >= 0 && slot < size && optionIcons[slot] != null) {
                OptionClickEvent e = new OptionClickEvent((Player) event.getWhoClicked(), slot, optionIcons[slot]);
                handler.onOptionClick(e);
                ((Player) event.getWhoClicked()).updateInventory();
                if (e.willClose()) {
                    final Player p = (Player)event.getWhoClicked();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, p::closeInventory);
                }
                if (e.willDestroy()) {
                    destroy();
                }
            }
        }
    }

    public interface OptionClickEventHandler {
        void onOptionClick(OptionClickEvent event);
    }

    public class OptionClickEvent {
        private Player player;
        private int position;
        private boolean close;
        private boolean destroy;
        private ItemStack item;

        public OptionClickEvent(Player player, int position, ItemStack item) {
            this.player = player;
            this.position = position;
            this.close = true;
            this.destroy = false;
            this.item = item;
        }

        public Player getPlayer() {
            return player;
        }

        public int getPosition() {
            return position;
        }

        public boolean willClose() {
            return close;
        }

        public boolean willDestroy() {
            return destroy;
        }

        public void setWillClose(boolean close) {
            this.close = close;
        }

        public void setWillDestroy(boolean destroy) {
            this.destroy = destroy;
        }

        public ItemStack getItem() {
            return item;
        }
    }

    private ItemStack setItemNameAndLore(ItemStack item, String name, String[] lore) {
        ItemMeta im = item.getItemMeta();
        if (im != null) {
            im.setDisplayName(name);
            im.setLore(Arrays.asList(lore));
            item.setItemMeta(im);
        }

        return item;
    }

}