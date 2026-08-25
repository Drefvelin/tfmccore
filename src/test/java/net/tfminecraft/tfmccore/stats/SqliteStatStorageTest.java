package net.tfminecraft.tfmccore.stats;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.tfminecraft.tfmccore.stats.storage.SqliteStatStorage;

class SqliteStatStorageTest {
    @Test
    void incrementAccumulatesPlayerAndServerTotals(@TempDir Path tempDir) {
        File dbFile = tempDir.resolve("stats.db").toFile();
        SqliteStatStorage storage = new SqliteStatStorage(dbFile);

        UUID playerUuid = UUID.fromString("00000000-0000-0000-0000-000000000001");
        String category = "test";
        String statKey = "smoke";

        storage.increment(playerUuid, category, statKey, 1L);
        storage.increment(playerUuid, category, statKey, 2L);

        assertEquals(3L, storage.getPlayerValue(playerUuid, category, statKey));
        assertEquals(3L, storage.getServerTotal(category, statKey));

        Map<String, Long> playerCategory = storage.getPlayerCategory(playerUuid, category);
        assertEquals(3L, playerCategory.get(statKey));

        Map<String, Long> serverCategory = storage.getServerCategoryTotals(category);
        assertEquals(3L, serverCategory.get(statKey));

        storage.close();
    }

    @Test
    void decrementAndFloorAtZero(@TempDir Path tempDir) {
        File dbFile = tempDir.resolve("stats-decrement.db").toFile();
        SqliteStatStorage storage = new SqliteStatStorage(dbFile);

        UUID playerUuid = UUID.fromString("00000000-0000-0000-0000-000000000002");
        String category = "test";
        String statKey = "spread";

        storage.increment(playerUuid, category, statKey, 2L);
        storage.increment(playerUuid, category, statKey, -1L);
        assertEquals(1L, storage.getPlayerValue(playerUuid, category, statKey));

        storage.increment(playerUuid, category, statKey, -5L);
        assertEquals(0L, storage.getPlayerValue(playerUuid, category, statKey));
        assertEquals(0L, storage.getServerTotal(category, statKey));

        storage.close();
    }
}
