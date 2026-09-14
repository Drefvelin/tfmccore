package net.tfminecraft.tfmccore.reference;

import org.bukkit.Bukkit;

import net.tfminecraft.tfmccore.cache.Cache;

public final class DropDebug {

    private DropDebug() {
    }

    public static void log(String message) {
        if (!Cache.dropsDebug) {
            return;
        }
        Bukkit.getLogger().info("[TFMCCore][drops] " + message);
    }
}
