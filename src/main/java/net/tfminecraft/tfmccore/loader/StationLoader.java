package net.tfminecraft.tfmccore.loader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class StationLoader {
    static Map<String, String> oList = new HashMap<>();
	public static void clear() {
		oList.clear();
	}
	public static Map<String, String> get() {
		return oList;
	}
	public static String getByString(String id) {
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

		List<String> list = config.getStringList("stations");
		
		for(String s : list) {
			String[] args = s.split("\\s+");
            if(args.length < 2) continue;
            oList.put(args[0], args[1]);
		}
		return true;
	}
}
