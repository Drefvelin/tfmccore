package net.tfminecraft.tfmccore.stats.categories.advancedcrafting;

import net.tfminecraft.tfmccore.stats.StatQuery;

public final class AdvancedCraftingStatQuery implements StatQuery {
    private final AdvancedCraftingStatConfig config;

    public AdvancedCraftingStatQuery(AdvancedCraftingStatConfig config) {
        this.config = config;
    }

    @Override
    public String getLabel(String statKey) {
        return config.getLabel(statKey);
    }
}
