package com.skyandforest.reboot_shop;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializableItemStack implements Serializable {
    public int amount;
    public byte data;
    public short durability;
    public String displayName;
    public Map<Enchantment, Integer> enchantmentList = new HashMap<Enchantment, Integer>();
    public List<String> lore;
    public Material type;

    public SerializableItemStack(ItemStack itemStack) {
        amount = itemStack.getAmount();
        durability = itemStack.getDurability();
        enchantmentList = itemStack.getEnchantments();
        type = itemStack.getType();
        data = itemStack.getData().getData();
        displayName = itemStack.getItemMeta().getDisplayName();
        lore = itemStack.getItemMeta().getLore();
    }

    public ItemStack toItemStack() {
        ItemStack newStack = new ItemStack(type, amount);
        newStack.setData(new MaterialData(type, data));
        newStack.setDurability(durability);
        newStack.addEnchantments(enchantmentList);
        ItemMeta newMeta = newStack.getItemMeta();
        newMeta.setLore(lore);
        newMeta.setDisplayName(displayName);
        newStack.setItemMeta(newMeta);
        return newStack;
    }
}
