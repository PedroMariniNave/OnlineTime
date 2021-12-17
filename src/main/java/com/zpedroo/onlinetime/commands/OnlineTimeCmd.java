package com.zpedroo.onlinetime.commands;

import com.zpedroo.onlinetime.utils.menu.Menus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OnlineTimeCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (args.length > 0) {
            switch (args[0].toUpperCase()) {
                case "TOP":
                    Menus.getInstance().openTopMenu(player);
                    return true;

                case "SHOP":
                    Menus.getInstance().openShopMenu(player);
                    return true;
            }
        }

        Menus.getInstance().openMainMenu(player);
        return false;
    }
}