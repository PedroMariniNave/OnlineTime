package com.zpedroo.onlinetime.tasks;

import com.zpedroo.onlinetime.managers.DataManager;
import com.zpedroo.onlinetime.mysql.DBConnection;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import static com.zpedroo.onlinetime.utils.config.Settings.*;

public class SaveTask extends BukkitRunnable {

    public SaveTask(Plugin plugin) {
        this.runTaskTimerAsynchronously(plugin, 20 * SAVE_INTERVAL, 20 * SAVE_INTERVAL);
    }

    @Override
    public void run() {
        DataManager.getInstance().saveAll();
        DataManager.getInstance().getCache().setTop(DBConnection.getInstance().getDBManager().getTop());
    }
}