package net.tfminecraft.tfmccore.loader;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.tfminecraft.tfmccore.reference.Drop;

public class DropLoader {
    static Map<String, Drop> oList = new HashMap<>();
	public static void clear() {
		oList.clear();
	}
	public static Collection<Drop> get() {
		return oList.values();
	}
	public static Drop getByString(String id) {
		return oList.get(id);
	}
	public boolean load(File configFile) {
		clear();
		FileConfiguration config = new YamlConfiguration();
        try {
        	config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }
        ConfigurationSection root = config.getConfigurationSection("drops");
        if (root == null) {
            root = config;
        }

		for (String key : root.getKeys(false)) {
			ConfigurationSection section = root.getConfigurationSection(key);
			if (section == null) continue;
			oList.put(key, new Drop(key, section));
		}
		return true;
	}
}
