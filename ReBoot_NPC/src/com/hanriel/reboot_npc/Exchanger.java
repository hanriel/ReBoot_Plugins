package com.hanriel.reboot_npc;

import com.hanriel.reboot_core.util.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Exchanger {

    public void showExchanger(Player player) {
        ExchangerMenu menu = new ExchangerMenu("Обменник", 27, event -> {
            event.setWillClose(true);
            event.setWillDestroy(true);
            if (((event.getPosition() <= 9) && (event.getPosition() >= 0)) ||
                    ((event.getPosition() <= 27) && (event.getPosition() >= 17))) {
                event.setWillClose(false);
                event.setWillDestroy(false);
            } else if (event.getPosition() == 10) {
                boolean whatIf = doExchange(player, 1);
                event.setWillClose(whatIf);
                event.setWillDestroy(whatIf);
            } else if (event.getPosition() == 12) {
                boolean whatIf = doExchange(player, 16);
                event.setWillClose(whatIf);
                event.setWillDestroy(whatIf);
            } else if (event.getPosition() == 14) {
                boolean whatIf = doExchange(player, 32);
                event.setWillClose(whatIf);
                event.setWillDestroy(whatIf);
            } else if (event.getPosition() == 16) {
                boolean whatIf = doExchange(player, 64);
                event.setWillClose(whatIf);
                event.setWillDestroy(whatIf);
            }
        }, NPC.getInstance())
                .setOption(10, getIDeposit("&aПродать алмаз", 1))
                .setOption(12, getIDeposit("&aПродать 16 алмазов", 16))
                .setOption(14, getIDeposit("&aПродать 32 алмаза", 32))
                .setOption(16, getIDeposit("&aПродать 64 алмаза", 64));

        for (int i = 0; i < 27; i++) {
            if (i != 10 && i != 12 && i != 14 && i != 16) {
                menu.setOption(i, getIBlock());
            }
        }

        menu.open(player);
    }

    private static ItemStack getIDeposit(String title, int amount) {
        ItemStack item = new ItemStack(Material.DIAMOND, amount);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(Utils.addColors(title));
        List<String> _lore = new ArrayList<>();
        _lore.add(Utils.addColors("&7Текущий баланс: &6666 люф"));
        _lore.add(Utils.addColors(""));
        _lore.add(Utils.addColors("&7Продать &b" + amount + " алмазов&7 и "));
        _lore.add(Utils.addColors("&7получить &e" + amount * 10 + " люф"));
        _lore.add(Utils.addColors(""));
        _lore.add(Utils.addColors("&eКлик чтобы продать"));
        m.setLore(_lore);
        m.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        item.setItemMeta(m);
        return item;
    }

    private static ItemStack getIBlock() {
        ItemStack itemBlock = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta imBlock = itemBlock.getItemMeta();
        imBlock.setDisplayName(Utils.addColors("х"));
        itemBlock.setItemMeta(imBlock);
        return itemBlock;
    }

    private boolean doExchange(Player player, int count) {
        try {
            if (player.getInventory().contains(Material.DIAMOND)) {
                for (ItemStack item : player.getInventory()) {
                    if (item != null && item.getType() == Material.DIAMOND) {
                        if (item.getAmount() >= count) {
                            item.setAmount(item.getAmount() - count);
                            EconomyBridge.giveMoney(player, 10L * count);
                            return false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
