package com.zpedroo.onlinetime.utils.menu;

import com.zpedroo.onlinetime.listeners.PlayerChatListener;
import com.zpedroo.onlinetime.managers.DataManager;
import com.zpedroo.onlinetime.objects.PlayerData;
import com.zpedroo.onlinetime.objects.ShopItem;
import com.zpedroo.onlinetime.utils.FileUtils;
import com.zpedroo.onlinetime.utils.builder.InventoryBuilder;
import com.zpedroo.onlinetime.utils.builder.InventoryUtils;
import com.zpedroo.onlinetime.utils.builder.ItemBuilder;
import com.zpedroo.onlinetime.utils.config.Messages;
import com.zpedroo.onlinetime.utils.config.Settings;
import com.zpedroo.onlinetime.utils.formatter.NumberFormatter;
import com.zpedroo.onlinetime.utils.formatter.TimeFormatter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Menus {

    private static Menus instance;
    public static Menus getInstance() { return instance; }

    private InventoryUtils inventoryUtils;

    private ItemStack nextPageItem;
    private ItemStack previousPageItem;

    public Menus() {
        instance = this;
        this.inventoryUtils = new InventoryUtils();
        this.nextPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).get(), "Next-Page").build();
        this.previousPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).get(), "Previous-Page").build();
    }

    public void openMainMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.MAIN;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        Inventory inventory = Bukkit.createInventory(null, size, title);

        PlayerData data = DataManager.getInstance().load(player);

        for (String str : FileUtils.get().getSection(file, "Inventory.items")) {
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + str, new String[]{
                    "{player}",
                    "{played_time}",
                    "{join_date}",
                    "{points}"
            }, new String[]{
                    player.getName(),
                    TimeFormatter.get().millisToTime(data.getTotalOnlineTime()),
                    TimeFormatter.get().millisToDate(data.getFirstJoinTime(), Settings.DATE_FORMAT),
                    NumberFormatter.getInstance().format(BigInteger.valueOf(data.getPointsAmount()))
            }).build();
            int slot = FileUtils.get().getInt(file, "Inventory.items." + str + ".slot");
            String action = FileUtils.get().getString(file, "Inventory.items." + str + ".action");

            switch (action.toUpperCase()) {
                case "SHOP" -> inventoryUtils.addAction(inventory, slot, () -> {
                    openShopMenu(player);
                }, InventoryUtils.ActionType.ALL_CLICKS);

                case "TOP" -> inventoryUtils.addAction(inventory, slot, () -> {
                    openTopMenu(player);
                }, InventoryUtils.ActionType.ALL_CLICKS);
            }

            inventory.setItem(slot, item);
        }

        player.openInventory(inventory);
    }

    public void openShopMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.SHOP;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        Inventory inventory = Bukkit.createInventory(null, size, title);

        List<ShopItem> shopItems = DataManager.getInstance().getCache().getShopItems();

        int i = -1;
        String[] slots = FileUtils.get().getString(file, "Inventory.item-slots").replace(" ", "").split(",");
        List<ItemBuilder> builders = new ArrayList<>(shopItems.size());
        for (ShopItem item : shopItems) {
            if (item == null) continue;
            if (++i >= slots.length) i = 0;

            ItemStack display = item.getDisplay().clone();
            int slot = Integer.parseInt(slots[i]);
            List<InventoryUtils.Action> actions = new ArrayList<>(1);

            actions.add(new InventoryUtils.Action(InventoryUtils.ActionType.ALL_CLICKS, slot, () -> {
                player.closeInventory();
                PlayerChatListener.getPlayerChat().put(player, new PlayerChatListener.PlayerChat(player, item));
                for (String msg : Messages.CHOOSE_AMOUNT) {
                    if (msg == null) continue;

                    player.sendMessage(StringUtils.replaceEach(msg, new String[]{
                            "{item}",
                            "{price}"
                    }, new String[]{
                            item.getDisplay().hasItemMeta() ? item.getDisplay().getItemMeta().hasDisplayName() ? item.getDisplay().getItemMeta().getDisplayName() : item.getDisplay().getType().toString() : item.getDisplay().getType().toString(),
                            NumberFormatter.getInstance().format(BigInteger.valueOf(item.getPrice()))
                    }));
                }
            }));

            builders.add(ItemBuilder.build(display, slot, actions));
        }

        int nextPageSlot = FileUtils.get().getInt(file, "Inventory.next-page-slot");
        int previousPageSlot = FileUtils.get().getInt(file, "Inventory.previous-page-slot");

        InventoryBuilder.build(player, inventory, title, builders, nextPageSlot, previousPageSlot, nextPageItem, previousPageItem);
    }

    public void openTopMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.TOP;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        Inventory inventory = Bukkit.createInventory(null, size, title);

        int pos = 0;
        String[] slots = FileUtils.get().getString(file, "Inventory.slots").replace(" ", "").split(",");

        for (PlayerData data : DataManager.getInstance().getCache().getTopOnline()) {
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.TOP).get(), "Item", new String[]{
                    "{player}",
                    "{played_time}",
                    "{join_date}",
                    "{points}",
                    "{pos}"
            }, new String[]{
                    Bukkit.getOfflinePlayer(data.getUUID()).getName(),
                    TimeFormatter.get().millisToTime(data.getTotalOnlineTime()),
                    TimeFormatter.get().millisToDate(data.getFirstJoinTime(), Settings.DATE_FORMAT),
                    NumberFormatter.getInstance().format(BigInteger.valueOf(data.getPointsAmount())),
                    String.valueOf(++pos)
            }).build();

            int slot = Integer.parseInt(slots[pos - 1]);

            inventoryUtils.addAction(inventory, slot, null, InventoryUtils.ActionType.ALL_CLICKS); // cancel click

            inventory.setItem(slot, item);
        }

        player.openInventory(inventory);
    }
}