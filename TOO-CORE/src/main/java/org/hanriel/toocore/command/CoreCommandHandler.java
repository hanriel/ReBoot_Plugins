package org.hanriel.toocore.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.hanriel.toocore.Permissions;
import org.hanriel.toocore.TOO_CORE;
import org.hanriel.toocore.util.Utils;

public class CoreCommandHandler extends CommandFramework {

    public CoreCommandHandler(String label) {
        super(label);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.COMMAND_BASE),
                    Utils.addColors(TOO_CORE.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            sender.sendMessage(Utils.addColors("&7--------------------&eReBoot &4Core&7-------------------"));
            sender.sendMessage(Utils.addColors("Type \"/core help\" for help"));
            return;
        }

        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(Utils.addColors(TOO_CORE.CHAT_PREFIX + "Команды:"));
            sender.sendMessage(Utils.addColors("&f/" + label + "&7 - основная команда для работы с ядром."));
            sender.sendMessage(Utils.addColors("&f/" + label + " help&7 - справка о командах плагина"));

            if (sender.hasPermission(Permissions.COMMAND_ADMIN)) {
                sender.sendMessage(Utils.addColors("&f/" + label + " reload&7 - Перезагрузить конфигурации плагина."));
                sender.sendMessage(Utils.addColors("&aВерсия: &7" + TOO_CORE.getInstance().getDescription().getVersion()));
                sender.sendMessage(Utils.addColors("&aРазрабочики: &7Hanriel"));
            }
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.COMMAND_ADMIN),
                    Utils.addColors(TOO_CORE.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            TOO_CORE.getInstance().load();
            sender.sendMessage(Utils.addColors(TOO_CORE.CHAT_PREFIX + "Конфигурации перезагружены. (наверное без ошибок)"));
            return;
        }

        sender.sendMessage("&aUnknown sub-command \"" + args[0] + "\".");
    }
}
