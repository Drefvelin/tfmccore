package net.tfminecraft.tfmccore.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.tfminecraft.tfmccore.TFMCCore;
import net.tfminecraft.tfmccore.focus.FocusService;

public class CoreCommands implements CommandExecutor {
    public String cmd1 = "core";

    private static final String RELOAD_PERMISSION = "tfmccore.reload";
    private static final String ADMIN_PERMISSION = "tfmccore.admin";

    private final StatsCommand statsCommand = new StatsCommand();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase(cmd1)) {
            return false;
        }

        if (args.length == 0) {
            sendUsage(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("stats")) {
            return statsCommand.handle(sender, Arrays.copyOfRange(args, 1, args.length));
        }

        if (args[0].equalsIgnoreCase("focus")) {
            return handleFocus(sender, args);
        }

        if (args[0].equalsIgnoreCase("reload")) {
            return handleReload(sender, args);
        }

        sendUsage(sender);
        return true;
    }

    private boolean handleReload(CommandSender sender, String[] args) {
        if (!canReload(sender)) {
            sender.sendMessage("You do not have permission to reload TFMCCore.");
            return true;
        }

        String target = args.length >= 2 ? args[1].toLowerCase() : "all";
        boolean ok;
        String label;
        switch (target) {
            case "all" -> {
                ok = TFMCCore.getInstance().reloadAll();
                label = "all configs";
            }
            case "config" -> {
                ok = TFMCCore.getInstance().reloadConfigFile();
                label = "config";
            }
            case "drops" -> {
                ok = TFMCCore.getInstance().reloadDrops();
                label = "drops";
            }
            case "stations" -> {
                ok = TFMCCore.getInstance().reloadStations();
                label = "stations";
            }
            case "stats" -> {
                ok = TFMCCore.getInstance().reloadStatsConfigs();
                label = "stats";
            }
            case "focus" -> {
                ok = TFMCCore.getInstance().reloadFocusConfig();
                label = "focus";
            }
            default -> {
                sender.sendMessage("Usage: /core reload [all|config|drops|stations|stats|focus]");
                return true;
            }
        }

        if (ok) {
            sender.sendMessage("§a[TFMCCore] Reloaded " + label + ".");
        } else {
            sender.sendMessage("§c[TFMCCore] Reload failed for " + label + ". Check console.");
        }
        return true;
    }

    private boolean handleFocus(CommandSender sender, String[] args) {
        if (!sender.hasPermission(ADMIN_PERMISSION)) {
            sender.sendMessage("You do not have permission to use this command.");
            return true;
        }
        if (args.length < 3 || !args[1].equalsIgnoreCase("restore")) {
            sender.sendMessage("Usage: /core focus restore <player>");
            return true;
        }
        Player target = Bukkit.getPlayerExact(args[2]);
        if (target == null) {
            sender.sendMessage("Player not found: " + args[2]);
            return true;
        }
        FocusService focus = TFMCCore.getFocusService();
        if (focus == null || !focus.restore(target)) {
            sender.sendMessage("Could not restore focus for " + target.getName()
                    + " (no active character).");
            return true;
        }
        sender.sendMessage("Restored focus for " + target.getName() + " ("
                + focus.getPoints(target) + "/" + focus.getMax() + ").");
        return true;
    }

    private static boolean canReload(CommandSender sender) {
        return sender.hasPermission(RELOAD_PERMISSION) || sender.hasPermission(ADMIN_PERMISSION);
    }

    private static void sendUsage(CommandSender sender) {
        sender.sendMessage("§e/core stats <category> [player]");
        if (canReload(sender)) {
            sender.sendMessage("§e/core reload [all|config|drops|stations|stats|focus]");
        }
        if (sender.hasPermission(ADMIN_PERMISSION)) {
            sender.sendMessage("§e/core focus restore <player>");
        }
    }
}
