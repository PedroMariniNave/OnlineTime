package com.zpedroo.onlinetime.managers.cache;

import com.zpedroo.onlinetime.mysql.DBConnection;
import com.zpedroo.onlinetime.objects.PlayerData;
import com.zpedroo.onlinetime.objects.ShopItem;
import org.bukkit.entity.Player;

import java.util.*;

public class DataCache {

    private final Map<Player, PlayerData> playerData;
    private final List<ShopItem> shopItems;
    private List<PlayerData> topOnline;

    public DataCache() {
        this.playerData = new HashMap<>(128);
        this.shopItems = new ArrayList<>(16);
    }

    public Map<Player, PlayerData> getPlayerData() {
        return playerData;
    }

    public List<ShopItem> getShopItems() {
        return shopItems;
    }

    public List<PlayerData> getTopOnline() {
        if (topOnline == null) {
            this.topOnline = DBConnection.getInstance().getDBManager().getTop();
        }

        return topOnline;
    }

    public void setTop(List<PlayerData> topOnline) {
        this.topOnline = topOnline;
    }
}