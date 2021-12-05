package com.hanriel.theobsidianorder.util;

import org.bukkit.permissions.Permission;

public enum Permissions {

    CORE_ADMIN(new Permission("core.admin")),
    ECO_BASE(new Permission("eco.base")),
    ECO_ADMIN(new Permission("eco.admin")),
    NPC_BASE(new Permission("npc.base")),
    NPC_ADMIN(new Permission("npc.admin")),
    SEE_ERRORS(new Permission("shop.errors")),
    SIGN_CREATE(new Permission("shop.sign")),
    SHOP_BASE(new Permission("shop.command.")),
    OPEN_MENU_BASE(new Permission("shop.open."));

    private Permission perm;

    Permissions(Permission perm) {
        this.perm = perm;
    }

    public Permission getPerm() {
        return perm;
    }
}
