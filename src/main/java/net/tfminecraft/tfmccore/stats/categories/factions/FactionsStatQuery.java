package net.tfminecraft.tfmccore.stats.categories.factions;

import net.tfminecraft.tfmccore.stats.StatQuery;

public final class FactionsStatQuery implements StatQuery {
    private final FactionsStatConfig config;

    public FactionsStatQuery(FactionsStatConfig config) {
        this.config = config;
    }

    @Override
    public String getLabel(String statKey) {
        return config.getLabel(statKey);
    }
}
