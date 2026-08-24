package net.tfminecraft.tfmccore.commands;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.tfminecraft.tfmccore.stats.StatCategory;
import net.tfminecraft.tfmccore.stats.StatCategoryRegistry;
import net.tfminecraft.tfmccore.stats.StatLabelFormatter;
import net.tfminecraft.tfmccore.stats.StatManager;
import net.tfminecraft.tfmccore.stats.StatQuery;

public final class StatsCommand {
    private static final String ADMIN_PERMISSION = "tfmccore.admin";

    public boolean handle(CommandSender sender, String[] args) {
        if (!StatManager.isInitialized()) {
            sender.sendMessage("Stats are not available.");
            return true;
        }

        if (args.length < 1) {
            sender.sendMessage("Usage: /tfmc stats <category> [player]");
            return true;
        }

        String category = resolveCategory(args[0]);
        if (category == null) {
            sender.sendMessage("Unknown stat category: " + args[0]);
            return true;
        }

        StatManager manager = StatManager.getInstance();

        if (args.length >= 2) {
            if (!sender.hasPermission(ADMIN_PERMISSION)) {
                sender.sendMessage("You do not have permission to view other players' stats.");
                return true;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            UUID targetUuid = target.getUniqueId();
            sendPlayerStats(sender, category, target.getName() != null ? target.getName() : args[1], targetUuid, manager);
            sendServerTotals(sender, category, manager);
            return true;
        }

        if (sender instanceof Player player) {
            sendPlayerStats(sender, category, player.getName(), player.getUniqueId(), manager);
            if (sender.hasPermission(ADMIN_PERMISSION)) {
                sendServerTotals(sender, category, manager);
            }
            return true;
        }

        sendServerTotals(sender, category, manager);
        return true;
    }

    private static String resolveCategory(String rawCategory) {
        for (String id : StatCategoryRegistry.getCategoryIds()) {
            if (id.equalsIgnoreCase(rawCategory)) {
                return id;
            }
        }
        return null;
    }

    private static void sendPlayerStats(
            CommandSender sender,
            String category,
            String playerName,
            UUID playerUuid,
            StatManager manager) {
        Map<String, Long> stats = manager.getPlayerCategory(playerUuid, category);
        sender.sendMessage("§e" + playerName + " - " + StatLabelFormatter.formatCategoryTitle(category));

        if (stats.isEmpty()) {
            sender.sendMessage("No stats recorded for this category.");
            return;
        }

        for (StatLine line : sortStatLines(stats, category)) {
            sender.sendMessage("  " + line.label + ": " + line.value);
        }
    }

    private static void sendServerTotals(CommandSender sender, String category, StatManager manager) {
        Map<String, Long> totals = manager.getServerCategoryTotals(category);
        sender.sendMessage("§eServer totals:");

        if (totals.isEmpty()) {
            sender.sendMessage("No stats recorded for this category.");
            return;
        }

        for (StatLine line : sortStatLines(totals, category)) {
            sender.sendMessage("  " + line.label + ": " + line.value);
        }
    }

    private static List<StatLine> sortStatLines(Map<String, Long> stats, String categoryId) {
        StatQuery query = findQuery(categoryId);
        List<StatLine> lines = new ArrayList<>();
        for (Map.Entry<String, Long> entry : stats.entrySet()) {
            String label = query != null
                ? query.getLabel(entry.getKey())
                : StatLabelFormatter.format(entry.getKey());
            lines.add(new StatLine(label, entry.getValue()));
        }
        lines.sort(Comparator.comparing(StatLine::label));
        return lines;
    }

    private static StatQuery findQuery(String categoryId) {
        for (StatCategory category : StatCategoryRegistry.getCategories()) {
            if (category.getId().equalsIgnoreCase(categoryId)) {
                return category.getQuery();
            }
        }
        return null;
    }

    private static final class StatLine {
        private final String label;
        private final long value;

        private StatLine(String label, long value) {
            this.label = label;
            this.value = value;
        }

        private String label() {
            return label;
        }
    }
}
