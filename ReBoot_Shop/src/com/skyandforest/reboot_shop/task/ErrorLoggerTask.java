package com.skyandforest.reboot_shop.task;

import com.skyandforest.reboot_core.util.Utils;
import com.skyandforest.reboot_shop.ErrorLogger;
import com.skyandforest.reboot_shop.Shop;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;

public class ErrorLoggerTask implements Runnable {

    private ErrorLogger errorLogger;

    public ErrorLoggerTask(ErrorLogger errorLogger) {
        this.errorLogger = errorLogger;
    }

    @Override
    public void run() {

        List<String> lines = Utils.newArrayList();

        lines.add(" ");
        lines.add(ChatColor.RED + "#----------------------- RB Shop Errors -----------------------#");
        int count = 1;
        for (String error : errorLogger.getErrors()) {
            lines.add(ChatColor.GRAY + "" + (count++) + ") " + ChatColor.WHITE + error);
        }
        lines.add(ChatColor.RED + "#-------------------------------------------------------------#");

        String output = Utils.join(lines, "\n");

        if (Shop.getSettings().use_console_colors) {
            Bukkit.getConsoleSender().sendMessage(output);
        } else {
            System.out.println(ChatColor.stripColor(output));
        }
    }

}
