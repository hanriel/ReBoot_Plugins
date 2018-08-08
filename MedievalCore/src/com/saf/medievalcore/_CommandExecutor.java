package com.saf.medievalcore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class _CommandExecutor implements CommandExecutor {

    _CommandExecutor() {}

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        boolean error = false;
        switch (cmd.getName()) {
            case "medieval":
                switch (args.length){
                    case 0:
                        String[] msg = new String[4];

                        msg[0] = Utils.addColors("&7--------------------&2Medieval&4Core&7--------------------");
                        msg[1] = Utils.addColors("Version: &a" + Core.getPlugin(Core.class).getDescription().getVersion());
                        msg[2] = Utils.addColors("Hooked: &a");
                        msg[3] = Utils.addColors("&7----------------------------------------------------");
                        if(Catcher.mcoins) msg[2] += "MedievalCoins";
                        if(Catcher.mnpc) msg[2] += ", MedievalNPC";
                        if(Catcher.mgui) msg[2] += ", MedievalGUI";
                        s.sendMessage(msg);
                        break;
                    case 1:
                        switch (args[0]) {
                            case "reload":
                                error = true;
                                break;
                            default:
                                error = true;
                                break;
                        }
                        break;
                    default:
                        error = true;
                        break;
                }

            default:
                break;
        }
        if(error) s.sendMessage(Utils.addColors("&ePlease type \"&f/? MedievalCore\" &efor help."));
        return true;
    }
}
