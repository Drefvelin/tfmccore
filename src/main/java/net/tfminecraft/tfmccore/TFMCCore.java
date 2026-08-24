package net.tfminecraft.tfmccore;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import me.Plugins.TLibs.database.SqliteProvider;
import net.tfminecraft.tfmccore.commands.CoreCommands;
import net.tfminecraft.tfmccore.commands.CoreTabCompletion;
import net.tfminecraft.tfmccore.loader.ConfigLoader;
import net.tfminecraft.tfmccore.loader.DropLoader;
import net.tfminecraft.tfmccore.loader.StationLoader;
import net.tfminecraft.tfmccore.manager.CoreManager;
import net.tfminecraft.tfmccore.manager.DropManager;
import net.tfminecraft.tfmccore.manager.StationManager;
import net.tfminecraft.tfmccore.stats.StatCategoryRegistry;
import net.tfminecraft.tfmccore.stats.StatManager;
import net.tfminecraft.tfmccore.stats.StatsConfig;
import net.tfminecraft.tfmccore.stats.categories.vehicles.VehiclesStatCategory;
import net.tfminecraft.tfmccore.stats.categories.vehicles.VehiclesStatConfig;

public class TFMCCore extends JavaPlugin{
    private static TFMCCore plugin;

    private final ConfigLoader configLoader = new ConfigLoader();
    private final DropLoader dropLoader = new DropLoader();
    private final StationLoader stationLoader = new StationLoader();
    private final StatsConfig statsConfig = new StatsConfig();
    private final VehiclesStatConfig vehiclesStatConfig = new VehiclesStatConfig();

    private final CoreManager coreManager = new CoreManager();
    private final StationManager stationManager = new StationManager();
    private final DropManager dropManager = new DropManager();

    private final CoreCommands commands = new CoreCommands();
    private final CoreTabCompletion tabCompletion = new CoreTabCompletion();

    @Override
    public void onEnable() {
        plugin = this;
        createConfigs();
        loadConfigs();
        initStats();
        registerListeners();
        getCommand(commands.cmd1).setExecutor(commands);
        getCommand(commands.cmd1).setTabCompleter(tabCompletion);
    }

    @Override
    public void onDisable() {
        if (StatManager.isInitialized()) {
            StatManager.getInstance().shutdown();
        }
    }

    public static TFMCCore getInstance() {
        return plugin;
    }

    public static StatManager getStatManager() {
        return StatManager.getInstance();
    }

    public void loadConfigs() {
        configLoader.loadConfig(new File(getDataFolder(), "config.yml"));
        dropLoader.load(new File(getDataFolder(), "drops.yml"));
        stationLoader.load(new File(getDataFolder(), "stations.yml"));
        statsConfig.load(new File(getDataFolder(), "stats.yml"));
        vehiclesStatConfig.load(new File(getDataFolder(), "vehiclestats.yml"));
    }

    private void initStats() {
        if (!statsConfig.isEnabled()) {
            return;
        }
        if (!SqliteProvider.isAvailable()) {
            getLogger().warning("SQLite unavailable; stats disabled");
            return;
        }

        StatManager.init(this, statsConfig);
        if (getServer().getPluginManager().getPlugin("VehicleFramework") != null) {
            StatCategoryRegistry.register(new VehiclesStatCategory(vehiclesStatConfig));
        }
        StatCategoryRegistry.registerAll(this);
        getLogger().info("stats enabled, db ready");
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(dropManager, this);
        getServer().getPluginManager().registerEvents(stationManager, this);
        getServer().getPluginManager().registerEvents(coreManager, this);
    }

    public void createConfigs() {
        String[] files = {
                "config.yml",
                "drops.yml",
                "stations.yml",
                "stats.yml",
                "vehiclestats.yml"
        };

        for (String s : files) {
            File newConfigFile = new File(getDataFolder(), s);
            if (!newConfigFile.exists()) {
                newConfigFile.getParentFile().mkdirs();
                saveResource(s, false);
            }
        }
    }
}
