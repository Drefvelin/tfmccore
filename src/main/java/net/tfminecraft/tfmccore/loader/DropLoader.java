package net.tfminecraft.tfmccore.loader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	public void load(File configFile) {
		clear();
		FileConfiguration config = new YamlConfiguration();
        try {
        	config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        Set<String> set = config.getKeys(false);

		List<String> list = new ArrayList<String>(set);
		
		for(String key : list) {
			Drop o = new Drop(key, config.getConfigurationSection(key));
			oList.put(key, o);
		}
	}
}
