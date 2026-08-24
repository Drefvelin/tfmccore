package net.tfminecraft.tfmccore.reference;

import org.bukkit.Bukkit;

public class DropEntry {
    private String item;
    private double chance = 0;
    private int min = 1;
    private int max = 1;

    public DropEntry(String s) {
        String[] full = s.split("\\s+");
        String first = full[0];
        String[] args = first.split("\\(");
        item = args[0];
        if(args.length > 1) {
            String c = args[1].replace(")", "");
            try {
                double d = Double.parseDouble(c);
                chance = d;
            } catch (Exception e) {
                chance = 0;
                Bukkit.getLogger().info("[TFMCCore] could not parse "+chance+" to a Double");
            }
        }
        if(full.length > 1) {
            try {
                min = Integer.parseInt(full[1]);
            } catch (Exception e) {
                Bukkit.getLogger().info("[TFMCCore] could not parse "+full[1]+" to an Integer");
            }
            if(full.length > 2) {
                try {
                    max = Integer.parseInt(full[2]);
                } catch (Exception e) {
                    Bukkit.getLogger().info("[TFMCCore] could not parse "+full[2]+" to an Integer");
                }
            }   
        }
    }

    public String getItem() {
        return item;
    }

    public double getChance() {
        return chance;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getAmount() {
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }
}
