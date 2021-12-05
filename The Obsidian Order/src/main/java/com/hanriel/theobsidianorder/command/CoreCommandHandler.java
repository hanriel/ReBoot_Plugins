package com.hanriel.theobsidianorder.command;

import com.hanriel.theobsidianorder.TheObsidianOrder;
import com.hanriel.theobsidianorder.util.Permissions;
import com.hanriel.theobsidianorder.util.Tags;
import com.hanriel.theobsidianorder.util.Utils;
import org.bukkit.command.CommandSender;

public class CoreCommandHandler extends CommandFramework {

    public CoreCommandHandler(String label) {
        super(label);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        //Проверям игрока на наличие доступа к команде "core.admin"
        CommandValidate.isTrue(
                sender.hasPermission(Permissions.CORE_ADMIN.getPerm()),
                Utils.addColorsTag(Tags.CORE, "&cУ вас недостаточно прав для выполнения данной команды.")
        );

        switch (args.length) {
            case 0: //Если ноль аргументов - то выводим информацию о плагине
                Utils.sendMsg(sender, "&7--------------------&eReBoot &4Core&7-------------------");
                Utils.sendMsg(sender, "&aВерсия: &7" + TheObsidianOrder.getInstance().getDescription().getVersion());
                Utils.sendMsg(sender, "&aРазрабочик: &7Hanriel");
                break;
            case 1: //Если 1 аргумент, то в свитче проверяем какой именно
                switch (args[0].toLowerCase()){
                    case "help":
                        Utils.sendMsg(sender, Tags.CORE + "Команды:");
                        Utils.sendMsg(sender, "&f/" + label + "&7 - основная команда для работы с ядром.");
                        Utils.sendMsg(sender, "&f/" + label + " help&7 - справка о командах плагина");
                        Utils.sendMsg(sender, "&f/" + label + " reload&7 - Перезагрузить конфигурации плагина.");
                        break;
                    case "reload":
                        TheObsidianOrder.getInstance().load();
                        Utils.sendMsg(sender, Tags.CORE + "Конфигурации перезагружены.");
                        break;
                    default:
                        Utils.sendMsg(sender, "&cНеизвестная подкоманда \"" + args[0] + "\".");
                }
                break;
            default:
                Utils.sendMsg(sender, "&cНеверное число аргументов. /core [help, reload]");
        }
    }
}
