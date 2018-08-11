package com.skyandforest.reboot_economy.internal;

import com.skyandforest.reboot_economy.Eco;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public enum Variable {

    PLAYER("{player}") {
        public String getReplacement(Player executor) {
            return executor.getName();
        }
    },

    ONLINE("{online}") {
        public String getReplacement(Player executor) {
            return String.valueOf(Bukkit.getOnlinePlayers().size());
        }
    },

    MAX_PLAYERS("{max_players}") {
        public String getReplacement(Player executor) {
            return String.valueOf(Bukkit.getMaxPlayers());
        }
    },

    MONEY("{money}") {
        public String getReplacement(Player executor) {
            return Eco.formatBalance(Eco.getBalance(executor));
        }
    },

    WORLD("{world}") {
        public String getReplacement(Player executor) {
            return executor.getWorld().getName();
        }
    };

    private String text;

    private Variable(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public abstract String getReplacement(Player executor);
}