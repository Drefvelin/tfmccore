package net.tfminecraft.tfmccore.loader;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.tfminecraft.tfmccore.cache.Cache;

public class ConfigLoader {
    public boolean loadConfig(File configFile) {
		FileConfiguration config = new YamlConfiguration();
        try {
        	config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return false;
        }

        Cache.blockedConsume.clear();
        Cache.blockedCrafts.clear();

        Cache.allowBoneMeal = config.getBoolean("bone-meal", true);
        Cache.limitShields = config.getBoolean("limit-shields", false);
        Cache.allowBrewing = config.getBoolean("allow-brewing", true);
        Cache.allowEnchanting = config.getBoolean("allow-enchanting", true);
        Cache.horseArchery = config.getBoolean("horse-archery", true);

        Cache.armourTime = config.getInt("armour-time", 7);

        if(config.contains("blocked-consume")) {
            for(String s : config.getStringList("blocked-consume")) {
                try {
                    Cache.blockedConsume.add(Material.valueOf(s.toUpperCase()));
                } catch (Exception e) {
                    Bukkit.getLogger().info("[TFMCCore] could not convert "+s+" to a material");
                }
            }
        }

        if(config.contains("blocked-craft")) {
            for(String s : config.getStringList("blocked-craft")) {
                try {
                    Cache.blockedCrafts.add(Material.valueOf(s.toUpperCase()));
                } catch (Exception e) {
                    Bukkit.getLogger().info("[TFMCCore] could not convert "+s+" to a material");
                }
            }
        }
        return true;
	}
}
