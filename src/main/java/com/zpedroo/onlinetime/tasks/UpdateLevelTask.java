package com.zpedroo.onlinetime.tasks;

import com.zpedroo.onlinetime.managers.PlayerLevelManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashSet;

import static com.zpedroo.onlinetime.utils.config.Settings.*;

public class UpdateLevelTask extends BukkitRunnable {

    public UpdateLevelTask(Plugin plugin) {
        this.runTaskTimerAsynchronously(plugin, 20 * UPDATE_LEVEL_INTERVAL, 20 * UPDATE_LEVEL_INTERVAL);
    }

    @Override
    public void run() {
        new HashSet<>(Bukkit.getOnlinePlayers()).forEach(PlayerLevelManager.getInstance()::update);
    }
}