package net.tfminecraft.tfmccore.stats.categories.factions;

import org.bukkit.plugin.Plugin;

import net.tfminecraft.tfmccore.stats.StatCategory;
import net.tfminecraft.tfmccore.stats.StatManager;
import net.tfminecraft.tfmccore.stats.StatQuery;

public final class FactionsStatCategory implements StatCategory {
    private static final String CATEGORY_ID = "factions";

    private final FactionsStatConfig config;
    private final FactionsStatMain main;
    private final FactionsStatQuery query;
    private final FactionsStatListener listener;

    public FactionsStatCategory(FactionsStatConfig config) {
        this.config = config;
        this.main = new FactionsStatMain();
        this.query = new FactionsStatQuery(config);
        this.listener = new FactionsStatListener(main);
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
