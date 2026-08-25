package net.tfminecraft.tfmccore.stats.categories.skills;

import java.util.Locale;
import java.util.UUID;

import io.lumine.mythic.lib.api.event.skill.SkillCastEvent;
import io.lumine.mythic.lib.skill.Skill;
import io.lumine.mythic.lib.skill.handler.SkillHandler;
import io.lumine.mythic.lib.skill.trigger.TriggerType;
import net.Indyuce.mmocore.skill.CastableSkill;
import net.Indyuce.mmocore.skill.RegisteredSkill;
import net.tfminecraft.tfmccore.stats.StatManager;

public final class SkillsStatMain {
    private static final String CATEGORY_ID = "skills";
    private static final String SKILL_KEY_PREFIX = "skill_";

    public void handle(SkillCastEvent event) {
        if (!StatManager.isInitialized()) {
            return;
        }

        Skill cast = event.getCast();
        if (cast == null) {
            return;
        }

        TriggerType trigger = cast.getTrigger();
        if (trigger != TriggerType.CAST && trigger != TriggerType.API) {
            return;
        }

        String skillId = resolveSkillId(cast);
        if (skillId == null || skillId.isBlank()) {
            return;
        }

        UUID playerUuid = event.getPlayer().getUniqueId();
        String statKey = SKILL_KEY_PREFIX + skillId.toLowerCase(Locale.ROOT);
        StatManager.getInstance().increment(playerUuid, CATEGORY_ID, statKey, 1L);
    }

    private static String resolveSkillId(Skill cast) {
        if (cast instanceof CastableSkill castable) {
            RegisteredSkill registered = castable.getSkill().getSkill();
            if (registered != null && registered.getName() != null && !registered.getName().isBlank()) {
                return registered.getName();
            }
        }

        SkillHandler<?> handler = cast.getHandler();
        if (handler != null) {
            return handler.getLowerCaseId();
        }

        return null;
    }
}
