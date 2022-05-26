package com.zpedroo.onlinetime.hooks;

import com.zpedroo.onlinetime.managers.DataManager;
import com.zpedroo.onlinetime.objects.PlayerData;
import com.zpedroo.onlinetime.utils.formatter.NumberFormatter;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private final Plugin plugin;

    public PlaceholderAPIHook(Plugin plugin) {
        this.plugin = plugin;
    }

    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    public @NotNull String getIdentifier() {
        return "onlinetime";
    }

    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public @NotNull String onPlaceholderRequest(Player player, String identifier) {
        PlayerData data = DataManager.getInstance().getPlayerData(player);
        switch (identifier.toUpperCase()) {
            case "LEVEL":
                return NumberFormatter.getInstance().formatDecimal(data.getLevel());
            default:
                return "";
        }
    }
}