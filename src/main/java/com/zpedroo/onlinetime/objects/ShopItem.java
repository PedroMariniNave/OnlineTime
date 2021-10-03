package com.zpedroo.onlinetime.objects;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ShopItem {

    private Long price;
    private Integer defaultAmount;
    private ItemStack display;
    private ItemStack shopItem;
    private List<String> commands;

    public ShopItem(Long price, Integer defaultAmount, ItemStack display, ItemStack shopItem, List<String> commands) {
        this.price = price;
        this.defaultAmount = defaultAmount;
        this.display = display;
        this.shopItem = shopItem;
        this.commands = commands;
    }

    public Long getPrice() {
        return price;
    }

    public Integer getDefaultAmount() {
        return defaultAmount;
    }

    public ItemStack getDisplay() {
        return display;
    }

    public ItemStack getShopItem() {
        return shopItem;
    }

    public List<String> getCommands() {
        return commands;
    }
}