package net.tfminecraft.tfmccore.stats.storage;

import java.util.Map;
import java.util.UUID;

public interface StatStorage {
    void increment(UUID playerUuid, String category, String statKey, long delta);

    long getPlayerValue(UUID playerUuid, String category, String statKey);

    Map<String, Long> getPlayerCategory(UUID playerUuid, String category);

    long getServerTotal(String category, String statKey);

    Map<String, Long> getServerCategoryTotals(String category);

    void close();
}
