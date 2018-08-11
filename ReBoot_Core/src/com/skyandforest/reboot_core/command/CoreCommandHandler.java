package com.skyandforest.reboot_core.command;

import com.skyandforest.reboot_core.*;
import com.skyandforest.reboot_core.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class CoreCommandHandler extends CommandFramework {

    public CoreCommandHandler(String label) {
        super(label);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.COMMAND_BASE),
                    Utils.addColors(Core.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            String[] msg = new String[3];
            msg[0] = Utils.addColors("&7--------------------&eReBoot &4Core&7-------------------");
            msg[1] = Utils.addColors("Version: &a" + Core.getPlugin(Core.class).getDescription().getVersion());
            msg[2] = Utils.addColors("&7----------------------------------------------------");
            sender.sendMessage(msg);
            return;
        }

        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(Core.CHAT_PREFIX + "Команды:");
            sender.sendMessage(Utils.addColors("&f/" + label + "&7 - основная команда для работы с ядром."));

            if (args.length == 2 && args[1].equalsIgnoreCase("admin")) {
                CommandValidate.isTrue(
                        sender.hasPermission(Permissions.COMMAND_ADMIN),
                        Utils.addColors(Core.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
                );
                sender.sendMessage(Utils.addColors("&f/" + label + " reload&7 - Перезагрузить конфигурации плагина."));
                sender.sendMessage(Utils.addColors("&aВерсия: &7" + Core.getInstance().getDescription().getVersion()));
                sender.sendMessage(Utils.addColors("&aРазрабочики: &7CMen_"));
            }
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.COMMAND_ADMIN),
                    Utils.addColors(Core.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            ErrorLogger errorLogger = new ErrorLogger();
            Core.getInstance().load(errorLogger);
            Core.setLastReloadErrors(errorLogger.getSize());

            if (!errorLogger.hasErrors()) {
                sender.sendMessage(Core.CHAT_PREFIX + "Конфигурации перезагружены.");
            } else {
                new ErrorLoggerTask(errorLogger).run();
                sender.sendMessage(Core.CHAT_PREFIX + ChatColor.RED + "Конфигурации перезагружены, встречено: " + errorLogger.getSize() + " ошибок.");
                if (!(sender instanceof ConsoleCommandSender)) {
                    sender.sendMessage(Core.CHAT_PREFIX + ChatColor.RED + "Пожайлуста загляни в консоль.");
                }
            }
            return;
        }

        sender.sendMessage(ChatColor.RED + "Unknown sub-command \"" + args[0] + "\".");
    }
}
