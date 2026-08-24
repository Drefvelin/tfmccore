package net.tfminecraft.tfmccore.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.plugin.Plugin;

public final class StatCategoryRegistry {
    private static final List<StatCategory> categories = new ArrayList<>();

    private StatCategoryRegistry() {}

    public static void register(StatCategory category) {
        if (category == null) {
            return;
        }
        for (StatCategory existing : categories) {
            if (existing.getId().equalsIgnoreCase(category.getId())) {
                return;
            }
        }
        categories.add(category);
    }

    public static List<String> getCategoryIds() {
        List<String> ids = new ArrayList<>();
        for (StatCategory category : categories) {
            ids.add(category.getId());
        }
        return ids;
    }

    public static void registerAll(Plugin plugin) {
        for (StatCategory category : categories) {
            category.register(plugin);
        }
    }

    public static List<StatCategory> getCategories() {
        return Collections.unmodifiableList(categories);
    }
}
