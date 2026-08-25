package net.tfminecraft.tfmccore.stats.categories.skills;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import io.lumine.mythic.lib.api.event.skill.SkillCastEvent;

public final class SkillsStatListener implements Listener {
    private final SkillsStatMain main;

    public SkillsStatListener(SkillsStatMain main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSkillCast(SkillCastEvent event) {
        main.handle(event);
    }
}
