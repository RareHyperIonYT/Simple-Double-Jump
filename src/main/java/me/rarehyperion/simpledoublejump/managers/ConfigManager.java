package me.rarehyperion.simpledoublejump.managers;

import me.rarehyperion.simpledoublejump.enums.ActivationMethod;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;

    private double horizontalForceMultiplier, verticalVelocity;
    private boolean preserveFallDamage, avoidFlyConflicts;
    private int cooldownTicks, hungerDrain;

    private ActivationMethod activationMethod;

    public ConfigManager(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        this.plugin.saveDefaultConfig();
        this.plugin.reloadConfig();
        this.config = this.plugin.getConfig();

        final String activationMethodString = this.config.getString("activationMethod", "ALL");

        try {
            this.activationMethod = ActivationMethod.valueOf(activationMethodString.toUpperCase());
        } catch (final IllegalArgumentException exception) {
            this.activationMethod = ActivationMethod.ALL;
        }

        this.preserveFallDamage = this.config.getBoolean("preserveFallDamage", true);
        this.avoidFlyConflicts = this.config.getBoolean("avoidFlyConflicts", true);

        this.cooldownTicks = this.config.getInt("jump-settings.cooldown-ticks", 100);
        this.hungerDrain = this.config.getInt("jump-settings.hunger-drain", 2);

        this.horizontalForceMultiplier = this.config.getDouble("jump-settings.horizontal-force-multiplier", 0.42);
        this.verticalVelocity = this.config.getDouble("jump-settings.vertical-velocity", 0.42);
    }

    public boolean shouldPreserveFallDamage() {
        return this.preserveFallDamage;
    }

    public boolean shouldAvoidFlyConflicts() {
        return this.avoidFlyConflicts;
    }

    public int getCooldownTicks() {
        return this.cooldownTicks;
    }

    public int getHungerDrain() {
        return this.hungerDrain;
    }

    public double getHorizontalForceMultiplier() {
        return this.horizontalForceMultiplier;
    }

    public double getVerticalVelocity() {
        return this.verticalVelocity;
    }

    public ActivationMethod getActivationMethod() {
        return this.activationMethod;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }

}
