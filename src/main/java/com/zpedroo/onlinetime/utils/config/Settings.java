package com.zpedroo.onlinetime.utils.config;

import com.zpedroo.onlinetime.utils.FileUtils;
import org.bukkit.ChatColor;

import java.util.List;

public class Settings {

    public static final String COMMAND = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.command");

    public static final List<String> ALIASES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.aliases");

    public static final Long SAVE_INTERVAL = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.save-interval");

    public static final Long UPDATE_LEVEL_INTERVAL = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.update-level-interval");

    public static final Long POINTS_PER_HOUR = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.points-per-hour");

    public static final String DATE_FORMAT = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.date-format");

    public static final String TAG = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.tag"));
}