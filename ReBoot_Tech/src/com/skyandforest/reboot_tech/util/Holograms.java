package com.skyandforest.reboot_tech.util;

import com.skyandforest.reboot_tech.Tech;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class Holograms {

    private Material material = Material.AIR;
    private List<EntityArmorStand> entitylist = new ArrayList<>();
    private String[] Text;
    private Location location;
    private double DISTANCE = 0.25D;
    private int count;

    public Holograms(String[] Text, Location location) {
        this.Text = Text;
        this.location = location.clone();
        this.location.setX(this.location.getBlockX());
        this.location.setY(this.location.getBlockY());
        this.location.setZ(this.location.getBlockZ());
        this.location.add(0.5,-1+(Text.length*DISTANCE),0.5);
        create();
    }

    public Holograms(Location location, Material block) {
        this.location = location.clone();
        this.location.setX(this.location.getBlockX());
        this.location.setY(this.location.getBlockY());
        this.location.setZ(this.location.getBlockZ());
        this.location.add(0.5,-1,0.5);

        this.material = block;
        createBlock();
    }

    public void showPlayerTemp(final Player p,int Time){
        showPlayer(p);
        Bukkit.getScheduler().runTaskLater(Tech.getInstance(), () -> hidePlayer(p), Time);
    }


    public void showAllTemp(final Player p,int Time){
        showAll();
        Bukkit.getScheduler().runTaskLater(Tech.getInstance(), () -> hideAll(), Time);
    }

    private void showPlayer(Player p) {
        for (EntityArmorStand armor : entitylist) {
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(armor);
            PacketPlayOutEntityEquipment EquipP = new PacketPlayOutEntityEquipment(armor.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(new ItemStack(material)));
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(EquipP);
        }
    }

    private void hidePlayer(Player player) {
        for (EntityArmorStand armor : entitylist) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(armor.getId());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void showAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            showPlayer(player);
        }
    }

    public void hideAll() {
        for (Player player : Bukkit.getOnlinePlayers()) hidePlayer(player);
    }

    private void create() {
        for (String Text : this.Text) {
            EntityArmorStand entity = new EntityArmorStand(((CraftWorld) this.location.getWorld()).getHandle(),this.location.getX(), this.location.getY(),this.location.getZ());
            entity.setCustomName(new ChatComponentText(Text));
            entity.setCustomNameVisible(true);
            entity.setInvisible(true);
            entity.setNoGravity(true);
            entitylist.add(entity);
            this.location.subtract(0, this.DISTANCE, 0);
            count++;
        }

        for (int i = 0; i < count; i++) {
            this.location.add(0, this.DISTANCE, 0);
        }
        this.count = 0;
    }

    private void createBlock() {
        EntityArmorStand entity = new EntityArmorStand(((CraftWorld) this.location.getWorld()).getHandle(),this.location.getX(), this.location.getY(),this.location.getZ());
        entity.setCustomNameVisible(false);
        entity.setInvisible(true);
        entity.setNoGravity(true);
        entitylist.add(entity);
    }

}
