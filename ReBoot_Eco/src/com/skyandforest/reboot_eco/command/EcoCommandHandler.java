package com.skyandforest.reboot_eco.command;

import com.saf.medievalcore.*;
import com.skyandforest.reboot_eco.Eco;
import com.skyandforest.reboot_eco.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
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
            sender.sendMessage(ChatColor.WHITE + "/" + label + ChatColor.GRAY + " - узнать баланс своего счёта.");
            sender.sendMessage(ChatColor.WHITE + "/pay" +ChatColor.GRAY + " - передать средства другому игроку.");

            if(args.length == 2 && args[1].equalsIgnoreCase("admin")){
                    CommandValidate.isTrue(
                            sender.hasPermission(Permissions.COMMAND_ADMIN),
                            Utils.addColors(Eco.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
                    );

                    sender.sendMessage(ChatColor.WHITE + "/" + label + " reload" + ChatColor.GRAY + " - Перезагрузить конфигурации плагина.");
                    sender.sendMessage(ChatColor.GREEN + "Версия: " + ChatColor.GRAY + Eco.getInstance().getDescription().getVersion());
                    sender.sendMessage(ChatColor.GREEN + "Разрабочики: " + ChatColor.GRAY + "CMen_");
            }


            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.COMMAND_ADMIN),
                    Utils.addColors(Eco.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            ErrorLogger errorLogger = new ErrorLogger();
            Eco.getInstance().load(errorLogger);
            Eco.setLastReloadErrors(errorLogger.getSize());

            if (!errorLogger.hasErrors()) {
                sender.sendMessage(Eco.CHAT_PREFIX + "Конфигурации перезагружены.");
            } else {
                new ErrorLoggerTask(errorLogger).run();
                sender.sendMessage(Eco.CHAT_PREFIX + ChatColor.RED + "Конфигурации перезагружены, встречено: " + errorLogger.getSize() + " ошибок.");
                if (!(sender instanceof ConsoleCommandSender)) {
                    sender.sendMessage(Eco.CHAT_PREFIX + ChatColor.RED + "Пожайлуста загляни в консоль.");
                }
            }
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

            long amount = Eco.getCopper(args[3],(long) CommandValidate.getPositiveDouble(args[2]));

            Eco.addBalance(target, amount);
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

            long amount = Eco.getCopper(args[3],(long) CommandValidate.getPositiveDouble(args[2]));

            Eco.setBalance(target, amount);
            sender.sendMessage(Utils.addColors(Eco.CHAT_PREFIX + "Счёт игрока успешно становлен в: &e"+amount+" &aмеди."));
            return;
        }

        sender.sendMessage(ChatColor.RED + "Unknown sub-command \"" + args[0] + "\".");
    }
}