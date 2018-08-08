package com.saf.medievalnpc;

import com.sun.istack.internal.NotNull;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import com.saf.medievalnpc.Holograms;

public class Place {

    private Location location;
    private String name;
    private boolean enabled = true;
    private Holograms holo;
    private String[] text = new String[2];

    /**
     * Initializes a new {@code Place} object.
     * @param name Place name
     * @param loc Location of place
     */
    public Place(String name, Location loc){
        this.name = name;
        this.location = loc.clone();
        text[0] = "§4Welcome to region!";
        text[1] = "§a" + name;
        holo = new Holograms(text, loc);
        Start();
    }

    /**Set's the name
     * @param arg1 Name
     */
    public void setName(@NotNull String arg1){
        this.name = arg1;
    }

    /**Returns the name of this place.
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**Set's the {@code Location} of place
     * @param arg1 {@code Location} of place
     */
    public void setLocation(@NotNull Location arg1){
        this.location = arg1;
    }

    /**Returns the {@code Locaton} of this place.
     * @return {@code Location} of this place
     */
    public Location getLocation() {
        return this.location;
    }

    /**Set's enabled of place
     * @param arg1 Enabled
     */
    public void setEnabled(@NotNull Boolean arg1){
        this.enabled = arg1;
    }

    /**Returns if enabled of place
     * @return isEnabled
     */
    public Boolean isEnabled() {
        return this.enabled;
    }

    /**
     *
     * @return Hologram
     */
    public Holograms getHolo(){
        return holo;
    }

    private void Start(){
        new BukkitRunnable() {
            public void run() {
                if (!enabled) cancel();//this.cancel();
                //Bukkit.broadcastMessage("Hello my friend!");
            }
        }.runTaskTimer(NPC.plugin, 0, 20);
    }

}
