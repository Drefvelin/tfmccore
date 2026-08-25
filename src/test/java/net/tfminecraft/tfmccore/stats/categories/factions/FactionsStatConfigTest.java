package net.tfminecraft.tfmccore.stats.categories.factions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FactionsStatConfigTest {
    @Test
    void labelsLoad(@TempDir Path tempDir) throws IOException {
        Path configPath = tempDir.resolve("factionsstats.yml");
        Files.writeString(configPath, """
                labels:
                  battles_joined: "Battles joined"
                """);

        FactionsStatConfig config = new FactionsStatConfig();
        config.load(configPath.toFile());

        assertEquals("Battles joined", config.getLabel("battles_joined"));
        assertEquals("Battles Won", config.getLabel("battles_won"));
    }
}
