package com.skyandforest.reboot_economy.command;

import com.skyandforest.reboot_core.command.CommandFramework;
import com.skyandforest.reboot_core.util.Utils;
import com.skyandforest.reboot_economy.Eco;
import com.skyandforest.reboot_economy.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EcoCommandHandler extends CommandFramework {

    private Eco eco;

    public EcoCommandHandler(String label) {
        super(label);
        eco = Eco.getInstance();
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

            long amount = Eco.asCopper(args[3],(long) CommandValidate.getPositiveDouble(args[2]));

            Eco.setBalance(target, amount);
            sender.sendMessage(Utils.addColors(Eco.CHAT_PREFIX + "Счёт игрока успешно становлен в: &e"+amount+" &aмеди."));
            return;
        }

        sender.sendMessage(ChatColor.RED + "Unknown sub-command \"" + args[0] + "\".");
    }
}