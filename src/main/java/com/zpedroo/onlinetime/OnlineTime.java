package com.zpedroo.onlinetime;

import com.zpedroo.onlinetime.commands.OnlineTimeCmd;
import com.zpedroo.onlinetime.hooks.PlaceholderAPIHook;
import com.zpedroo.onlinetime.listeners.PlayerChatListener;
import com.zpedroo.onlinetime.listeners.PlayerGeneralListeners;
import com.zpedroo.onlinetime.managers.DataManager;
import com.zpedroo.onlinetime.managers.InventoryManager;
import com.zpedroo.onlinetime.managers.PlayerLevelManager;
import com.zpedroo.onlinetime.mysql.DBConnection;
import com.zpedroo.onlinetime.tasks.SaveTask;
import com.zpedroo.onlinetime.tasks.UpdateLevelTask;
import com.zpedroo.onlinetime.utils.FileUtils;
import com.zpedroo.onlinetime.utils.formatter.NumberFormatter;
import com.zpedroo.onlinetime.utils.formatter.TimeFormatter;
import com.zpedroo.onlinetime.utils.menu.Menus;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;

import static com.zpedroo.onlinetime.utils.config.Settings.*;

public class OnlineTime extends JavaPlugin {

    private static OnlineTime instance;
    public static OnlineTime get() { return instance; }

    public void onEnable() {
        instance = this;
        new FileUtils(this);

        if (!isMySQLEnabled(getConfig())) {
            getLogger().log(Level.SEVERE, "MySQL are disabled! You need to enable it.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        new DBConnection(getConfig());
        new NumberFormatter(getConfig());
        new TimeFormatter();
        new DataManager();
        new InventoryManager();
        new PlayerLevelManager();
        new SaveTask(this);
        new UpdateLevelTask(this);
        new Menus();

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceholderAPIHook(this).register();
        }

        registerCommand(COMMAND, ALIASES, new OnlineTimeCmd());
        registerListeners();
    }

    public void onDisable() {
        if (!isMySQLEnabled(getConfig())) return;

        try {
            DataManager.getInstance().saveAllPlayersData();
            DBConnection.getInstance().closeConnection();
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "An error occurred while trying to save data!");
            ex.printStackTrace();
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerChatListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerGeneralListeners(), this);
    }

    private void registerCommand(String command, List<String> aliases, CommandExecutor executor) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            PluginCommand pluginCmd = constructor.newInstance(command, this);
            pluginCmd.setAliases(aliases);
            pluginCmd.setExecutor(executor);

            Field field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            commandMap.register(getName().toLowerCase(), pluginCmd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Boolean isMySQLEnabled(FileConfiguration file) {
        if (!file.contains("MySQL.enabled")) return false;

        return file.getBoolean("MySQL.enabled");
    }
}