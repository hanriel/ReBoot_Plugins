package com.hanriel.theobsidianorder.command;

import com.hanriel.theobsidianorder.util.Permissions;
import com.hanriel.theobsidianorder.util.Tags;
import com.hanriel.theobsidianorder.Eco;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.hanriel.theobsidianorder.util.Utils.addColorsTag;
import static com.hanriel.theobsidianorder.util.Utils.sendMsg;

public class PayCommandHandler extends CommandFramework {

    public PayCommandHandler(String label) {
        super(label);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        CommandValidate.isTrue(
                sender.hasPermission(Permissions.ECO_BASE.getPerm()),
                addColorsTag(Tags.ECO,  "&cУ вас недостаточно прав для выполнения данной команды.")
        );

        CommandValidate.minLength(args, 2, "Usage: /" + label + " <игрок> <количество>");

        Player target;
        if (!(sender instanceof Player)) {
            sendMsg(sender,Tags.ECO + "&cЭтого нельзя сделать от имени консоли!");
            return;
        } else {
            target = Bukkit.getPlayerExact(args[0]);
        }

        CommandValidate.notNull(target, addColorsTag(Tags.ECO, "&cЭтот игрок не в сети!"));

        long amount = CommandValidate.getPositiveLong(args[1]);
        CommandValidate.isTrue(Eco.hasBalance((Player) sender, amount), addColorsTag(Tags.ECO, "У вас недостаточно средств для перевода!"));

        Eco.addBalance(target, amount);
        Eco.addBalance((Player) sender, -amount);

        target.sendMessage(addColorsTag(Tags.ECO,((Player) sender).getDisplayName() + "&a отправил вам: &e" + amount + " &aЛюф."));
        sendMsg(sender,Tags.ECO + "Перевод успешно отправлен.");
    }

}

