package net.tfminecraft.tfmccore.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.Plugins.TLibs.Utils.TabCleaner;
import net.tfminecraft.tfmccore.stats.StatCategoryRegistry;

public final class CoreTabCompletion implements TabCompleter {
    private static final String ADMIN_PERMISSION = "tfmccore.admin";

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("tfmc")) {
            return List.of();
        }

        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("stats");
            TabCleaner.cleanTab(completions, args);
            return completions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("stats")) {
            List<String> completions = new ArrayList<>(StatCategoryRegistry.getCategoryIds());
            TabCleaner.cleanTab(completions, args);
            return completions;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("stats") && sender.hasPermission(ADMIN_PERMISSION)) {
            List<String> completions = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
            TabCleaner.cleanTab(completions, args);
            return completions;
        }

        return List.of();
    }
}
