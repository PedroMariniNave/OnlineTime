package com.zpedroo.onlinetime.api;

import com.zpedroo.onlinetime.managers.DataManager;
import org.bukkit.entity.Player;

public class OnlineTimeAPI {

    public static long getLevel(Player player) {
        return DataManager.getInstance().getPlayerData(player).getLevel();
    }
}