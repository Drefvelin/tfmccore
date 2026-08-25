package net.tfminecraft.tfmccore.stats.categories.rpcharacters;

import net.tfminecraft.tfmccore.stats.StatQuery;

public final class RpCharactersStatQuery implements StatQuery {
    private final RpCharactersStatConfig config;

    public RpCharactersStatQuery(RpCharactersStatConfig config) {
        this.config = config;
    }

    @Override
    public String getLabel(String statKey) {
        return config.getLabel(statKey);
    }
}
