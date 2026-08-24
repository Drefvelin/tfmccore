package net.tfminecraft.tfmccore.stats;

import org.bukkit.plugin.Plugin;

public interface StatCategory {
    String getId();

    void register(Plugin plugin);

    StatQuery getQuery();
}
