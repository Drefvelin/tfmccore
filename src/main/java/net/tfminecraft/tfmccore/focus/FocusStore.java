package net.tfminecraft.tfmccore.focus;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.tfminecraft.tfmccore.TFMCCore;

public final class FocusStore {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final File folder;

    public FocusStore(File folder) {
        this.folder = folder;
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public FocusData load(String characterId) {
        if (characterId == null || characterId.isBlank()) {
            return null;
        }
        File file = fileFor(characterId);
        if (!file.exists()) {
            return null;
        }
        try (Reader reader = new FileReader(file)) {
            FocusData data = GSON.fromJson(reader, FocusData.class);
            if (data != null && (data.getCharacterId() == null || data.getCharacterId().isBlank())) {
                data.setCharacterId(characterId);
            }
            return data;
        } catch (IOException | RuntimeException ex) {
            TFMCCore.getInstance().getLogger().severe("[TFMCCore] Failed to load focus " + characterId
                    + ": " + ex.getMessage());
            return null;
        }
    }

    public void save(FocusData data) {
        if (data == null || data.getCharacterId() == null || data.getCharacterId().isBlank()) {
            return;
        }
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File target = fileFor(data.getCharacterId());
        File temp = new File(folder, data.getCharacterId() + ".json.tmp");
        try (Writer writer = new FileWriter(temp)) {
            GSON.toJson(data, writer);
        } catch (IOException ex) {
            TFMCCore.getInstance().getLogger().severe("[TFMCCore] Failed to write focus "
                    + data.getCharacterId() + ": " + ex.getMessage());
            return;
        }
        try {
            Files.move(temp.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            TFMCCore.getInstance().getLogger().severe("[TFMCCore] Failed to replace focus "
                    + data.getCharacterId() + ": " + ex.getMessage());
        }
    }

    public static FocusData tryMigrateFromResearch(String characterId, String ownerUuid) {
        if (ownerUuid == null || ownerUuid.isBlank()) {
            return null;
        }
        File researchFile = new File("plugins/Research/data/players/" + ownerUuid + ".json");
        if (!researchFile.exists()) {
            return null;
        }
        try (Reader reader = new FileReader(researchFile)) {
            JsonObject object = JsonParser.parseReader(reader).getAsJsonObject();
            if (!object.has("mental_points")) {
                return null;
            }
            FocusData data = FocusData.createNew(characterId, ownerUuid);
            data.setPoints(object.get("mental_points").getAsInt());
            if (object.has("last_regen_ms")) {
                data.setLastRegenMs(object.get("last_regen_ms").getAsLong());
            }
            return data;
        } catch (Exception ex) {
            return null;
        }
    }

    private File fileFor(String characterId) {
        return new File(folder, characterId + ".json");
    }
}
