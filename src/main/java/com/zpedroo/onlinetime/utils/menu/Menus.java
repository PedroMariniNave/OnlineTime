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
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.List;

public class Menus extends InventoryUtils {

    private static Menus instance;
    public static Menus getInstance() { return instance; }

    private final ItemStack nextPageItem;
    private final ItemStack previousPageItem;

    public Menus() {
        instance = this;
        this.nextPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).get(), "Next-Page").build();
        this.previousPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).get(), "Previous-Page").build();
    }

    public void openMainMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.MAIN;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        PlayerData data = DataManager.getInstance().getPlayerData(player);

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

            inventory.addItem(item, slot, () -> {
                switch (action.toUpperCase()) {
                    case "SHOP":
                        openShopMenu(player);
                        break;
                    case "TOP":
                        openTopMenu(player);
                        break;
                }
            }, ActionType.ALL_CLICKS);
        }

        inventory.open(player);
    }

    public void openShopMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.SHOP;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        int nextPageSlot = FileUtils.get().getInt(file, "Inventory.next-page-slot");
        int previousPageSlot = FileUtils.get().getInt(file, "Inventory.previous-page-slot");

        InventoryBuilder inventory = new InventoryBuilder(title, size, previousPageItem, previousPageSlot, nextPageItem, nextPageSlot);

        List<ShopItem> shopItems = DataManager.getInstance().getCache().getShopItems();

        int i = -1;
        String[] slots = FileUtils.get().getString(file, "Inventory.item-slots").replace(" ", "").split(",");
        for (ShopItem item : shopItems) {
            if (item == null) continue;
            if (++i >= slots.length) i = 0;

            ItemStack display = item.getDisplay().clone();
            int slot = Integer.parseInt(slots[i]);

            inventory.addItem(display, slot, () -> {
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
            }, ActionType.ALL_CLICKS);
        }

        inventory.open(player);
    }

    public void openTopMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.TOP;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        int pos = 0;
        String[] slots = FileUtils.get().getString(file, "Inventory.slots").replace(" ", "").split(",");

        for (PlayerData data : DataManager.getInstance().getCache().getTopOnline()) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(data.getUUID());

            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.TOP).get(), "Item", new String[]{
                    "{player}",
                    "{played_time}",
                    "{join_date}",
                    "{points}",
                    "{pos}"
            }, new String[]{
                    target.hasPlayedBefore() ? target.getName() : "-/-",
                    TimeFormatter.get().millisToTime(data.getTotalOnlineTime()),
                    TimeFormatter.get().millisToDate(data.getFirstJoinTime(), Settings.DATE_FORMAT),
                    NumberFormatter.getInstance().format(BigInteger.valueOf(data.getPointsAmount())),
                    String.valueOf(++pos)
            }).build();

            int slot = Integer.parseInt(slots[pos - 1]);

            inventory.addItem(item, slot);
        }

        inventory.open(player);
    }
}