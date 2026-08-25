package net.tfminecraft.tfmccore.stats.categories.rpcharacters;

import org.bukkit.plugin.Plugin;

import net.tfminecraft.tfmccore.stats.StatCategory;
import net.tfminecraft.tfmccore.stats.StatManager;
import net.tfminecraft.tfmccore.stats.StatQuery;

public final class RpCharactersStatCategory implements StatCategory {
    private static final String CATEGORY_ID = "rpcharacters";

    private final RpCharactersStatConfig config;
    private final RpCharactersStatMain main;
    private final RpCharactersStatQuery query;
    private final RpCharactersStatListener listener;

    public RpCharactersStatCategory(RpCharactersStatConfig config) {
        this.config = config;
        this.main = new RpCharactersStatMain();
        this.query = new RpCharactersStatQuery(config);
        this.listener = new RpCharactersStatListener(main);
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
