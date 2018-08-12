package com.skyandforest.reboot_shop.command;

import com.skyandforest.reboot_core.command.CommandFramework;
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
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.COMMAND_BASE + "shop"),
                    Utils.addColors(Eco.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );
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
            CommandValidate.isTrue(sender.hasPermission(
                    Permissions.COMMAND_BASE + "pay"),
                    Utils.addColors(Eco.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            CommandFramework.CommandValidate.isTrue(
                    sender instanceof Player,
                    Utils.addColors(Eco.CHAT_PREFIX + "&cОператор, ты бомж, у тебя нет денег!")
            );

            ItemStack itemStack;
            itemStack = ((Player) sender).getInventory().getItemInMainHand().clone();

            sender.sendMessage(Shop.CHAT_PREFIX + ChatColor.GREEN + "Выставлено на продажу!");

            ItemMeta im = itemStack.getItemMeta().clone();

            List<String> lore = new ArrayList<>();
            lore.add(Utils.addColors("&aСтоимость:"));

            long[] cost = new long[3];

            switch (args.length) {
                case 4:
                    cost[2] = Long.parseLong(args[3]);
                case 3:
                    cost[1] = Long.parseLong(args[2]);
                    break;
            }

            cost[0] = Long.parseLong(args[1]);

            cost = Eco.normBalance(cost).clone();

            lore.add(Utils.addColors(Eco.formatBalance(cost)));
            lore.add(Utils.addColors("&aПродавец: &c" + ((Player) sender).getDisplayName()));
            lore.add("");
            lore.add(Utils.addColors("&aЛевый клик чтобы купить"));

            im.setLore(lore);
            itemStack.setItemMeta(im);
            Shop.iList.add(itemStack);
            ((Player) sender).getInventory().getItemInMainHand().setAmount(0);
            return;
        }

        if (args[0].equalsIgnoreCase("help")) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.COMMAND_BASE + "help"),
                    Utils.addColors(Eco.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );


            sender.sendMessage(Shop.CHAT_PREFIX);
            sender.sendMessage(ChatColor.GREEN + "Version: " + ChatColor.GRAY + Shop.getInstance().getDescription().getVersion());
            sender.sendMessage(ChatColor.GREEN + "Developer: " + ChatColor.GRAY + "CMen_");
            sender.sendMessage("Commands:");
            sender.sendMessage(ChatColor.WHITE + "/" + label + " reload" + ChatColor.GRAY + " - Reloads the plugin.");
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

    private void nextPage(Player player, int page) {
        if (page < 0) return;

        ShopMenu menu = new ShopMenu("§9Shop", 54, event -> {
            event.setWillDestroy(true);
            event.setWillClose(true);
            if (event.getPosition() <= 43 && event.getPosition() >= 0) {
                event.setWillClose(false);
                event.setWillDestroy(true);
                confirmPay(event.getPlayer(), event.getItem());
            } else if ((event.getPosition() <= 48) && (event.getPosition() >= 46)) {
                event.setWillClose(false);
                event.setWillDestroy(false);
            } else if (event.getPosition() == 45) {
                event.setWillClose(false);
                event.setWillDestroy(true);
                nextPage(event.getPlayer(), page - 1);
            } else if (event.getPosition() == 53) {
                event.setWillClose(false);
                event.setWillDestroy(true);
                nextPage(event.getPlayer(), page + 1);
            } else if (event.getPosition() == 49) {
//                  sort(target);
                event.setWillClose(false);
                event.setWillDestroy(false);
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

        for (int i = 0; i < 45; i++) {
            if ((i + (page - 1) * 45) > Shop.getiList().size() - 1) break;
            menu.setOption(i, Shop.getiList().get(i + (page - 1) * 45));
        }

        menu.open(player);
    }

    private void confirmPay(Player player, ItemStack item) {
        ShopMenu menu = new ShopMenu("ConfPay", 27, event -> {
            event.setWillDestroy(true);
            event.setWillClose(true);
            int pos = event.getPosition();
            if ((pos >= 0 && pos <= 2) || (pos >= 9 && pos <= 11) || (pos >= 18 && pos <= 20)) {
                event.setWillClose(true);
                event.setWillDestroy(true);

                String lore = item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 4);
                String[] cost = lore.split(" ");
                long[] costArray = new long[3];
                costArray[0] = Long.parseLong(cost[0].substring(1));
                costArray[1] = Long.parseLong(cost[2].substring(1));
                costArray[2] = Long.parseLong(cost[4].substring(1));

                costArray[0] = Eco.asCopper("g", costArray[0]);
                costArray[1] = Eco.asCopper("s", costArray[1]);

                long price = costArray[0] + costArray[1] + costArray[2];

                player.sendMessage(String.valueOf(price));
                doPayment(event.getPlayer(), price, item);

            } else if ((pos >= 6 && pos <= 8) || (pos >= 15 && pos <= 17) || (pos >= 24 && pos <= 26)) {
                event.setWillClose(false);
                event.setWillDestroy(true);
                nextPage(player, 1);
            } else if ((pos >= 3 && pos <= 5) || (pos >= 12 && pos <= 14) || (pos >= 21 && pos <= 23)) {
                event.setWillClose(false);
                event.setWillDestroy(false);
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

    private void doPayment(Player player, long price, ItemStack item) {
        if (Eco.hasBalance(player, "c", price)) {
            if (player.getInventory().firstEmpty() != -1) {
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

    private ItemStack getILeft() {
        ItemStack item = new ItemStack(Material.BANNER, 1, (short) 15);
        BannerMeta m = (BannerMeta) item.getItemMeta();
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

    private ItemStack getIRight() {
        ItemStack item = new ItemStack(Material.BANNER, 1, (short) 15);
        BannerMeta m = (BannerMeta) item.getItemMeta();
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

    private ItemStack getISort() {
        ItemStack item = new ItemStack(Material.BANNER, 1, (short) 15);
        BannerMeta m = (BannerMeta) item.getItemMeta();
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
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
        ItemMeta imDeny = item.getItemMeta();
        imDeny.setDisplayName(Utils.addColors("&4Deny"));
        item.setItemMeta(imDeny);
        return item;
    }

    private ItemStack getIAccept() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 5);
        ItemMeta imAccept = item.getItemMeta();
        imAccept.setDisplayName(Utils.addColors("&aAccept"));
        item.setItemMeta(imAccept);
        return item;
    }

    private ItemStack getIBlock() {
        ItemStack itemBlock = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta imBlock = itemBlock.getItemMeta();
        imBlock.setDisplayName(Utils.addColors("<3"));
        itemBlock.setItemMeta(imBlock);
        return itemBlock;
    }

}