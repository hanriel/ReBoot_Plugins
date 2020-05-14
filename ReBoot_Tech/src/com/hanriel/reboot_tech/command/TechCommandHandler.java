package com.hanriel.reboot_tech.command;

import com.hanriel.reboot_core.command.CommandFramework;
import com.hanriel.reboot_core.util.Utils;
import com.hanriel.reboot_tech.Permissions;
import com.hanriel.reboot_tech.Tech;
import com.hanriel.reboot_tech.machines.QueryGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class TechCommandHandler extends CommandFramework {

    public TechCommandHandler(String label) {
        super(label);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Неизвестаная саб-команда.");
            return;
//            CommandValidate.isTrue(
//                    sender.hasPermission(Permissions.COMMAND_BASE),
//                    Utils.addColors(Tech.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
//            );
//
//            CommandValidate.isTrue(
//                    sender instanceof Player,
//                    Utils.addColors(Tech.CHAT_PREFIX + "&cОператор, ты бомж, у тебя нет денег!")
//            );
//
//            long[] balance = Tech.getBalance((Player)sender);
//            String[] msg = new String[5];
//            msg[0] = Utils.addColors("&7--------------------&eТвой баланс&7---------------------");
////            msg[1] = Utils.addColors("     &bБриллиантов: " + ((Player)sender).getMetadata("d").get(0).asInt());
//            msg[1] = Utils.addColors("     &eЗолотых: " + balance[2]);
//            msg[2] = Utils.addColors("     &7Серебрянных: " + balance[1]);
//            msg[3] = Utils.addColors("     &6Медных: " + balance[0]);
//            msg[4] = Utils.addColors("&7----------------------------------------------------");
//            sender.sendMessage(msg);
//            return;
        }

        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(Tech.CHAT_PREFIX + "Команды:");
//            sender.sendMessage(Utils.addColors("&f/" + label + "&7 - узнать баланс своего счёта."));
//            sender.sendMessage(Utils.addColors("&f/pay&7 - передать средства другому игроку."));
            sender.sendMessage(Utils.addColors("&f/" + label + " help&7 - справка о командах плагина"));

            if (sender.hasPermission(Permissions.COMMAND_ADMIN)) {
                sender.sendMessage(Utils.addColors("&f/" + label + " reload&7 - Перезагрузить конфигурации плагина."));
                sender.sendMessage(Utils.addColors("&aВерсия: &7" + Tech.getInstance().getDescription().getVersion()));
                sender.sendMessage(Utils.addColors("&aРазработчики: &7CMen_"));
            }
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.COMMAND_ADMIN),
                    Utils.addColors(Tech.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            Tech.getInstance().load();
            sender.sendMessage(Tech.CHAT_PREFIX + "Конфигурации перезагружены. (наверное без ошибок)");
            return;
        }

        if (args[0].equalsIgnoreCase("query")) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.COMMAND_ADMIN),
                    Utils.addColors(Tech.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            ItemStack stack = new ItemStack(Material.FURNACE, 1);
            ItemMeta meta = stack.getItemMeta();

            meta.setDisplayName("Super Query");
            ArrayList<String> loreList = new ArrayList<>();
            loreList.add("Query");
            meta.setLore(loreList);
            stack.setItemMeta(meta);

            ((Player)sender).getInventory().addItem(stack);

            return;
        }

        if (args[0].equalsIgnoreCase("i")) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.COMMAND_ADMIN),
                    Utils.addColors(Tech.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            QueryGUI queryGUI = new QueryGUI();
            queryGUI.openInventory((Player) sender);
            queryGUI.initializeItems();

            return;
        }

        sender.sendMessage(ChatColor.RED + "Неизвестаная саб-команда \"" + args[0] + "\".");
    }
}
