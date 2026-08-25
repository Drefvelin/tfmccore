package net.tfminecraft.tfmccore.stats.categories.advancedcrafting;

import org.bukkit.plugin.Plugin;

import net.tfminecraft.tfmccore.stats.StatCategory;
import net.tfminecraft.tfmccore.stats.StatManager;
import net.tfminecraft.tfmccore.stats.StatQuery;

public final class AdvancedCraftingStatCategory implements StatCategory {
    private static final String CATEGORY_ID = "advancedcrafting";

    private final AdvancedCraftingStatConfig config;
    private final AdvancedCraftingStatMain main;
    private final AdvancedCraftingStatQuery query;
    private final AdvancedCraftingStatListener listener;

    public AdvancedCraftingStatCategory(AdvancedCraftingStatConfig config) {
        this.config = config;
        this.main = new AdvancedCraftingStatMain();
        this.query = new AdvancedCraftingStatQuery(config);
        this.listener = new AdvancedCraftingStatListener(main);
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
