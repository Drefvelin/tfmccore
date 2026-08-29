package net.tfminecraft.tfmccore.whistle;

import java.util.EnumSet;

import org.bukkit.entity.EntityType;

public final class WhistleConfig {

    public static String itemPath = "m.pets.animal_whistle";
    public static double detectionRadius = 64.0;
    public static int glowDuration = 5;
    public static int cooldownSeconds = 3;
    public static final EnumSet<EntityType> whitelistedAnimals = EnumSet.noneOf(EntityType.class);
    public static String soundName = "ITEM_GOAT_HORN_SOUND_6";
    public static float soundVolume = 4.0f;
    public static float soundPitch = 2.0f;
    public static String highlightedMessage = "&aHighlighted &6%count% &aanimals nearby.";
    public static String noAnimalsMessage = "&7No animals found nearby.";
    public static String cooldownMessage = "&7The whistle is on cooldown for another &6%seconds%s&7.";

    private WhistleConfig() {}
}
