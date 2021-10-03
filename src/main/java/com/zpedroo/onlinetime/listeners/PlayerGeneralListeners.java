package com.zpedroo.onlinetime.listeners;

import com.zpedroo.onlinetime.OnlineTime;
import com.zpedroo.onlinetime.managers.DataManager;
import com.zpedroo.onlinetime.managers.PlayerLevelManager;
import com.zpedroo.onlinetime.objects.PlayerData;
import me.extremall.advancedkits.api.event.PlayerUpgradeKitEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerGeneralListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerData data = DataManager.getInstance().load(player);
        data.setLoginTime(System.currentTimeMillis());

        PlayerLevelManager.getInstance().update(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        DataManager.getInstance().save(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onRespawn(PlayerRespawnEvent event) {
        PlayerLevelManager.getInstance().update(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onUpgrade(PlayerUpgradeKitEvent event) {
        OnlineTime.get().getServer().getScheduler().runTaskLater(OnlineTime.get(), () -> PlayerLevelManager.getInstance().update(event.getPlayer()), 0L);
    }
}