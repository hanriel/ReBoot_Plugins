package com.hanriel.reboot_auth.command;

import com.hanriel.reboot_auth.Shop;
import com.hanriel.reboot_auth.Permissions;
import com.hanriel.reboot_auth.task.ErrorLoggerTask;
import com.hanriel.reboot_auth.util.ErrorLogger;
import com.hanriel.reboot_auth.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CommandHandler extends CommandFramework {

    public CommandHandler(String label) {
        super(label);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "open"), "You don't have permission.");
            Player target = null;

            if (!(sender instanceof Player)) {
                sender.sendMessage(Shop.CHAT_PREFIX + " It is not possible to open a store through the console...");
            } else {
//                if (args.length > 2) {
//                    CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "open.others"), "You don't have permission to open menus for others.");
//                    target = Bukkit.getPlayerExact(args[2]);
//                } else {
                    target = (Player) sender;
//                    CommandValidate.notNull(target, "That player is not online.");
//                }
            }
//            String menuName = "shop.yml";
//            ExtendedIconMenu menu = Shop.getFileNameToMenuMap().get(menuName);
//            menu.open(target);

            ItemStack itemBlock =  new ItemStack(Material.STAINED_GLASS_PANE, 1,(short)15);
            ItemMeta imBlock = itemBlock.getItemMeta();
            imBlock.setDisplayName("<3");
            itemBlock.setItemMeta(imBlock);

            Inventory inv = Bukkit.createInventory(null, 54, "Shop");

            int i = 0;
            for (ItemStack object : Shop.iList) {
                inv.setItem(i++, object);
            }

            inv.setItem(45, itemLeft());
            inv.setItem(46, itemBlock);
            inv.setItem(47, itemBlock);
            inv.setItem(48, itemBlock);
            inv.setItem(49, itemSort());
            inv.setItem(50, itemBlock);
            inv.setItem(51, itemBlock);
            inv.setItem(52, itemBlock);
            inv.setItem(53, itemRight());

            target.openInventory(inv);
            return;
        }

        if (args[0].equalsIgnoreCase("sell")) {
            CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "pay"), "You don't have permission.");

            Player target = null;

            if (!(sender instanceof Player)) {
                sender.sendMessage(Shop.CHAT_PREFIX + " It is not possible to open a store through the console...");
            } else {
                target = (Player) sender;
            }

            ItemStack itemStack;
            itemStack = target.getInventory().getItemInMainHand().clone();

            sender.sendMessage(Shop.CHAT_PREFIX + ChatColor.GREEN + "Выставлено на продажу!");

            ItemMeta im = itemStack.getItemMeta().clone();

            List<String> lore = new ArrayList<String>();
            lore.add(Utils.addColors("&aCost: &c" + args[1] + "$"));
            lore.add(Utils.addColors("&aLeft click to buy"));
            lore.add(Utils.addColors("&aSeller: &c" + target.getDisplayName()));

            im.setLore(lore);

            itemStack.setItemMeta(im);

            List<ItemStack> iList = Shop.getItemsList();

            iList.add(itemStack);

            Shop.setItemsList(iList);
            target.getInventory().getItemInMainHand().setAmount(0);
            return;
        }

        if (args[0].equalsIgnoreCase("pc")) {
            CommandValidate.isTrue(sender.hasPermission(Permissions.COMMAND_BASE + "pc"), "You don't have permission.");

            Player target = null;

            if (!(sender instanceof Player)) {
                sender.sendMessage(Shop.CHAT_PREFIX + " It is not possible to open a store through the console...");
            } else {
                target = (Player) sender;
            }

            ItemStack itemAccept =  new ItemStack(Material.STAINED_GLASS_PANE, 1,(short)5);
            ItemMeta imAccept = itemAccept.getItemMeta();
            imAccept.setDisplayName("Accept");
            itemAccept.setItemMeta(imAccept);

            ItemStack itemDeny =  new ItemStack(Material.STAINED_GLASS_PANE, 1,(short)14);
            ItemMeta imDeny = itemDeny.getItemMeta();
            imDeny.setDisplayName("Deny");
            itemDeny.setItemMeta(imDeny);

            ItemStack itemBlock =  new ItemStack(Material.STAINED_GLASS_PANE, 1,(short)15);
            ItemMeta imBlock = itemBlock.getItemMeta();
            imBlock.setDisplayName("<3");
            itemBlock.setItemMeta(imBlock);

            Inventory inv = Bukkit.createInventory(null, 27, "Payment confirm");

            inv.setItem(0, itemAccept);
            inv.setItem(1, itemAccept);
            inv.setItem(2, itemAccept);
            inv.setItem(3, itemBlock);
            inv.setItem(4, itemBlock);
            inv.setItem(5, itemBlock);
            inv.setItem(6, itemDeny);
            inv.setItem(7, itemDeny);
            inv.setItem(8, itemDeny);
            inv.setItem(9, itemAccept);
            inv.setItem(10, itemAccept);
            inv.setItem(11, itemAccept);
            inv.setItem(12, itemBlock);
            inv.setItem(13, new ItemStack(Material.GOLD_BLOCK, 16));
            inv.setItem(14, itemBlock);
            inv.setItem(15, itemDeny);
            inv.setItem(16, itemDeny);
            inv.setItem(17, itemDeny);
            inv.setItem(18, itemAccept);
            inv.setItem(19, itemAccept);
            inv.setItem(20, itemAccept);
            inv.setItem(21, itemBlock);
            inv.setItem(22, itemBlock);
            inv.setItem(23, itemBlock);
            inv.setItem(24, itemDeny);
            inv.setItem(25, itemDeny);
            inv.setItem(26, itemDeny);

            target.openInventory(inv);
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
//            sender.sendMessage(ChatColor.WHITE + "/" + label + " open <menu> [player]" + ChatColor.GRAY + " - Opens a menu for a player.");
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

    public ItemStack itemLeft()
    {
        ItemStack item = new ItemStack(Material.BANNER, 1);
        BannerMeta m = (BannerMeta)item.getItemMeta();
        m.setDisplayName("<---");
        m.setBaseColor(DyeColor.WHITE);
        List<String> lore = new ArrayList<>();
        lore.add(Utils.addColors("Previous page"));
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

    public ItemStack itemRight()
    {
        ItemStack item = new ItemStack(Material.BANNER, 1);
        BannerMeta m = (BannerMeta)item.getItemMeta();
        m.setDisplayName("--->");
        m.setBaseColor(DyeColor.WHITE);
        List<String> lore = new ArrayList<>();
        lore.add(Utils.addColors("Next page"));
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

    public ItemStack itemSort()
    {
        ItemStack item = new ItemStack(Material.BANNER, 1);
        BannerMeta m = (BannerMeta)item.getItemMeta();
        m.setDisplayName("Sort");
        m.setBaseColor(DyeColor.WHITE);
        List<String> lore = new ArrayList<>();
        lore.add(Utils.addColors("Some lore..."));
        m.setLore(lore);
        m.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);

        List<Pattern> patterns = new ArrayList<>();
        patterns.add(new Pattern(DyeColor.BLACK, PatternType.RHOMBUS_MIDDLE));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));

        m.setPatterns(patterns);

        item.setItemMeta(m);
        return item;
    }

}