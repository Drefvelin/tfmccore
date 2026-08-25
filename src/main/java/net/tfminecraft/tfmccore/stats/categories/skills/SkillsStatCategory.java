package net.tfminecraft.tfmccore.stats.categories.skills;

import org.bukkit.plugin.Plugin;

import net.tfminecraft.tfmccore.stats.StatCategory;
import net.tfminecraft.tfmccore.stats.StatManager;
import net.tfminecraft.tfmccore.stats.StatQuery;

public final class SkillsStatCategory implements StatCategory {
    private static final String CATEGORY_ID = "skills";

    private final SkillsStatConfig config;
    private final SkillsStatMain main;
    private final SkillsStatQuery query;
    private final SkillsStatListener listener;

    public SkillsStatCategory(SkillsStatConfig config) {
        this.config = config;
        this.main = new SkillsStatMain();
        this.query = new SkillsStatQuery(config);
        this.listener = new SkillsStatListener(main);
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
