package net.tfminecraft.tfmccore.reference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Plugins.TLibs.TLibs;

public class Drop {
    private String id;
    private boolean vanillaDrops;

    private Map<Material, Double> blocks = new HashMap<>();
    private Map<String, Double> mults = new HashMap<>();
    private Map<String, Double> tools = new HashMap<>();
    private List<DropEntry> drops = new ArrayList<>();

    public Drop(String key, ConfigurationSection config) {
        id = key;
        vanillaDrops = config.getBoolean("vanilla-drops", true);

        for(String s : config.getStringList("materials")) {
            String[] args = s.split("\\(");
            Material m = Material.AIR;
            try {
                m = Material.valueOf(args[0].toUpperCase());
            } catch (Exception e) {
                Bukkit.getLogger().info("[TFMCCore] could not convert "+args[0]+" to a material");
            }
            if(m.equals(Material.AIR)) continue;
            if(args.length == 1) blocks.put(m, 1.0);
            else {
                String mult = args[1].replace(")", "");
                try {
                    double d = Double.parseDouble(mult);
                    blocks.put(m, d);
                } catch (Exception e) {
                    Bukkit.getLogger().info("[TFMCCore] could not parse "+mult+" to a Double");
                }
            }
        }
        if(config.contains("mults")) {
            for(String s : config.getStringList("mults")) {
                String[] args = s.split("\\(");
                if(args.length <= 1) continue;
                else {
                    String mult = args[1].replace(")", "");
                    try {
                        double d = Double.parseDouble(mult);
                        mults.put(args[0], d);
                    } catch (Exception e) {
                        Bukkit.getLogger().info("[TFMCCore] could not parse "+mult+" to a Double");
                    }
                }
            }
        }
        if(config.contains("tools")) {
            for(String s : config.getStringList("tools")) {
                parseTool(s);
            }
        }
        if(config.contains("tool")) parseTool(config.getString("tool"));

        for(String s : config.getStringList("drops")) {
            drops.add(new DropEntry(s));
        }
    }

    private void parseTool(String s) {
        String[] args = s.split("\\(");
        if(args.length <= 1) tools.put(args[0], 1.0);
        else {
            String mult = args[1].replace(")", "");
            try {
                double d = Double.parseDouble(mult);
                tools.put(args[0], d);
            } catch (Exception e) {
                Bukkit.getLogger().info("[TFMCCore] could not parse "+mult+" to a Double");
            }
        }
    }

    public String getId() {
        return id;
    }

    public boolean hasVanillaDrops(Block block) {
        if(!blocks.containsKey(block.getType())) return true;
        return vanillaDrops;
    }

    public void trigger(Player p, Block block, ItemStack tool) {
        if(!blocks.containsKey(block.getType())) return;
        double seed = Math.random();
        for(DropEntry drop : drops) {
            double chance = getFinalChance(drop.getChance(), p, tool, block.getType());
            if(seed <= chance) {
                drop(drop, block);
            }
        }
    }

    private void drop(DropEntry drop, Block block) {
        ItemStack item = TLibs.getItemAPI().getCreator().getItemFromPath(drop.getItem());
        item.setAmount(drop.getAmount());
        block.getWorld().dropItem(
            block.getLocation().clone().add(0.5, 0.2, 0.5),
            item
        );
    }

    private double getFinalChance(Double chance, Player p, ItemStack tool, Material m) {
        chance *= blocks.get(m);
        for(String t : tools.keySet()) {
            if(TLibs.getItemAPI().getChecker().checkItemWithPath(tool, t)) {
                chance *= tools.get(t);
            }
        }
        for(Map.Entry<String, Double> mult : mults.entrySet()) {
            if(p.hasPermission(mult.getKey())) {
                chance *= mult.getValue();
            }
        }
        if(tool != null && tool.containsEnchantment(Enchantment.FORTUNE)) {
            int fortuneLevel = tool.getEnchantmentLevel(Enchantment.FORTUNE);
            chance = 1 - Math.pow(1 - chance, fortuneLevel + 1);
        }
        return chance;
    }
}
