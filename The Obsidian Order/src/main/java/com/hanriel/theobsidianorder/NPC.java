package com.hanriel.theobsidianorder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPC {
    private static final List<EntityPlayer> npcList = new ArrayList<>();

    public static void createNPC(Player player, String skin) {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) Bukkit.getWorld(player.getWorld().getName())).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), ChatColor.GOLD + "" + ChatColor.BOLD + "Банкир");
        EntityPlayer npc = new EntityPlayer(nmsServer, world, gameProfile, new PlayerInteractManager(world));

        npc.setLocation(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());

        String[] name = getSkin(player, skin);
        gameProfile.getProperties().put("textures", new Property("textures", name[0], name[1]));

        addNPCPacket(npc);
        npcList.add(npc);

        int var = 1;
        if (TheObsidianOrder.getNPCList().contains("data"))
            var = TheObsidianOrder.getNPCList().getConfigurationSection("data").getKeys(false).size() + 1;
        TheObsidianOrder.getNPCList().set("data." + var + ".x", player.getLocation().getX());
        TheObsidianOrder.getNPCList().set("data." + var + ".y", player.getLocation().getY());
        TheObsidianOrder.getNPCList().set("data." + var + ".z", player.getLocation().getZ());
        TheObsidianOrder.getNPCList().set("data." + var + ".pitch", player.getLocation().getPitch());
        TheObsidianOrder.getNPCList().set("data." + var + ".yaw", player.getLocation().getYaw());
        TheObsidianOrder.getNPCList().set("data." + var + ".world", player.getLocation().getWorld().getName());

        TheObsidianOrder.getNPCList().set("data." + var + ".name", gameProfile.getName());
        TheObsidianOrder.getNPCList().set("data." + var + ".text", name[0]);
        TheObsidianOrder.getNPCList().set("data." + var + ".sign", name[1]);
        TheObsidianOrder.saveNPCList();
    }

    public static void loadNPC(Location location, GameProfile profile){
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) location.getWorld()).getHandle();
        EntityPlayer npc = new EntityPlayer(nmsServer, world, profile, new PlayerInteractManager(world));

        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        addNPCPacket(npc);
        npcList.add(npc);
    }

    private static String[] getSkin(Player player, String name) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();

            URL url1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader1 = new InputStreamReader(url1.openStream());
            JsonObject property = new JsonParser().parse(reader1).getAsJsonObject().get("properties")
                    .getAsJsonArray().get(0).getAsJsonObject();
            String texture = property.get("value").getAsString();
            String signature = property.get("signature").getAsString();
            return new String[]{texture, signature};
        } catch (Exception e) {
            EntityPlayer p = ((CraftPlayer)player).getHandle();
            GameProfile profile = p.getProfile();
            Property property = profile.getProperties().get("textures").iterator().next();
            String texture = property.getValue();
            String signature = property.getSignature();
            return new String[]{texture, signature};
        }
    }

    public static void addNPCPacket(EntityPlayer npc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
        }
    }

    public static void addJoinPacket(Player player) {
        for (EntityPlayer npc : npcList) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
        }
    }

    public static List<EntityPlayer> getNPCs() {
        return npcList;
    }
}
