package net.tfminecraft.tfmccore.stats.categories.skills;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SkillsStatConfigTest {
    @Test
    void labelsLoad(@TempDir Path tempDir) throws IOException {
        Path configPath = tempDir.resolve("skillsstats.yml");
        Files.writeString(configPath, """
                labels:
                  skill_fireball: "Fireball"
                skill_labels:
                  heal: "Heal"
                """);

        SkillsStatConfig config = new SkillsStatConfig();
        config.load(configPath.toFile());

        assertEquals("Fireball", config.getLabel("skill_fireball"));
        assertEquals("Heal", config.getLabel("skill_heal"));
        assertEquals("Skill Unknown Spell", config.getLabel("skill_unknown_spell"));
    }
}
