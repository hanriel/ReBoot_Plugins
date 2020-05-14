package com.hanriel.reboot_core.command;

import com.hanriel.reboot_core.*;
import com.hanriel.reboot_core.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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

            sender.sendMessage(Utils.addColors("&7--------------------&eReBoot &4Core&7-------------------"));
            sender.sendMessage(Utils.addColors("Type \"/core help\" for help"));
            return;
        }

        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(Utils.addColors(Core.CHAT_PREFIX + "Команды:"));
            sender.sendMessage(Utils.addColors("&f/" + label + "&7 - основная команда для работы с ядром."));
            sender.sendMessage(Utils.addColors("&f/" + label + " help&7 - справка о командах плагина"));

            if (sender.hasPermission(Permissions.COMMAND_ADMIN)) {
                sender.sendMessage(Utils.addColors("&f/" + label + " reload&7 - Перезагрузить конфигурации плагина."));
                sender.sendMessage(Utils.addColors("&aВерсия: &7" + Core.getInstance().getDescription().getVersion()));
                sender.sendMessage(Utils.addColors("&aРазрабочики: &7Hanriel"));
            }
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.COMMAND_ADMIN),
                    Utils.addColors(Core.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            Core.getInstance().load();
            sender.sendMessage(Utils.addColors(Core.CHAT_PREFIX + "Конфигурации перезагружены. (наверное без ошибок)"));
            return;
        }

        sender.sendMessage(ChatColor.RED + "Unknown sub-command \"" + args[0] + "\".");
    }
}
