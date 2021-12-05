package com.hanriel.reboot_npc;

import com.hanriel.reboot_core.command.CommandFramework;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class _CommandExecutor extends CommandFramework {

    public _CommandExecutor(String label) {
        super(label);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        int argCount = args.length;
        if (argCount >= 1) {
            switch (args[0]) {
                case "list":
//                            sender.sendMessage("List");
//                            for (HashMap.Entry<Double[], DefNPC> npc : NPC.npcList.entrySet()) {
//                                String[] msg = new String[2];
//                                msg[0] = Utils.addColors("&3#&f" + npc.getKey()[0].toString() + " &3Name: &f" + npc.getValue().toString());
//                                msg[1] = Utils.addColors("&3X: &f" + String.format("%(.2f", npc.getKey()[1]) + " &3Y: &f" + String.format("%(.2f", npc.getKey()[2]) + " &3Z: &f" + String.format("%(.2f", npc.getKey()[3]));
//                                sender.sendMessage(msg);
//                            }
//                            return true;
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
                default:
                    Player player = (Player) sender;
                    DefNPC.createNPC(player, args[1]);
                    player.sendMessage("NPC CREATED");
            }
        }
    }
}