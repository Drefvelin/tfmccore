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
    private static final String RELOAD_PERMISSION = "tfmccore.reload";
    private static final List<String> RELOAD_TARGETS = List.of("all", "config", "drops", "stations", "stats", "focus", "whistle");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("core")) {
            return List.of();
        }

        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("stats");
            if (canReload(sender)) {
                completions.add("reload");
            }
            if (sender.hasPermission(ADMIN_PERMISSION)) {
                completions.add("focus");
            }
            TabCleaner.cleanTab(completions, args);
            return completions;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("reload") && canReload(sender)) {
            List<String> completions = new ArrayList<>(RELOAD_TARGETS);
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

        if (args.length == 2 && args[0].equalsIgnoreCase("focus") && sender.hasPermission(ADMIN_PERMISSION)) {
            List<String> completions = new ArrayList<>();
            completions.add("restore");
            TabCleaner.cleanTab(completions, args);
            return completions;
        }

        if (args.length == 3 && args[0].equalsIgnoreCase("focus")
                && args[1].equalsIgnoreCase("restore") && sender.hasPermission(ADMIN_PERMISSION)) {
            List<String> completions = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                completions.add(player.getName());
            }
            TabCleaner.cleanTab(completions, args);
            return completions;
        }

        return List.of();
    }

    private static boolean canReload(CommandSender sender) {
        return sender.hasPermission(RELOAD_PERMISSION) || sender.hasPermission(ADMIN_PERMISSION);
    }
}
