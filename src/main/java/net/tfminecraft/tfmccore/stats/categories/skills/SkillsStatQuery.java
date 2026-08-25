package net.tfminecraft.tfmccore.stats.categories.skills;

import net.tfminecraft.tfmccore.stats.StatQuery;

public final class SkillsStatQuery implements StatQuery {
    private final SkillsStatConfig config;

    public SkillsStatQuery(SkillsStatConfig config) {
        this.config = config;
    }

    @Override
    public String getLabel(String statKey) {
        return config.getLabel(statKey);
    }
}
