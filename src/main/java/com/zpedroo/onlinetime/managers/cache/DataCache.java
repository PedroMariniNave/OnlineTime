package com.zpedroo.onlinetime.managers.cache;

import com.zpedroo.onlinetime.mysql.DBConnection;
import com.zpedroo.onlinetime.objects.PlayerData;
import com.zpedroo.onlinetime.objects.ShopItem;
import org.bukkit.entity.Player;

import java.util.*;

public class DataCache {

    private Map<Player, PlayerData> playerData;
    private List<ShopItem> shopItems;
    private List<PlayerData> topOnline;

    public DataCache() {
        this.playerData = new HashMap<>(128);
        this.shopItems = new ArrayList<>(16);
        this.topOnline = DBConnection.getInstance().getDBManager().getTop();
    }

    public Map<Player, PlayerData> getPlayerData() {
        return playerData;
    }

    public List<ShopItem> getShopItems() {
        return shopItems;
    }

    public List<PlayerData> getTopOnline() {
        return topOnline;
    }

    public void setTop(List<PlayerData> topOnline) {
        this.topOnline = topOnline;
    }
}