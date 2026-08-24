package net.tfminecraft.tfmccore.stats.categories.vehicles;

import org.bukkit.plugin.Plugin;

import net.tfminecraft.tfmccore.stats.StatCategory;
import net.tfminecraft.tfmccore.stats.StatManager;
import net.tfminecraft.tfmccore.stats.StatQuery;

public final class VehiclesStatCategory implements StatCategory {
    private static final String CATEGORY_ID = "vehicles";

    private final VehiclesStatConfig config;
    private final VehiclesStatMain main;
    private final VehiclesStatQuery query;
    private final VehiclesStatListener listener;

    public VehiclesStatCategory(VehiclesStatConfig config) {
        this.config = config;
        this.main = new VehiclesStatMain(config);
        this.query = new VehiclesStatQuery(config);
        this.listener = new VehiclesStatListener(main);
    }

    @Override
    public String getId() {
        return CATEGORY_ID;
    }

    @Override
    public void register(Plugin plugin) {
        if (!StatManager.isInitialized()) {
            return;
        }
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    public StatQuery getQuery() {
        return query;
    }
}
