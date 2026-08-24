package net.tfminecraft.tfmccore.stats;

import java.io.File;
import java.util.Map;
import java.util.UUID;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import net.tfminecraft.tfmccore.stats.storage.SqliteStatStorage;
import net.tfminecraft.tfmccore.stats.storage.StatStorage;

public final class StatManager {
    private static StatManager instance;

    private final JavaPlugin plugin;
    private final StatsConfig config;
    private final StatStorage storage;

    private StatManager(JavaPlugin plugin, StatsConfig config, StatStorage storage) {
        this.plugin = plugin;
        this.config = config;
        this.storage = storage;
    }

    public static void init(JavaPlugin plugin, StatsConfig config) {
        if (instance != null) {
            throw new IllegalStateException("StatManager already initialized");
        }
        File dbFile = new File(plugin.getDataFolder(), "stats.db");
        StatStorage storage = new SqliteStatStorage(dbFile);
        instance = new StatManager(plugin, config, storage);
    }

    public static StatManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("StatManager not initialized");
        }
        return instance;
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    public void increment(UUID playerUuid, String category, String statKey, long delta) {
        if (!config.isEnabled()) {
            return;
        }
        if (!isValidIncrement(playerUuid, category, statKey, delta)) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                storage.increment(playerUuid, category, statKey, delta);
            }
        }.runTaskAsynchronously(plugin);
    }

    public long getPlayerValue(UUID playerUuid, String category, String statKey) {
        if (playerUuid == null || isBlank(category) || isBlank(statKey)) {
            return 0L;
        }
        return storage.getPlayerValue(playerUuid, category, statKey);
    }

    public Map<String, Long> getPlayerCategory(UUID playerUuid, String category) {
        if (playerUuid == null || isBlank(category)) {
            return Map.of();
        }
        return storage.getPlayerCategory(playerUuid, category);
    }

    public long getServerTotal(String category, String statKey) {
        if (isBlank(category) || isBlank(statKey)) {
            return 0L;
        }
        return storage.getServerTotal(category, statKey);
    }

    public Map<String, Long> getServerCategoryTotals(String category) {
        if (isBlank(category)) {
            return Map.of();
        }
        return storage.getServerCategoryTotals(category);
    }

    public void shutdown() {
        storage.close();
        instance = null;
    }

    private static boolean isValidIncrement(UUID playerUuid, String category, String statKey, long delta) {
        return playerUuid != null && !isBlank(category) && !isBlank(statKey) && delta > 0L;
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
