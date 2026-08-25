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
import net.tfminecraft.tfmccore.stats.categories.advancedcrafting.AdvancedCraftingStatCategory;
import net.tfminecraft.tfmccore.stats.categories.advancedcrafting.AdvancedCraftingStatConfig;
import net.tfminecraft.tfmccore.stats.categories.rpcharacters.RpCharactersStatCategory;
import net.tfminecraft.tfmccore.stats.categories.rpcharacters.RpCharactersStatConfig;
import net.tfminecraft.tfmccore.stats.categories.factions.FactionsStatCategory;
import net.tfminecraft.tfmccore.stats.categories.factions.FactionsStatConfig;
import net.tfminecraft.tfmccore.stats.categories.skills.SkillsStatCategory;
import net.tfminecraft.tfmccore.stats.categories.skills.SkillsStatConfig;
import net.tfminecraft.tfmccore.stats.categories.vehicles.VehiclesStatCategory;
import net.tfminecraft.tfmccore.stats.categories.vehicles.VehiclesStatConfig;

public class TFMCCore extends JavaPlugin{
    private static TFMCCore plugin;

    private final ConfigLoader configLoader = new ConfigLoader();
    private final DropLoader dropLoader = new DropLoader();
    private final StationLoader stationLoader = new StationLoader();
    private final StatsConfig statsConfig = new StatsConfig();
    private final VehiclesStatConfig vehiclesStatConfig = new VehiclesStatConfig();
    private final RpCharactersStatConfig rpCharactersStatConfig = new RpCharactersStatConfig();
    private final AdvancedCraftingStatConfig advancedCraftingStatConfig = new AdvancedCraftingStatConfig();
    private final SkillsStatConfig skillsStatConfig = new SkillsStatConfig();
    private final FactionsStatConfig factionsStatConfig = new FactionsStatConfig();

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
        rpCharactersStatConfig.load(new File(getDataFolder(), "rpcharactersstats.yml"));
        advancedCraftingStatConfig.load(new File(getDataFolder(), "advancedcraftingstats.yml"));
        skillsStatConfig.load(new File(getDataFolder(), "skillsstats.yml"));
        factionsStatConfig.load(new File(getDataFolder(), "factionsstats.yml"));
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
        if (getServer().getPluginManager().getPlugin("RPCharacters") != null) {
            StatCategoryRegistry.register(new RpCharactersStatCategory(rpCharactersStatConfig));
        }
        if (getServer().getPluginManager().getPlugin("AdvancedCrafting") != null) {
            StatCategoryRegistry.register(new AdvancedCraftingStatCategory(advancedCraftingStatConfig));
        }
        if (getServer().getPluginManager().getPlugin("MythicLib") != null) {
            StatCategoryRegistry.register(new SkillsStatCategory(skillsStatConfig));
        }
        if (getServer().getPluginManager().getPlugin("SimpleFactions") != null) {
            StatCategoryRegistry.register(new FactionsStatCategory(factionsStatConfig));
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
                "vehiclestats.yml",
                "rpcharactersstats.yml",
                "advancedcraftingstats.yml",
                "skillsstats.yml",
                "factionsstats.yml"
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
