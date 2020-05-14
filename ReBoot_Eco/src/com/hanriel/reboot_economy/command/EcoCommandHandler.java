package com.hanriel.reboot_economy.command;

import com.hanriel.reboot_core.command.CommandFramework;
import com.hanriel.reboot_core.util.Utils;
import com.hanriel.reboot_economy.Eco;
import com.hanriel.reboot_economy.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EcoCommandHandler extends CommandFramework {

    public EcoCommandHandler(String label) {
        super(label);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.COMMAND_BASE),
                    Utils.addColors(Eco.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            CommandValidate.isTrue(
                    sender instanceof Player,
                    Utils.addColors(Eco.CHAT_PREFIX + "&cОператор, ты бомж, у тебя нет денег!")
            );

            long[] balance = Eco.getBalance((Player)sender);
            String[] msg = new String[5];
            msg[0] = Utils.addColors("&7--------------------&eТвой баланс&7---------------------");
//            msg[1] = Utils.addColors("     &bБриллиантов: " + ((Player)sender).getMetadata("d").get(0).asInt());
            msg[1] = Utils.addColors("     &eЗолотых: " + balance[2]);
            msg[2] = Utils.addColors("     &7Серебрянных: " + balance[1]);
            msg[3] = Utils.addColors("     &6Медных: " + balance[0]);
            msg[4] = Utils.addColors("&7----------------------------------------------------");
            sender.sendMessage(msg);
            return;
        }

        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(Eco.CHAT_PREFIX + "Команды:");
            sender.sendMessage(Utils.addColors("&f/" + label + "&7 - узнать баланс своего счёта."));
            sender.sendMessage(Utils.addColors("&f/pay&7 - передать средства другому игроку."));
            sender.sendMessage(Utils.addColors("&f/" + label + " help&7 - справка о командах плагина"));

            if (sender.hasPermission(Permissions.COMMAND_ADMIN)) {
                    sender.sendMessage(Utils.addColors("&f/" + label + " reload&7 - Перезагрузить конфигурации плагина."));
                    sender.sendMessage(Utils.addColors("&aВерсия: &7" + Eco.getInstance().getDescription().getVersion()));
                    sender.sendMessage(Utils.addColors("&aРазрабочики: &7CMen_"));
            }
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.COMMAND_ADMIN),
                    Utils.addColors(Eco.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            Eco.getInstance().load();
            sender.sendMessage(Eco.CHAT_PREFIX + "Конфигурации перезагружены. (наверное без ошибок)");
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.COMMAND_ADMIN),
                    Utils.addColors(Eco.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            CommandValidate.minLength(args, 4, "Usage: /" + label + " add <player> <amount> <currency>");

            Player target = Bukkit.getPlayerExact(args[1]);
            CommandValidate.notNull(target, Utils.addColors(Eco.CHAT_PREFIX + "&сЭтот игрок не в сети!"));

            long amount = Eco.asCopper(args[3],(long) CommandValidate.getPositiveDouble(args[2]));

            Eco.addBalance(target, amount, true);
            sender.sendMessage(Utils.addColors(Eco.CHAT_PREFIX + "Счёт игрока успешно пополнен на: &e"+amount+" &aмеди."));
            return;
        }

        if (args[0].equalsIgnoreCase("set")) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.COMMAND_ADMIN),
                    Utils.addColors(Eco.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            CommandValidate.minLength(args, 4, "Usage: /" + label + " set <player> <amount> <currency>");

            Player target = Bukkit.getPlayerExact(args[1]);
            CommandValidate.notNull(target, Utils.addColors(Eco.CHAT_PREFIX + "&сЭтот игрок не в сети!"));

            long amount = Eco.asCopper(args[3],(long) CommandValidate.getPositiveDouble(args[2]));

            Eco.setBalance(target, amount);
            sender.sendMessage(Utils.addColors(Eco.CHAT_PREFIX + "Счёт игрока успешно становлен в: &e"+amount+" &aмеди."));
            target.sendMessage(Utils.addColors(Eco.CHAT_PREFIX + ("&aВаш счёт установлен в: &e" + amount + " &aмеди.")));
            return;
        }

        sender.sendMessage(ChatColor.RED + "Неизвестаная саб-команда \"" + args[0] + "\".");
    }
}