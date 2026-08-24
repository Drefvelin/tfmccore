package net.tfminecraft.tfmccore.stats;

import java.util.UUID;

public final class StatRecord {
    private final UUID playerUuid;
    private final String category;
    private final String statKey;
    private final long value;

    public StatRecord(UUID playerUuid, String category, String statKey, long value) {
        this.playerUuid = playerUuid;
        this.category = category;
        this.statKey = statKey;
        this.value = value;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public String getCategory() {
        return category;
    }

    public String getStatKey() {
        return statKey;
    }

    public long getValue() {
        return value;
    }
}
