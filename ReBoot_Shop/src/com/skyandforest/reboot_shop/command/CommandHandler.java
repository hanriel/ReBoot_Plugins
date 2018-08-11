package com.skyandforest.reboot_shop.command;

import com.skyandforest.reboot_core.util.Utils;
import com.skyandforest.reboot_economy.Eco;
import com.skyandforest.reboot_shop.*;
import com.skyandforest.reboot_shop.task.ErrorLoggerTask;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler extends CommandFramework implements Listener {

    public CommandHandler(String label) {
        super(label);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        if (args.length == 0) {
            CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "shop"), "You don't have permission.");
            Player target;

            if (!(sender instanceof Player)) {
                sender.sendMessage(Shop.CHAT_PREFIX + " It is not possible to open a store through the console...");
                return;
            } else {
                target = (Player) sender;
            }
            nextPage(target, 1);
            return;
        }

        if (args[0].equalsIgnoreCase("sell")) {
            CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "pay"), "You don't have permission.");

            com.skyandforest.reboot_core.command.CommandFramework.CommandValidate.isTrue(
                    sender instanceof Player,
                    Utils.addColors(Eco.CHAT_PREFIX + "&cОператор, ты бомж, у тебя нет денег!")
            );

            ItemStack itemStack;
            itemStack = ((Player)sender).getInventory().getItemInMainHand().clone();

            sender.sendMessage(Shop.CHAT_PREFIX + ChatColor.GREEN + "Выставлено на продажу!");

            ItemMeta im = itemStack.getItemMeta().clone();

            List<String> lore = new ArrayList<String>();

            String lore1 = "&aCost: &c";

            switch (args.length){
                case 4:
                    lore1 += String.valueOf(args[3]) + " G ";
                case 3:
                    lore1 += String.valueOf(args[2]) + " S ";
                break;
            }

            lore1 += String.valueOf(args[1]) + " C ";

            lore.add(Utils.addColors(lore1));
            lore.add(Utils.addColors("&aSeller: &c" + ((Player)sender).getDisplayName()));
            lore.add("");
            lore.add(Utils.addColors("&aLeft click to buy"));


            im.setLore(lore);

            itemStack.setItemMeta(im);

            Shop.iList.add(itemStack);
            ((Player)sender).getInventory().getItemInMainHand().setAmount(0);
            return;
        }

        if (args[0].equalsIgnoreCase("help")) {
            CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "help"), "You don't have permission.");

            sender.sendMessage(Shop.CHAT_PREFIX);
            sender.sendMessage(ChatColor.GREEN + "Version: " + ChatColor.GRAY + Shop.getInstance().getDescription().getVersion());
            sender.sendMessage(ChatColor.GREEN + "Developer: " + ChatColor.GRAY + "CMen_");
            sender.sendMessage("Commands:");
            sender.sendMessage(ChatColor.WHITE + "/" + label + " reload" + ChatColor.GRAY + " - Reloads the plugin.");
//            sender.sendMessage(ChatColor.WHITE + "/" + label + " list" + ChatColor.GRAY + " - Lists the loaded menus.");
            sender.sendMessage(ChatColor.WHITE + "/" + label + ChatColor.GRAY + " - Opens a shop for a player.");
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "reload"), "You don't have permission.");

            Shop.closeAllSessions();

            ErrorLogger errorLogger = new ErrorLogger();
            Shop.getInstance().load(errorLogger);

            Shop.setLastReloadErrors(errorLogger.getSize());

            if (!errorLogger.hasErrors()) {
                sender.sendMessage(Shop.CHAT_PREFIX + "Plugin reloaded.");
            } else {
                new ErrorLoggerTask(errorLogger).run();
                sender.sendMessage(Shop.CHAT_PREFIX + ChatColor.RED + "Plugin reloaded with " + errorLogger.getSize() + " error(s).");
                if (!(sender instanceof ConsoleCommandSender)) {
                    sender.sendMessage(Shop.CHAT_PREFIX + ChatColor.RED + "Please check the console.");
                }
            }
            return;
        }

        sender.sendMessage(ChatColor.RED + "Unknown sub-command \"" + args[0] + "\".");
    }

    public void nextPage(Player player, int page){
        if(page<0) return;

        ShopMenu menu = new ShopMenu("§9Shop", 54, new ShopMenu.OptionClickEventHandler() {
            @Override
            public void onOptionClick(ShopMenu.OptionClickEvent event) {
                event.setWillDestroy(true);
                event.setWillClose(true);
                if(event.getPosition() <= 43 && event.getPosition() >= 0) {
                    event.setWillClose(false);
                    event.setWillDestroy(true);
                    confirmPay(event.getPlayer(), event.getItem());
                    return;
                } else if ((event.getPosition() <= 48) && (event.getPosition() >= 46)) {
                    event.setWillClose(false);
                    event.setWillDestroy(false);
                    return;
                } else if (event.getPosition() == 45) {
                    event.setWillClose(false);
                    event.setWillDestroy(true);
                    nextPage(event.getPlayer(), page-1);
                    return;
                } else if (event.getPosition() == 53) {
                    event.setWillClose(false);
                    event.setWillDestroy(true);
                    nextPage(event.getPlayer(), page+1);
                    return;
                } else if (event.getPosition() == 49) {
//                  sort(target);
                    event.setWillClose(false);
                    event.setWillDestroy(false);
                    return;
                }
            }
        }, Shop.getInstance())
                .setOption(45, getILeft())
                .setOption(46, getIBlock())
                .setOption(47, getIBlock())
                .setOption(48, getIBlock())
                .setOption(49, getISort())
                .setOption(50, getIBlock())
                .setOption(51, getIBlock())
                .setOption(52, getIBlock())
                .setOption(53, getIRight());

        for(int i = 0; i<45; i++){
            if((i+(page-1)*45)>Shop.getiList().size()-1) break;
            menu.setOption(i, Shop.getiList().get(i+(page-1)*45));
        }

        menu.open(player);
    }

    public void confirmPay(Player player, ItemStack item) {
        ShopMenu menu = new ShopMenu("ConfPay", 27, event -> {
            event.setWillDestroy(true);
            event.setWillClose(true);
            int pos = event.getPosition();
            if ((pos >= 0 && pos <= 2) || (pos >= 9 && pos <= 11) || (pos >= 18 && pos <= 20)) {
                event.setWillClose(true);
                event.setWillDestroy(true);

                String lore = item.getItemMeta().getLore().get(0);
                int price = Integer.parseInt(lore.substring(10 ,lore.length()-1));
                doPayment(event.getPlayer(), price, item);
                return;
            } else if ((pos >= 6 && pos <= 8) || (pos >= 15 && pos <= 17) || (pos >= 24 && pos <= 26)) {
                event.setWillClose(false);
                event.setWillDestroy(true);
                nextPage(player, 1);
                return;
            } else if ((pos >= 3 && pos <= 5) || (pos >= 12 && pos <= 14) || (pos >= 21 && pos <= 23)) {
                event.setWillClose(false);
                event.setWillDestroy(false);
                return;
            }
        }, Shop.getInstance())
                .setOption(0, getIAccept())
                .setOption(1, getIAccept())
                .setOption(2, getIAccept())
                .setOption(3, getIBlock())
                .setOption(4, getIBlock())
                .setOption(5, getIBlock())
                .setOption(6, getIDeny())
                .setOption(7, getIDeny())
                .setOption(8, getIDeny())
                .setOption(9, getIAccept())
                .setOption(10, getIAccept())
                .setOption(11, getIAccept())
                .setOption(12, getIBlock())
                .setOption(13, item)
                .setOption(14, getIBlock())
                .setOption(15, getIDeny())
                .setOption(16, getIDeny())
                .setOption(17, getIDeny())
                .setOption(18, getIAccept())
                .setOption(19, getIAccept())
                .setOption(20, getIAccept())
                .setOption(21, getIBlock())
                .setOption(22, getIBlock())
                .setOption(23, getIBlock())
                .setOption(24, getIDeny())
                .setOption(25, getIDeny())
                .setOption(26, getIDeny());

        menu.open(player);
    }

    private void doPayment(Player player, int price, ItemStack item){
        long amount = price;
        if(Eco.hasBalance(player, "c", price)){
            if(player.getInventory().firstEmpty() != -1){
                player.getInventory().addItem(item);
                EconomyBridge.takeMoney(player, price);
                Shop.getiList().remove(item);
            } else {
                player.sendMessage(Shop.getLang().no_slots);
            }
        } else {
            player.sendMessage("No money!");
        }
    }

    public ItemStack getILeft()
    {
        ItemStack item = new ItemStack(Material.BANNER, 1,(short)15);
        BannerMeta m = (BannerMeta)item.getItemMeta();
        m.setDisplayName(Utils.addColors("&3<---"));
        List<String> lore = new ArrayList<>();
        lore.add(Utils.addColors("&6Previous page"));
        m.setLore(lore);
        m.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.RED, PatternType.RHOMBUS_MIDDLE));
        patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_RIGHT));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_RIGHT));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_RIGHT));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.BORDER));

        m.setPatterns(patterns);

        item.setItemMeta(m);
        return item;
    }

    public ItemStack getIRight()
    {
        ItemStack item = new ItemStack(Material.BANNER, 1,(short)15);
        BannerMeta m = (BannerMeta)item.getItemMeta();
        m.setDisplayName(Utils.addColors("&3--->"));
        List<String> lore = new ArrayList<>();
        lore.add(Utils.addColors("&6Next page"));
        m.setLore(lore);
        m.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.RED, PatternType.RHOMBUS_MIDDLE));
        patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_LEFT));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.SQUARE_BOTTOM_LEFT));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.SQUARE_TOP_LEFT));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.BORDER));

        m.setPatterns(patterns);

        item.setItemMeta(m);
        return item;
    }

    public ItemStack getISort()
    {
        ItemStack item = new ItemStack(Material.BANNER, 1,(short)15);
        BannerMeta m = (BannerMeta)item.getItemMeta();
        m.setDisplayName(Utils.addColors("&3Sort"));
        List<String> lore = new ArrayList<>();
        lore.add(Utils.addColors("&6Sorting shop by:"));
        lore.add(Utils.addColors("&a - Date"));
        lore.add(Utils.addColors("&a - Name"));
        lore.add(Utils.addColors("&a - Price"));
        m.setLore(lore);
        m.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));

        m.setPatterns(patterns);

        item.setItemMeta(m);
        return item;
    }

    private ItemStack getIDeny() {
        ItemStack item =  new ItemStack(Material.STAINED_GLASS_PANE, 1,(short)14);
        ItemMeta imDeny = item.getItemMeta();
        imDeny.setDisplayName(Utils.addColors("&4Deny"));
        item.setItemMeta(imDeny);
        return item;
    }

    private ItemStack getIAccept() {
        ItemStack item =  new ItemStack(Material.STAINED_GLASS_PANE, 1,(short)5);
        ItemMeta imAccept = item.getItemMeta();
        imAccept.setDisplayName(Utils.addColors("&aAccept"));
        item.setItemMeta(imAccept);
        return item;
    }

    private ItemStack getIBlock(){
        ItemStack itemBlock =  new ItemStack(Material.STAINED_GLASS_PANE, 1,(short)15);
        ItemMeta imBlock = itemBlock.getItemMeta();
        imBlock.setDisplayName(Utils.addColors("<3"));
        itemBlock.setItemMeta(imBlock);
        return itemBlock;
    }

}