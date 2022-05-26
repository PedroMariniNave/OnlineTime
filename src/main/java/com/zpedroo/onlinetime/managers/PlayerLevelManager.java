package com.zpedroo.onlinetime.managers;

import com.zpedroo.onlinetime.objects.PlayerData;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class PlayerLevelManager {

    private static PlayerLevelManager instance;
    public static PlayerLevelManager getInstance() { return instance; }

    public PlayerLevelManager() {
        instance = this;
    }

    public void update(Player player) {
        PlayerData data = DataManager.getInstance().getPlayerData(player);

        long hourInMillis = TimeUnit.HOURS.toMillis(1L);
        float hoursOnline = (float) data.getTotalOnlineTime() / hourInMillis;
        player.setLevel((int) hoursOnline);

        float progress = (float) (hoursOnline - Math.floor(hoursOnline));
        player.setExp(progress);
    }
}