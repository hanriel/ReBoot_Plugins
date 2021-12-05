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

            sender.sendMessage(Utils.addColors(Eco.CHAT_PREFIX + "&eТвой баланс: &7" + Eco.getBalance((Player)sender) + " Люф"));
            return;
        }

        if (args[0].equalsIgnoreCase("help")) {
            String[] message = new String[7];
            message[0] = Eco.CHAT_PREFIX + "Информация:";
            message[1] = Utils.addColors("    &aВерсия: &7" + Eco.getInstance().getDescription().getVersion());
            message[2] = Utils.addColors("    &aРазрабочик: &7Hanriel");
            message[3] = Eco.CHAT_PREFIX + "Команды:";
            message[4] = Utils.addColors("&f/" + label + "&7 - узнать баланс своего счёта.");
            message[5] = Utils.addColors("&f/pay&7 - передать средства другому игроку.");
            message[6] = Utils.addColors("&f/" + label + " help&7 - справка о командах плагина");

            sender.sendMessage(message);

            if (sender.hasPermission(Permissions.COMMAND_ADMIN)) {
                message = new String[1];
                message[0] = Utils.addColors("&f/" + label + " reload&7 - Перезагрузить конфигурации плагина.");

                sender.sendMessage(message);
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

            CommandValidate.minLength(args, 3, "Usage: /" + label + " add <игрок> <количество>");

            Player target = Bukkit.getPlayerExact(args[1]);
            CommandValidate.notNull(target, Utils.addColors(Eco.CHAT_PREFIX + "&сЭтот игрок не в сети!"));

            long amount = CommandValidate.getPositiveLong(args[2]);

            Eco.addBalance(target, amount);
            sender.sendMessage(Utils.addColors(Eco.CHAT_PREFIX + "Счёт игрока успешно пополнен на: &e"+amount+" &aЛюфов."));
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

            long amount = CommandValidate.getPositiveLong(args[2]);

            Eco.setBalance(target, amount);
            sender.sendMessage(Utils.addColors(Eco.CHAT_PREFIX + "Счёт игрока успешно становлен в: &e"+amount+" &aЛюф."));
            target.sendMessage(Utils.addColors(Eco.CHAT_PREFIX + ("&aВаш счёт установлен в: &e" + amount + " &aЛюф.")));
            return;
        }

        sender.sendMessage(ChatColor.RED + "Неизвестаная саб-команда \"" + args[0] + "\". Попробуй: /" + label +" help");
    }
}