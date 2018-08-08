package com.saf.medievalnpc;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketReader {

    Player player;
    Channel channel;

    public PacketReader(Player player) {
        this.player = player;
    }

    public void inject(){
        CraftPlayer cPlayer = (CraftPlayer)this.player;
        channel = cPlayer.getHandle().playerConnection.networkManager.channel;
        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {@Override protected void decode(ChannelHandlerContext arg0, Packet<?> packet, List<Object> arg2) throws Exception {arg2.add(packet);readPacket(packet);}});
    }

    public void uninject(){
        if(channel.pipeline().get("PacketInjector") != null){
            channel.pipeline().remove("PacketInjector");
        }
    }


    public void readPacket(Packet<?> packet){
        if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")){
            int id = (Integer)getValue(packet, "a");
            Collection<Player> pl = Bukkit.getWorld("world").getEntitiesByClass(Player.class);
            Player p = Bukkit.getPlayer("CreeperMenn");

            for(Player pl2 : pl) if(pl2.getEntityId() == id && pl2.getUniqueId() == UUID.fromString("00000000-0000-0000-0000-000000000001")) p = pl2.getPlayer();

            System.out.println(getValue(packet, "action").toString());
            System.out.println(id);
            //System.out.println(NPC.npcs.get(1).getId());
            System.out.println("КОЛ_ВО: " + Bukkit.getWorld("world").getEntities().size());


            if(p.getDisplayName() == "123"){
                System.out.println("ТУТ НАЧАЛО");
                if(getValue(packet, "action").toString().equalsIgnoreCase("ATTACK")){
                    System.out.println("ТУТ БЬЮТ");
                    NPC.npc.animation(1);
                }else if(getValue(packet, "action").toString().equalsIgnoreCase("INTERACT")){
                    System.out.println("ТУТ ТЫКАЮТ");
                    player.openInventory(player.getEnderChest());
                }
            }

        }
    }

    public void setValue(Object obj,String name,Object value){
        try{
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        }catch(Exception e){}
    }

    public Object getValue(Object obj,String name){
        try{
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        }catch(Exception e){}
        return null;
    }
}
