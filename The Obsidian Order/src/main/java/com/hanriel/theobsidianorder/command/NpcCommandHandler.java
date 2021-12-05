package com.hanriel.theobsidianorder.command;

import com.hanriel.theobsidianorder.NPC;
import com.hanriel.theobsidianorder.util.Permissions;
import com.hanriel.theobsidianorder.util.Tags;
import com.hanriel.theobsidianorder.util.Utils;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NpcCommandHandler extends CommandFramework {
    public NpcCommandHandler(String label) {
        super(label);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        CommandValidate.isTrue(
                sender.hasPermission(Permissions.CORE_ADMIN.getPerm()),
                Utils.addColorsTag(Tags.CORE, "&cУ вас недостаточно прав для выполнения данной команды.")
        );

        switch (args.length) {
            case 1: //Если 1 аргумент, то в свитче проверяем какой именно
                switch (args[0].toLowerCase()){
                    case "list":
                        for (EntityPlayer npc : NPC.getNPCs()) {
                            String[] msg = new String[2];
                            msg[0] = Utils.addColors("&3#&f" + npc.getUniqueIDString() + " &3Имя: &f" + npc.displayName);
                            msg[1] = Utils.addColors("&3X: &f" + String.format("%(.2f", npc.locX()) + " &3Y: &f" + String.format("%(.2f", npc.locY()) + " &3Z: &f" + String.format("%(.2f", npc.locZ()));
                            sender.sendMessage(msg);
                        }
                        break;
                    case "remove":
//                            for (EntityPlayer npc : DefNPC.getNPCs()) {
//                                if (Objects.equals(npc., Double.valueOf(args[1]))) {
//                                    NPC.npcList.remove(npc.getKey());
//                                    sender.sendMessage(Utils.addColors("&3NPC №&f" + npc.getKey()[0].toString() + "&3 successful remove...Rejoin please..."));
//                                    return true;
//                                }
//                            }
//                            sender.sendMessage(Utils.addColors("&3Oops...NPC №&f" + args[1] + "&3 not found O_o\r\nPlese use \"npc list\" for view a list of npc's"));
//                            return false;
                        break;
                    default:
                        Player player = (Player) sender;
                        NPC.createNPC(player, args[1]);
                        Utils.sendMsg(player, Tags.NPC + "Создан NCP с именем " + args[1]);
                }
                break;
            default:
                Utils.sendMsg(sender, "&cНеверное число аргументов. /core [help, reload]");
        }
    }
}
