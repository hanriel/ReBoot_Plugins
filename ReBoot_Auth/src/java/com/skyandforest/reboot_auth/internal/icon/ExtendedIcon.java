package com.skyandforest.reboot_auth.internal.icon;

import com.skyandforest.reboot_auth.Shop;
import com.skyandforest.reboot_auth.api.Icon;
import com.skyandforest.reboot_auth.bridge.EconomyBridge;
import com.skyandforest.reboot_auth.internal.ExtendedIconMenu;
import com.skyandforest.reboot_auth.internal.MenuInventoryHolder;
import com.skyandforest.reboot_auth.internal.RequiredItem;
import com.skyandforest.reboot_auth.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.util.List;

public class ExtendedIcon extends Icon {

    private String permission;
    private String permissionMessage;
    private String viewPermission;

    private boolean permissionNegated;
    private boolean viewPermissionNegated;

    private double moneyPrice;
    private int playerPointsPrice;
    private int expLevelsPrice;
    private RequiredItem requiredItem;

    public ExtendedIcon() {
        super();
    }

    public boolean canClickIcon(Player player) {
        if (permission == null) {
            return true;
        }

        if (permissionNegated) {
            return !player.hasPermission(permission);
        } else {
            return player.hasPermission(permission);
        }
    }

    public void setPermission(String permission) {
        if (StringUtils.isNullOrEmpty(permission)) {
            permission = null;
        }

        if (permission != null) {
            if (permission.startsWith("-")) {
                permissionNegated = true;
                permission = permission.substring(1, permission.length()).trim();
            }
        }
        this.permission = permission;
    }

    public String getPermissionMessage() {
        return permissionMessage;
    }

    public void setPermissionMessage(String permissionMessage) {
        this.permissionMessage = permissionMessage;
    }

    public boolean hasViewPermission() {
        return viewPermission != null;
    }

    public boolean canViewIcon(Player player) {
        if (viewPermission == null) {
            return true;
        }

        if (viewPermissionNegated) {
            return !player.hasPermission(viewPermission);
        } else {
            return player.hasPermission(viewPermission);
        }
    }

    public void setViewPermission(String viewPermission) {
        if (StringUtils.isNullOrEmpty(viewPermission)) {
            viewPermission = null;
        }

        if (viewPermission != null) {
            if (viewPermission.startsWith("-")) {
                viewPermissionNegated = true;
                viewPermission = viewPermission.substring(1, viewPermission.length()).trim();
            }
        }
        this.viewPermission = viewPermission;
    }

    public double getMoneyPrice() {
        return moneyPrice;
    }

    public void setMoneyPrice(double moneyPrice) {
        this.moneyPrice = moneyPrice;
    }

    public int getPlayerPointsPrice() {
        return playerPointsPrice;
    }

    public void setPlayerPointsPrice(int playerPointsPrice) {
        this.playerPointsPrice = playerPointsPrice;
    }

    public int getExpLevelsPrice() {
        return expLevelsPrice;
    }

    public void setExpLevelsPrice(int expLevelsPrice) {
        this.expLevelsPrice = expLevelsPrice;
    }

    public RequiredItem getRequiredItem() {
        return requiredItem;
    }

    public void setRequiredItem(RequiredItem requiredItem) {
        this.requiredItem = requiredItem;
    }

    public String calculateName(Player pov) {
        return super.calculateName(pov);
    }

    public List<String> calculateLore(Player pov) {
        return super.calculateLore(pov);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onClick(Player player) {

        // Check all the requirements.

        if (!canClickIcon(player)) {
            if (permissionMessage != null) {
                player.sendMessage(permissionMessage);
            } else {
                player.sendMessage(Shop.getLang().default_no_icon_permission);
            }
            return closeOnClick;
        }

        if (moneyPrice > 0) {
            if (!EconomyBridge.hasValidEconomy()) {
                player.sendMessage(ChatColor.RED + "This command has a price, but Vault with a compatible economy plugin was not found. For security, the command has been blocked. Please inform the staff.");
                return closeOnClick;
            }

            if (!EconomyBridge.hasMoney(player, moneyPrice)) {
                player.sendMessage(Shop.getLang().no_money.replace("{money}", EconomyBridge.formatMoney(moneyPrice)));
                return closeOnClick;
            }
        }

        // Take the money, the points and the required item.

        boolean changedVariables = false; // To update the placeholders.

        if (moneyPrice > 0) {
            if (!EconomyBridge.takeMoney(player, moneyPrice)) {
                player.sendMessage(ChatColor.RED + "Error: the transaction couldn't be executed. Please inform the staff.");
                return closeOnClick;
            }
            changedVariables = true;
        }

        if (expLevelsPrice > 0) {
            player.setLevel(player.getLevel() - expLevelsPrice);
        }

        if (requiredItem != null) {
            requiredItem.takeItem(player);
        }

        if (changedVariables) {
            InventoryView view = player.getOpenInventory();
            if (view != null) {
                Inventory topInventory = view.getTopInventory();
                if (topInventory.getHolder() instanceof MenuInventoryHolder) {
                    MenuInventoryHolder menuHolder = (MenuInventoryHolder) topInventory.getHolder();

                    if (menuHolder.getIconMenu() instanceof ExtendedIconMenu) {
                        ((ExtendedIconMenu) menuHolder.getIconMenu()).refresh(player, topInventory);
                    }
                }
            }
        }

        return super.onClick(player);
    }


}