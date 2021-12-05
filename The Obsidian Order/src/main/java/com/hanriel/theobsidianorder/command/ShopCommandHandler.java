package com.hanriel.theobsidianorder.command;

import com.hanriel.theobsidianorder.Eco;
import com.hanriel.theobsidianorder.GameGUI;
import com.hanriel.theobsidianorder.TheObsidianOrder;
import com.hanriel.theobsidianorder.util.Permissions;
import com.hanriel.theobsidianorder.util.Tags;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.hanriel.theobsidianorder.util.Utils.addColors;
import static com.hanriel.theobsidianorder.util.Utils.addColorsTag;
import static com.hanriel.theobsidianorder.util.Utils.sendMsg;

public class ShopCommandHandler extends CommandFramework {
    public ShopCommandHandler(String label) {
        super(label);
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length == 0) {
            CommandValidate.isTrue(
                    sender.hasPermission(Permissions.SHOP_BASE + "shop"),
                    addColorsTag(Tags.SHOP, "&cУ вас недостаточно прав для выполнения данной команды.")
            );
            Player target;

            if (!(sender instanceof Player)) {
                sendMsg(sender, Tags.ECO + "Не возможно открыть магазин из консоли...");
                return;
            } else {
                target = (Player) sender;
            }
            nextPage(target, 1);
            return;
        }

        if (args[0].equalsIgnoreCase("sell")) {
            CommandValidate.isTrue(sender.hasPermission(
                    Permissions.SHOP_BASE + "pay"),
                    addColorsTag(Tags.SHOP, "&cУ вас недостаточно прав для выполнения данной команды.")
            );

            CommandValidate.minLength(args, 2, "Usage: /" + label + " sell <copper> <silver> <gold>");

            CommandValidate.isTrue(
                    sender instanceof Player,
                    addColorsTag(Tags.SHOP, "&cОператор, ты бомж, у тебя нет денег!")
            );

            ItemStack itemStack;
            itemStack = ((Player) sender).getInventory().getItemInMainHand().clone();

            CommandValidate.isTrue(
                    itemStack.getType() != Material.AIR,
                    addColorsTag(Tags.SHOP, "&cСер, у вас же пусто в руке!")
            );

            sendMsg(sender, Tags.ECO + "&aВыставлено на продажу!");

            ItemMeta im = Objects.requireNonNull(itemStack.getItemMeta()).clone();

            List<String> lore = new ArrayList<>();
            lore.add(addColors("&aСтоимость:"));

            long cost = 0;

            if (args.length == 3) {
                cost += Long.parseLong(args[2]);
            }

            cost += Long.parseLong(args[1]);

            //cost = Eco.normBalance(cost).clone();

            lore.add(addColors(Eco.formatBalance(cost)));
            lore.add(addColors("&aПродавец: &c" + ((Player) sender).getDisplayName()));
            lore.add("");
            lore.add(addColors("&aЛевый клик чтобы купить"));

            im.setLore(lore);
            itemStack.setItemMeta(im);
            TheObsidianOrder.iList.add(itemStack);
            ((Player) sender).getInventory().getItemInMainHand().setAmount(0);
            return;
        }

        if (args[0].equalsIgnoreCase("help")) {
            sendMsg(sender, "&aВерсия: &7" + TheObsidianOrder.getInstance().getDescription().getVersion());
            sendMsg(sender, "&aРазрабочики: &7CMen_");
            sendMsg(sender, "Команды:");
            sendMsg(sender, "&f/" + label + "&7 - открыть меню магазина.");
            sendMsg(sender, "&f/" + label + " sell <copper> <silver> <gold>&7 - выставить предмет нахдящийся в руке на продажу.");
            sendMsg(sender, "&f/" + label + " help&7 - справка о командах плагина");

            if (sender.hasPermission(Permissions.SHOP_BASE + "help")) {
                sendMsg(sender, "&f/" + label + " reload&7 - Перезагрузить конфигурации плагина.");
            }
            return;
        }

        sendMsg(sender, "&cНеизвестаная под-команда  \"" + args[0] + "\".");
    }

    private void nextPage(Player player, int page) {
        if (page < 0) return;

        GameGUI menu = new GameGUI("§9Shop", 54, event -> {
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
        }, TheObsidianOrder.getInstance())
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
            if ((i + (page - 1) * 45) > TheObsidianOrder.getiList().size() - 1) break;
            menu.setOption(i, TheObsidianOrder.getiList().get(i + (page - 1) * 45));
        }

        menu.open(player);
    }

    private void confirmPay(Player player, ItemStack item) {
        GameGUI menu = new GameGUI("ConfPay", 27, event -> {
            event.setWillDestroy(true);
            event.setWillClose(true);
            int pos = event.getPosition();
            if ((pos >= 0 && pos <= 2) || (pos >= 9 && pos <= 11) || (pos >= 18 && pos <= 20)) {
                event.setWillClose(true);
                event.setWillDestroy(true);

                String lore = Objects.requireNonNull(Objects.requireNonNull(item.getItemMeta()).getLore()).get(Objects.requireNonNull(item.getItemMeta().getLore()).size() - 4);
                String[] lor = lore.split(" ");
                String targ = item.getItemMeta().getLore().get(item.getItemMeta().getLore().size() - 3);
                String[] tar = targ.split(" ");
                Player target = Bukkit.getPlayerExact(tar[tar.length - 1].substring(2));

                long price = Long.parseLong(lor[0].substring(2));

                if (target != null) {
                    if (target.isOnline()){
                        doPayment(event.getPlayer(), price, item, target);
                        sendMsg(target, Tags.ECO + event.getPlayer().getDisplayName() + " &aкупил " + item.getItemMeta().getDisplayName() + " за &6" + price + " люф");
                    }
                } else {
                    sendMsg(player, Tags.ECO + "&cИгрок не в сети");
                }

            } else if ((pos >= 6 && pos <= 8) || (pos >= 15 && pos <= 17) || (pos >= 24 && pos <= 26)) {
                event.setWillClose(false);
                event.setWillDestroy(true);
                nextPage(player, 1);
            } else if ((pos >= 3 && pos <= 5) || (pos >= 12 && pos <= 14) || (pos >= 21 && pos <= 23)) {
                event.setWillClose(false);
                event.setWillDestroy(false);
            }
        }, TheObsidianOrder.getInstance())
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
                ItemMeta iMeta = item.getItemMeta();
                List<String> iLore = Objects.requireNonNull(iMeta).getLore();

//                iLore.subList(iLore.size() - 5, iLore.size());
                assert iLore != null;
                iLore.remove(iLore.size() - 1);
                iLore.remove(iLore.size() - 1);
                iLore.remove(iLore.size() - 1);
                iLore.remove(iLore.size() - 1);
                iLore.remove(iLore.size() - 1);

                iMeta.setLore(iLore);
                item.setItemMeta(iMeta);
                player.getInventory().addItem(item);
                Eco.takeBalance(player, price);
                Eco.addBalance(target, price);
                TheObsidianOrder.getiList().remove(item);
            } else {
                player.sendMessage(TheObsidianOrder.getLang().no_slots);
            }
        } else {
            player.sendMessage("Недостаточно Люфов");
        }
    }

    private ItemStack getILeft() {
        ItemStack item = new ItemStack(Material.WHITE_BANNER, 1, (short) 15);
        BannerMeta m = (BannerMeta) item.getItemMeta();
        assert m != null;
        m.setDisplayName(addColors("&3<---"));
        List<String> lore = new ArrayList<>();
        lore.add(addColors("&6Предыдущая страница"));
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
        assert m != null;
        m.setDisplayName(addColors("&3--->"));
        List<String> lore = new ArrayList<>();
        lore.add(addColors("&6Следующая страница"));
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
        assert m != null;
        m.setDisplayName(addColors("&3Сортировать"));
        List<String> lore = new ArrayList<>();
        lore.add(addColors("&6Sorting shop by:"));
        lore.add(addColors("&a - Date"));
        lore.add(addColors("&a - Name"));
        lore.add(addColors("&a - Price"));
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
        assert imDeny != null;
        imDeny.setDisplayName(addColors("&4Отменить"));
        item.setItemMeta(imDeny);
        return item;
    }

    private ItemStack getIAccept() {
        ItemStack item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1);
        ItemMeta imAccept = item.getItemMeta();
        assert imAccept != null;
        imAccept.setDisplayName(addColors("&aПринять"));
        item.setItemMeta(imAccept);
        return item;
    }

    private ItemStack getIBlock() {
        ItemStack itemBlock = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta imBlock = itemBlock.getItemMeta();
        assert imBlock != null;
        imBlock.setDisplayName(addColors("<3"));
        itemBlock.setItemMeta(imBlock);
        return itemBlock;
    }
}
