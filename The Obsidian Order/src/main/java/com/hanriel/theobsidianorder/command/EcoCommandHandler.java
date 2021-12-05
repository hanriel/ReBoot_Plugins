package com.hanriel.theobsidianorder.command;

import com.hanriel.theobsidianorder.Eco;
import com.hanriel.theobsidianorder.TheObsidianOrder;
import com.hanriel.theobsidianorder.util.Permissions;
import com.hanriel.theobsidianorder.util.Tags;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.util.ArrayList;
import java.util.List;

import static com.hanriel.theobsidianorder.util.Utils.addColors;
import static com.hanriel.theobsidianorder.util.Utils.addColorsTag;
import static com.hanriel.theobsidianorder.util.Utils.sendMsg;

public class EcoCommandHandler extends CommandFramework {

    public EcoCommandHandler(String label) {
        super(label);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.ECO_BASE.getPerm()),
                    addColorsTag(Tags.ECO, "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            CommandValidate.isTrue(
                    sender instanceof Player,
                    addColorsTag(Tags.ECO, "&cОператор, ты бомж, у тебя нет денег!")
            );

            sendMsg(sender, Tags.ECO + "Твой баланс: &6" + Eco.getBalance((Player) sender) + " Люф");
            return;
        }

        if (args[0].equalsIgnoreCase("help")) {
            String[] message = new String[7];
            message[0] = addColorsTag(Tags.ECO, "Информация:");
            message[1] = addColors("    &aВерсия: &7" + TheObsidianOrder.getInstance().getDescription().getVersion());
            message[2] = addColors("    &aРазрабочик: &7Hanriel");
            message[3] = addColorsTag(Tags.ECO, "Команды:");
            message[4] = addColors("&f/" + label + "&7 - узнать баланс своего счёта.");
            message[5] = addColors("&f/pay&7 - передать средства другому игроку.");
            message[6] = addColors("&f/" + label + " help&7 - справка о командах плагина");

            sender.sendMessage(message);

            if (sender.hasPermission(Permissions.ECO_ADMIN.getPerm())) {
                message = new String[1];
                message[0] = addColors("&f/" + label + " reload&7 - Перезагрузить конфигурации плагина.");

                sender.sendMessage(message);
            }
            return;
        }

        if (args[0].equalsIgnoreCase("add")) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.ECO_ADMIN.getPerm()),
                    addColorsTag(Tags.ECO, "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            CommandValidate.minLength(args, 3, "Использование: /" + label + " add <игрок> <количество>");

            Player target = Bukkit.getPlayerExact(args[1]);
            CommandValidate.notNull(target, addColorsTag(Tags.ECO, "&сЭтот игрок не в сети!"));

            long amount = CommandValidate.getPositiveLong(args[2]);

            Eco.addBalance(target, amount);
            sender.sendMessage(addColorsTag(Tags.ECO, "Счёт игрока успешно пополнен на: &e" + amount + " &aЛюфов."));
            return;
        }

        if (args[0].equalsIgnoreCase("take")) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.ECO_ADMIN.getPerm()),
                    addColorsTag(Tags.ECO, "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            CommandValidate.minLength(args, 3, "Использование: /" + label + " take <игрок> <количество>");

            Player target = Bukkit.getPlayerExact(args[1]);
            CommandValidate.notNull(target, addColorsTag(Tags.ECO, "&сЭтот игрок не в сети!"));

            long amount = CommandValidate.getPositiveLong(args[2]);

            Eco.takeBalance(target, amount);
            sender.sendMessage(addColorsTag(Tags.ECO, "Счёт игрока успешно пополнен на: &e" + amount + " &aЛюфов."));
            return;
        }

        if (args[0].equalsIgnoreCase("set")) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.ECO_ADMIN.getPerm()),
                    addColorsTag(Tags.ECO, "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            CommandValidate.minLength(args, 4, "Использование: /" + label + " set <игрок> <количество> ");

            Player target = Bukkit.getPlayerExact(args[1]);
            CommandValidate.notNull(target, addColorsTag(Tags.ECO, "&сЭтот игрок не в сети!"));

            long amount = CommandValidate.getPositiveLong(args[2]);

            Eco.setBalance(target, amount);
            sendMsg(sender, Tags.ECO + "Счёт игрока успешно становлен в: &e" + amount + " &aЛюф.");
            sendMsg(target, Tags.ECO + "&aВаш счёт установлен в: &e" + amount + " &aЛюф.");
            return;
        }

        sendMsg(sender, "&cНеизвестаная под-команда \"" + args[0] + "\". Попробуй: /" + label + " help");
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("eco")) {
            List<String> l = new ArrayList<String>();

            if (args.length == 1) {
                l.add("1");
                l.add("2");
            } else if (args.length == 2) {
                l.add("a");
                l.add("b");
            }

            return l;
        }
        return null;
    }
}