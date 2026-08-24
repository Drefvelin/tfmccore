package net.tfminecraft.tfmccore.stats.categories.vehicles;

import net.tfminecraft.tfmccore.stats.StatQuery;

public final class VehiclesStatQuery implements StatQuery {
    private final VehiclesStatConfig config;

    public VehiclesStatQuery(VehiclesStatConfig config) {
        this.config = config;
    }

    @Override
    public String getLabel(String statKey) {
        return config.getLabel(statKey);
    }
}
