package com.hanriel.reboot_dungeons;

import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.SkullType;
import org.bukkit.block.Banner;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Furnace;
import org.bukkit.block.Jukebox;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.block.banner.Pattern;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class BlockSave {

    Location location = null;
    Material type = null;

    BlockState state = null;
    MaterialData data = null;

    ItemStack[] inventory = null;

    Integer brewTime = null;
    Integer fuelLevel = null;

    String name = null;
    String command = null;

    DyeColor baseColor = null;
    List<Pattern> patterns = null;

    EntityType creature = null;
    Integer delay = null;

    Short burnTime = null;
    Short cookTime = null;

    Material disc = null;

    Note note = null;

    String[] signText = null;

    BlockFace direction;
    SkullType skulltype;
    String owner;
    private Short layer;

    public BlockSave(Block block) {

        location = block.getLocation();
        type = block.getType();

        this.layer = 1;

        if(block.getState() instanceof BlockState) {
            BlockState state = (BlockState) block.getState();

            this.state = state;
            this.data = state.getData();
        }

        if(block.getState() instanceof InventoryHolder) {
            InventoryHolder holder = (InventoryHolder) block.getState();
            this.inventory = holder.getInventory().getContents();

            for(int i = 0; i < this.inventory.length; i++) {
                if(this.inventory[i] != null)
                    this.inventory[i] = new ItemStack(this.inventory[i]);

            }

        }

        if(block.getState() instanceof BrewingStand) {
            BrewingStand stand = (BrewingStand) block.getState();
            this.brewTime = stand.getFuelLevel();
            this.fuelLevel = stand.getBrewingTime();

        }

        if(block.getState() instanceof Banner) {
            Banner banner = (Banner) block.getState();
            this.baseColor = banner.getBaseColor();
            this.patterns = banner.getPatterns();
            this.layer = 2;
        }

        if(block.getState() instanceof CommandBlock) {
            CommandBlock command = (CommandBlock) block.getState();
            this.name = command.getName();
            this.command = command.getCommand();
        }

        if(block.getState() instanceof CreatureSpawner) {
            CreatureSpawner spawner = (CreatureSpawner) block.getState();
            this.creature = spawner.getSpawnedType();
            this.delay = spawner.getDelay();
        }

        if(block.getState() instanceof Furnace) {
            Furnace furnace = (Furnace) block.getState();
            this.burnTime = furnace.getCookTime();
            this.cookTime = furnace.getBurnTime();
        }

        if(block.getState() instanceof Jukebox) {
            Jukebox box = (Jukebox) block.getState();
            this.disc = box.getPlaying();
        }

        if(block.getState() instanceof NoteBlock) {
            NoteBlock note = (NoteBlock) block.getState();
            this.note = note.getNote();
        }

        if(block.getState() instanceof Sign) {
            Sign sign = (Sign) block.getState();
            this.signText = sign.getLines();
            this.layer = 2;
        }

        if(block.getState() instanceof Skull) {
            Skull skull = (Skull) block.getState();
            this.direction = skull.getRotation();
            this.skulltype = skull.getSkullType();
            this.owner = skull.getOwner();
        }
    }

    public void restoreBlock(Block block) {

        block.setType(this.type);


        if(block.getState() instanceof BlockState) {
            BlockState state = (BlockState) block.getState();
            if(!state.getType().equals(Material.TALL_GRASS) && !state.getType().equals(Material.LEGACY_REDSTONE_TORCH_OFF))
                state.setData(data);
            state.update();
        }

        if(block.getState() instanceof InventoryHolder) {
            InventoryHolder holder = (InventoryHolder) block.getState();

            for(ItemStack item : inventory) {
                if(item != null) {
                    holder.getInventory().addItem(item);
                }

            }
        }

        if(block.getState() instanceof BrewingStand) {
            BrewingStand stand = (BrewingStand) block.getState();

            stand.setBrewingTime(this.fuelLevel);
            stand.setFuelLevel(this.brewTime);
            stand.update();
        }

        if(block.getState() instanceof Banner) {
            Banner banner = (Banner) block.getState();
            banner.setBaseColor(this.baseColor);
            banner.setPatterns(this.patterns);
            banner.update();
        }

        if(block.getState() instanceof CommandBlock) {
            CommandBlock command = (CommandBlock) block.getState();

            command.setName(this.name);
            command.setCommand(this.command);
            command.update();
        }

        if(block.getState() instanceof CreatureSpawner) {
            CreatureSpawner spawner = (CreatureSpawner) block.getState();

            spawner.setSpawnedType(this.creature);
            spawner.setDelay(this.delay);
            spawner.update();
        }

        if(block.getState() instanceof Furnace) {
            Furnace furnace = (Furnace) block.getState();

            furnace.setBurnTime(this.burnTime);
            furnace.setCookTime(this.cookTime);
            furnace.update();
        }

        if(block.getState() instanceof Jukebox) {
            Jukebox box = (Jukebox) block.getState();

            box.setPlaying(this.disc);
            box.update();
        }

        if(block.getState() instanceof NoteBlock) {
            NoteBlock note = (NoteBlock) block.getState();

            note.setNote(this.note);
            note.update();
        }

        if(block.getState() instanceof Sign) {
            Sign sign = (Sign) block.getState();

            sign.setLine(0, this.signText[0]);
            sign.setLine(1, this.signText[1]);
            sign.setLine(2, this.signText[2]);
            sign.setLine(3, this.signText[3]);
            sign.update();
        }

        if(block.getState() instanceof Skull) {
            Skull skull = (Skull) block.getState();

            skull.setOwner(this.owner);
            skull.setRotation(this.direction);
            skull.setSkullType(this.skulltype);
            skull.update();
        }

        block.getState().update();

    }

    public void restoreBlock() {

        Block block = location.getWorld().getBlockAt(location);

        this.restoreBlock(block);;

    }

    public Location getLocation() {
        return this.location;
    }

    public Material getType() {
        return this.type;
    }

    public Short getLayer() {
        return this.layer;
    }

}