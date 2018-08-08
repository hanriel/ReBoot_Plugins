package com.saf.medievalnpc;

import com.saf.reboot_core.util.Utils;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class _CommandExecutor implements CommandExecutor {

    _CommandExecutor() {
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        int argCount = args.length;
        switch (cmd.getName()) {
            case "mnpc":
                s.sendMessage("NPC??");
                if (argCount >= 1) {
                    switch (args[0]) {
                        case "list":
//                            for (HashMap.Entry<Double[], EntityPlayer> npc : NPC.npcs.entrySet()) {
//                                msg = new String[2];
//                                msg[0] = Utils.translateColor("&3#&f" + npc.getKey()[0].toString() + " &3Name: &f" + npc.getValue().displayName);
//                                msg[1] = Utils.translateColor("&3X: &f" + String.format("%(.2f", npc.getKey()[1]) + " &3Y: &f" + String.format("%(.2f", npc.getKey()[2]) + " &3Z: &f" + String.format("%(.2f", npc.getKey()[3]));
//                                s.sendMessage(msg);
//                            }
                            return true;
                        case "remove":
                            for (HashMap.Entry<Double[], EntityPlayer> npc : NPC.npcs.entrySet()) {
                                if (Objects.equals(npc.getKey()[0], Double.valueOf(args[1]))) {
                                    NPC.npcs.remove(npc.getKey());
                                    s.sendMessage(Utils.translateColor("&3NPC №&f" + npc.getKey()[0].toString() + "&3 successful remove...Rejoin please..."));
                                    return true;
                                }
                            }
                            s.sendMessage(Utils.translateColor("&3Oops...NPC №&f" + args[1] + "&3 not found O_o\r\nPlese use \"npc list\" for view a list of npc's"));
                            return false;
                        default:
                            NPC.npc = new DefNPC(args[0], ((Player) s).getLocation());
                            NPC.npc.spawn();
                            //NPC.npc.spawnFakePlayer();
                            return true;
                    }
                }
                break;


//                    /place p
//                    /place set NAME
//                    /place remove NAME
//                    /place setup NAME
//                    /place list
//                    /place NAME

            case "place":
                if (args.length >= 1) {
                    switch (args[0]) {
                        case "tp":
                            if(argCount == 2){
                                if(NPC.places.containsKey(args[1])){
                                    if (s instanceof Player) {
                                        ((Player) s).teleport(NPC.places.get(args[1]).getLocation());
                                        return true;
                                    }
                                } else {
                                    s.sendMessage(Utils.translateColor("&cInvalid arguments: place not found."));
                                }
                            } else if(argCount == 3 && Bukkit.getServer().getPlayer(args[2]) != null){
                                if(NPC.places.containsKey(args[1])){
                                    Bukkit.getServer().getPlayer(args[2]).teleport(NPC.places.get(args[1]).getLocation());
                                    return true;
                                } else {
                                    s.sendMessage(Utils.translateColor("&cInvalid arguments: place not found."));
                                }

                            }
                            s.sendMessage(Utils.translateColor("&cInvalid argument."));
                            return false;
                        case "list":
                            for (Map.Entry<String, Place> entry: NPC.places.entrySet()) {
                                String[] msg = new String[2];
                                msg[0] = Utils.translateColor("===&3Name: &f" + entry.getValue().getName());
                                msg[1] = Utils.translateColor(
                                        "&3X: &f" + String.format("%(.2f", entry.getValue().getLocation().getX()) +
                                        " &3Y: &f" + String.format("%(.2f", entry.getValue().getLocation().getY()) +
                                        " &3Z: &f" + String.format("%(.2f", entry.getValue().getLocation().getZ()));
                                s.sendMessage(msg);
                            }
                            return true;
                        case "set":
                            Location loc;
                            if (argCount == 2) {
                                if (s instanceof Player) {
                                    loc = ((Player) s).getLocation();
                                    loc.setYaw(0f);
                                    loc.setPitch(0f);
                                } else {
                                    s.sendMessage(Utils.translateColor("&cInvalid arguments: X Y Z [World]..."));
                                    return false;
                                }
                            } else if (argCount == 6) {
                                if (Bukkit.getWorld(args[5]) == null) {
                                    s.sendMessage(Utils.translateColor("&cInvalid argument: World"));
                                    return false;
                                }

                                World w = Bukkit.getWorld(args[5]);
                                loc = new Location(
                                        w,
                                        Double.parseDouble(args[2]),
                                        Double.parseDouble(args[3]),
                                        Double.parseDouble(args[4])
                                );
                            } else {
                                s.sendMessage(Utils.translateColor("&cInvalid arguments: X Y Z [World]"));
                                return false;
                            }
                            try{
                                Place p = new Place(args[1],loc);
                                NPC.places.put(args[1], p);
                                s.sendMessage(Utils.translateColor("&aNew point created successfully."));
                            } catch (Exception e){
                                e.printStackTrace();
                                return false;
                            }
                            return true;
                        case "remove":
                            if (argCount == 2 && NPC.places.containsKey(args[1])) {
                                NPC.places.get(args[1]).setEnabled(false);
                                NPC.places.remove(args[1]);
                                s.sendMessage(Utils.translateColor("&aThe point was deleted successfully."));
                                return true;
                            }
                        s.sendMessage(Utils.translateColor("&cInvalid argument."));
                        return false;
                        case "setup":
                            if (argCount > 1 && NPC.places.containsKey(args[1])) {
                                if (argCount == 2) {
                                    Place p = NPC.places.get(args[1]);
                                    String[] _msg = new String[5];
                                    _msg[0] = Utils.translateColor("&7-------------------&aMedieval&c Places&7-------------------");
                                    _msg[1] = Utils.translateColor("Name: &a" + p.getName());
                                    _msg[2] = Utils.translateColor("Loc: &a" + p.getLocation().toString());
                                    _msg[3] = Utils.translateColor("Enabled: &a" + p.isEnabled().toString());
                                    _msg[4] = Utils.translateColor("&7-----------------------------------------------------");
                                    s.sendMessage(_msg);
                                    return true;
                                }
                                switch (args[2]){
                                    case "loc":
                                        if (argCount>6) {
                                            if (Bukkit.getWorld(args[6]) == null) {
                                                s.sendMessage(Utils.translateColor("&сInvalid argument: World"));
                                                return false;
                                            }

                                            World w = Bukkit.getWorld(args[6]);
                                            loc = new Location(
                                                    w,
                                                    Double.parseDouble(args[3]),
                                                    Double.parseDouble(args[4]),
                                                    Double.parseDouble(args[5])
                                            );
                                            NPC.places.get(args[1]).setLocation(loc);
                                        } else if (argCount==3)
                                            s.sendMessage(NPC.places.get(args[1]).getLocation().toString());

                                        return true;
                                    case "enabled":
                                        if (argCount>3)
                                            NPC.places.get(args[1]).setEnabled(Boolean.getBoolean(args[3]));
                                        else
                                            s.sendMessage(NPC.places.get(args[1]).isEnabled().toString());
                                        return true;
                                    case "name":
                                        return true;
                                    default:
                                        s.sendMessage(Utils.translateColor("&cInvalid argument: " + args[2]));
                                        return false;
                                }
                            }
                            return true;
                        default:
                            s.sendMessage(Utils.translateColor("&cInvalid argument: " + args[0]));
                            return false;
                    }
                }
                break;
            default:
                break;

        }
        return false;
    }
}