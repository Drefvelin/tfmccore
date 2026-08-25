package net.tfminecraft.tfmccore.stats.categories.advancedcrafting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AdvancedCraftingStatConfigTest {
    @Test
    void labelsLoad(@TempDir Path tempDir) throws IOException {
        Path configPath = tempDir.resolve("advancedcraftingstats.yml");
        Files.writeString(configPath, """
                labels:
                  alloys_crafted: "Alloys crafted"
                  items_crafted_armor: "Armor crafted"
                  hits_whittle: "Whittles"
                category_labels:
                  armor: "Armor"
                  weapons: "Weapons"
                hit_labels:
                  whittle: "Whittle"
                """);

        AdvancedCraftingStatConfig config = new AdvancedCraftingStatConfig();
        config.load(configPath.toFile());

        assertEquals("Alloys crafted", config.getLabel("alloys_crafted"));
        assertEquals("Armor crafted", config.getLabel("items_crafted_armor"));
        assertEquals("Whittles", config.getLabel("hits_whittle"));
        assertEquals("Weapons", config.getLabel("items_crafted_weapons"));
        assertEquals("Hits Unknown Hit", config.getLabel("hits_unknown_hit"));
    }
}
