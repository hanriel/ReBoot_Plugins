package com.skyandforest.reboot_tech.machines;

import com.skyandforest.reboot_tech.Machine;
import com.skyandforest.reboot_tech.util.Holograms;
import org.bukkit.*;

import java.util.ArrayList;
import java.util.List;

public class Query extends Machine {

    public List<Holograms> lentitylist;

    public Query(Location loc) {
        super(ChatColor.GREEN + "Query", loc);
    }

    @Override
    public void onPlace() {
        lentitylist = new ArrayList<>();
        Location loc = new Location(location.getWorld(),
                location.getBlockX() + 0.5 + 6 * Math.round(location.getDirection().getX()),
                location.getBlockY(),
                location.getBlockZ() + 0.5 + 6 * Math.round(location.getDirection().getZ()));

//        createArm(loc.getX(), loc.getY(), loc.getZ(), loc.getWorld(), Material.EMERALD_BLOCK);  //LOCATION 0 arm

        pints5(loc);
        loc.add(0, 3, 0);
        pints5(loc);

        for (Holograms value : lentitylist) value.showAll();
        holo.showAll();
    }

    @Override
    public void onDestroy() {
        for (Holograms value : lentitylist) value.hideAll();
        holo.hideAll();
        Bukkit.getServer().getScheduler().cancelTask(mainCycle);
    }

    private void pints5(Location loc0) {
        Location loc1;

        loc1 = loc0.clone().add(5.5, 0, 5.5);
        create4arms(loc0, loc1);

        loc1 = loc0.clone().add(4.5, 0, 5.5);
        create4arms(loc0, loc1);

        loc1 = loc0.clone().add(3.5, 0, 5.5);
        create4arms(loc0, loc1);

        loc1 = loc0.clone().add(2.5, 0, 5.5);
        create4arms(loc0, loc1);

        loc1 = loc0.clone().add(1.5, 0, 5.5);
        create4arms(loc0, loc1);

        loc1 = loc0.clone().add(0.5, 0, 5.5);
        create4arms(loc0, loc1);

        loc1 = loc0.clone().add(5.5, 0, 4.5);
        create4arms(loc0, loc1);

        loc1 = loc0.clone().add(5.5, 0, 3.5);
        create4arms(loc0, loc1);

        loc1 = loc0.clone().add(5.5, 0, 2.5);
        create4arms(loc0, loc1);

        loc1 = loc0.clone().add(5.5, 0, 1.5);
        create4arms(loc0, loc1);

        loc1 = loc0.clone().add(5.5, 0, 0.5);
        create4arms(loc0, loc1);

    }

    private void create4arms(Location loc0, Location loc1) {
        createArm(loc1.getX(), loc1.getY(), loc1.getZ(), loc1.getWorld(), Material.REDSTONE_BLOCK);
        createArm(loc0.getX() - (loc1.getX() - loc0.getX()), loc1.getY(), loc1.getZ(), loc1.getWorld(), Material.REDSTONE_BLOCK);
        createArm(loc1.getX(), loc1.getY(), loc0.getZ() - (loc1.getZ() - loc0.getZ()), loc1.getWorld(), Material.REDSTONE_BLOCK);
        createArm(loc0.getX() - (loc1.getX() - loc0.getX()), loc1.getY(), loc0.getZ() - (loc1.getZ() - loc0.getZ()), loc1.getWorld(), Material.REDSTONE_BLOCK);
    }

    private void createArm(double x, double y, double z, World world, Material mat) {
        Holograms holo = new Holograms(new Location(world, x, y, z), mat);
        lentitylist.add(holo);
    }

}
