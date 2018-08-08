package com.skyandforest.reboot_eco.command;

import com.saf.medievalcore.CommandFramework;
import com.saf.medievalcore.Utils;
import com.skyandforest.reboot_eco.Eco;
import com.skyandforest.reboot_eco.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PayCommandHandler extends CommandFramework {

    public PayCommandHandler(String label) {
        super(label);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        CommandValidate.isTrue(
                sender.hasPermission(Permissions.COMMAND_BASE),
                Utils.addColors(Eco.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
        );

        CommandValidate.minLength(args, 3, "Usage: /" + label + " <player> <amount> <currency>");
        Player target = null;

        if (!(sender instanceof Player)) {
            CommandValidate.minLength(args, 3, Eco.CHAT_PREFIX + ChatColor.RED + "You must specify a player from the console.");
            return;
        } else {
            target = Bukkit.getPlayerExact(args[0]);
        }

        long amount = Eco.getCopper(args[2],(long) CommandValidate.getPositiveDouble(args[1]));

        CommandValidate.notNull(target, Utils.addColors(Eco.CHAT_PREFIX + "&сЭтот игрок не в сети!"));
        CommandValidate.isTrue(!Eco.hasMoney(target,  args[2], amount),Eco.CHAT_PREFIX + ChatColor.RED + "У вас недостаточно средств для перевода!");

        Eco.addBalance(target, amount);
        Eco.addBalance((Player) sender, -amount);
        target.sendMessage(Utils.addColors(Eco.CHAT_PREFIX + ((Player) sender).getDisplayName() + "&a отправил вам: &e" + amount + " &aмеди."));
        sender.sendMessage(Eco.CHAT_PREFIX + "Перевод успешно отправлен.");
    }

}

