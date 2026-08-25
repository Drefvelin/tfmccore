package net.tfminecraft.tfmccore.stats.categories.rpcharacters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class RpCharactersStatConfigTest {
    @Test
    void labelsLoad(@TempDir Path tempDir) throws IOException {
        Path configPath = tempDir.resolve("rpcharactersstats.yml");
        Files.writeString(configPath, """
                labels:
                  characters_created: "Characters created"
                  characters_killed: "Characters killed"
                  messages_rp: "RP messages sent"
                  messages_ooc: "OOC messages sent"
                class_labels:
                  warrior: "Warrior"
                race_labels:
                  human: "Human"
                """);

        RpCharactersStatConfig config = new RpCharactersStatConfig();
        config.load(configPath.toFile());

        assertEquals("Characters created", config.getLabel("characters_created"));
        assertEquals("RP messages sent", config.getLabel("messages_rp"));
        assertEquals("Messages Unknown", config.getLabel("messages_unknown"));
        assertEquals("Warrior", config.getLabel("class_warrior"));
        assertEquals("Human", config.getLabel("race_human"));
        assertEquals("Class Mage", config.getLabel("class_mage"));
    }
}
