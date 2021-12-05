package com.hanriel.reboot_shop.command;

import com.hanriel.reboot_core.command.CommandFramework;
import com.hanriel.reboot_core.util.Utils;
import com.hanriel.reboot_economy.Eco;
import com.hanriel.reboot_shop.*;
import com.hanriel.reboot_shop.task.ErrorLoggerTask;
import org.bukkit.Bukkit;
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
                    Utils.addColors(Shop.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
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
                    Utils.addColors(Shop.CHAT_PREFIX + "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            CommandValidate.minLength(args, 2, "Usage: /" + label + " sell <copper> <silver> <gold>");

            CommandFramework.CommandValidate.isTrue(
                    sender instanceof Player,
                    Utils.addColors(Shop.CHAT_PREFIX + "&cОператор, ты бомж, у тебя нет денег!")
            );

            ItemStack itemStack;
            itemStack = ((Player) sender).getInventory().getItemInMainHand().clone();

            CommandValidate.isTrue(
                    itemStack.getType() != Material.AIR,
                    Utils.addColors(Shop.CHAT_PREFIX + "&cСер, у вас же пусто в руке!")
            );

            sender.sendMessage(Shop.CHAT_PREFIX + ChatColor.GREEN + "Выставлено на продажу!");

            ItemMeta im = itemStack.getItemMeta().clone();

            List<String> lore = new ArrayList<>();
            lore.add(Utils.addColors("&aСтоимость:"));

            long cost = 0;

            switch (args.length) {
                case 3:
                    cost += Long.parseLong(args[2]);
                    break;
            }

            cost += Long.parseLong(args[1]);

            //cost = Eco.normBalance(cost).clone();

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
            sender.sendMessage(Shop.CHAT_PREFIX + "Команды:");
            sender.sendMessage(Utils.addColors("&f/" + label + "&7 - открыть меню магазина."));
            sender.sendMessage(Utils.addColors("&f/" + label + " sell <copper> <silver> <gold>&7 - выставить предмет нахдящийся в руке на продажу."));
            sender.sendMessage(Utils.addColors("&f/" + label + " help&7 - справка о командах плагина"));

            if (sender.hasPermission(Permissions.COMMAND_BASE + "help")) {
                sender.sendMessage(Utils.addColors("&f/" + label + " reload&7 - Перезагрузить конфигурации плагина."));
                sender.sendMessage(Utils.addColors("&aВерсия: &7" + Shop.getInstance().getDescription().getVersion()));
                sender.sendMessage(Utils.addColors("&aРазрабочики: &7CMen_"));
            }
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
                String[] lor = lore.split(" ");
                String targ = item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 3);
                String[] tar = targ.split(" ");
                Player target = Bukkit.getPlayerExact(tar[tar.length-1].substring(2));

                long price = Long.parseLong(lor[0].substring(2));
                doPayment(event.getPlayer(), price, item, target);

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

    private void doPayment(Player player, long price, ItemStack item, Player target) {
        if (Eco.hasBalance(player, price)) {
            if (player.getInventory().firstEmpty() != -1) {
                ItemStack paItem = item;
                ItemMeta iMeta = paItem.getItemMeta();
                List<String> iLore = iMeta.getLore();

//                iLore.subList(iLore.size() - 5, iLore.size());
                iLore.remove(iLore.size() - 1);
                iLore.remove(iLore.size() - 1);
                iLore.remove(iLore.size() - 1);
                iLore.remove(iLore.size() - 1);
                iLore.remove(iLore.size() - 1);

                iMeta.setLore(iLore);
                paItem.setItemMeta(iMeta);
                player.getInventory().addItem(paItem);
                EconomyBridge.takeMoney(player, price);
                EconomyBridge.giveMoney(target, price);
                Shop.getiList().remove(item);
            } else {
                player.sendMessage(Shop.getLang().no_slots);
            }
        } else {
            player.sendMessage("Недостаточно Люфов");
        }
    }

    private ItemStack getILeft() {
        ItemStack item = new ItemStack(Material.WHITE_BANNER, 1, (short) 15);
        BannerMeta m = (BannerMeta) item.getItemMeta();
        m.setDisplayName(Utils.addColors("&3<---"));
        List<String> lore = new ArrayList<>();
        lore.add(Utils.addColors("&6Предыдущая страница"));
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
        ItemStack item = new ItemStack(Material.WHITE_BANNER, 1, (short) 15);
        BannerMeta m = (BannerMeta) item.getItemMeta();
        m.setDisplayName(Utils.addColors("&3--->"));
        List<String> lore = new ArrayList<>();
        lore.add(Utils.addColors("&6Следующая страница"));
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
        ItemStack item = new ItemStack(Material.WHITE_BANNER, 1, (short) 15);
        BannerMeta m = (BannerMeta) item.getItemMeta();
        m.setDisplayName(Utils.addColors("&3Сортировать"));
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
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemMeta imDeny = item.getItemMeta();
        imDeny.setDisplayName(Utils.addColors("&4Отменить"));
        item.setItemMeta(imDeny);
        return item;
    }

    private ItemStack getIAccept() {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta imAccept = item.getItemMeta();
        imAccept.setDisplayName(Utils.addColors("&aПринять"));
        item.setItemMeta(imAccept);
        return item;
    }

    private ItemStack getIBlock() {
        ItemStack itemBlock = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta imBlock = itemBlock.getItemMeta();
        imBlock.setDisplayName(Utils.addColors("<3"));
        itemBlock.setItemMeta(imBlock);
        return itemBlock;
    }

}