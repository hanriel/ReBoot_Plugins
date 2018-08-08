package com.saf.medievalgui;

import com.saf.reboot_core.Core;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import com.saf.reboot_core.Log;

public class GUI extends JavaPlugin {

    public static GUI plugin;

    @Override
    public void onEnable() {
        try {
            plugin = this;
            new _Listener();

            Log.success("The " + getName() +" was successfully loaded!" + plugin.getDescription().getVersion());
        } catch (Exception e) {
            Log.warning("Error loading " + getName());
            e.printStackTrace();
            super.setEnabled(false);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args)
    {

        if (!(sender instanceof Player)) {sender.sendMessage("onlyPlayers"); return true; }
        if ((cmd.getName().equalsIgnoreCase("quest"))) {
            Player p = (Player) sender;
            String[] s = {"{\"text\":\"На поиски приключений!\",\"translateColor\":\"black\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tp -150922 219 86\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"Волшебный мир магии и парящих земель\",\"translateColor\":\"dark_red\"}]}}}",
                    "{clickEvent:{action:\"run_command\",value:\"/tp 8114 246 -12381\"},hoverEvent:{action:\"show_text\",value:\"\u00A7bБрррр... тут холодно(>..<)''\"},translateColor:black,text:\"За плохое поведение ? ПОКАРАН!\"}",
                    "{clickEvent:{action:\"run_command\",value:\"/tp 352 64 -658\"},hoverEvent:{action:\"show_text\",value:\"\u00A71Ухты! как же много воды.\n\u00A71А её можно пить?\"},translateColor:black,text:\"Маленький портовый городишка \"}",
                    "{clickEvent:{action:\"run_command\",value:\"/tp -254 112.6 247\"},hoverEvent:{action:\"show_text\",value:\"Хочу домой! В свою любимую кроватку, под тёплое одеялко._.\"},translateColor:black,text:\"Домой!\"}"};
            openBook(book("TEST", "MED", s), p);
        } else if (cmd.getName().equalsIgnoreCase("health")){
            if (args.length == 1){
                ((Player)sender).setMetadata("m", new FixedMetadataValue(Core.plugin, args[0]));
            }
        }



        return true;
    }

    private void openBook(org.bukkit.inventory.ItemStack book, Player p) {
        int slot = p.getInventory().getHeldItemSlot();
        org.bukkit.inventory.ItemStack old = p.getInventory().getItem(slot);
        p.getInventory().setItem(slot, book);

        ByteBuf buf = Unpooled.buffer(256);
        buf.setByte(0, (byte)0);
        buf.writerIndex(1);

        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        p.getInventory().setItem(slot, old);
    }

    private static org.bukkit.inventory.ItemStack book(String title, String Shopor, String[] pages) {
        org.bukkit.inventory.ItemStack is = new org.bukkit.inventory.ItemStack(Material.WRITTEN_BOOK, 1);
        net.minecraft.server.v1_12_R1.ItemStack nmsis = CraftItemStack.asNMSCopy(is);
        NBTTagCompound bd = new NBTTagCompound();
        bd.setString("title", title);
        bd.setString("Shopor", Shopor);
        NBTTagList bp = new NBTTagList();
        for (String text : pages) {
            bp.add(new NBTTagString(text));
        }
        bd.set("pages", bp);
        nmsis.setTag(bd);
        is = CraftItemStack.asBukkitCopy(nmsis);
        return is;
    }

}
