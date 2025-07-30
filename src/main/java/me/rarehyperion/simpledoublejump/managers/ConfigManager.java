package me.rarehyperion.simpledoublejump.managers;

import me.rarehyperion.simpledoublejump.enums.ActivationMethod;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {

    private final JavaPlugin plugin;

    private double horizontalForceMultiplier, verticalVelocity;
    private boolean preserveFallDamage, avoidFlyConflicts, hardenedGroundCheck;
    private int cooldownTicks, hungerDrain;

    // SFX
    private boolean soundEffects;
    private float soundVolume, soundPitch;
    private String soundSource;

    // VFX
    private boolean particleEffects;
    private int particleCount;
    private double particleSpread;
    private String particleType;

    private ActivationMethod activationMethod;

    public ConfigManager(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        this.plugin.saveDefaultConfig();
        this.plugin.reloadConfig();

        final FileConfiguration config = this.plugin.getConfig();
        final String activationMethodString = config.getString("activationMethod", "ALL");

        try {
            this.activationMethod = ActivationMethod.valueOf(activationMethodString.toUpperCase());
        } catch (final IllegalArgumentException exception) {
            this.activationMethod = ActivationMethod.ALL;
        }

        this.preserveFallDamage = config.getBoolean("preserveFallDamage", true);
        this.avoidFlyConflicts = config.getBoolean("avoidFlyConflicts", true);
        this.hardenedGroundCheck = config.getBoolean("hardenedGroundCheck", false);

        this.cooldownTicks = config.getInt("jump-settings.cooldown-ticks", 100);
        this.hungerDrain = config.getInt("jump-settings.hunger-drain", 2);

        this.horizontalForceMultiplier = config.getDouble("jump-settings.horizontal-force-multiplier", 0.42);
        this.verticalVelocity = config.getDouble("jump-settings.vertical-velocity", 0.42);

        // Jump SFX
        this.soundEffects = config.getBoolean("jump-settings.effects.sound.enabled", true);
        this.soundSource = config.getString("jump-settings.effects.sound.source", "entity.ender_dragon.flap");
        this.soundVolume = (float) config.getDouble("jump-settings.effects.sound.volume", 0.5D);
        this.soundPitch = (float) config.getDouble("jump-settings.effects.sound.pitch", 1.32D);

        // Jump VFX
        this.particleEffects = config.getBoolean("jump-settings.effects.particles.enabled", true);
        this.particleType = config.getString("jump-settings.effects.particles.type", "CLOUD").toUpperCase();
        this.particleCount = config.getInt("jump-settings.effects.particles.count", 40);
        this.particleSpread = config.getDouble("jump-settings.effects.particles.spread", 0.25D);
    }

    public void reload() {
        this.load();
    }

    public boolean shouldPreserveFallDamage() {
        return this.preserveFallDamage;
    }

    public boolean shouldAvoidFlyConflicts() {
        return this.avoidFlyConflicts;
    }

    public boolean shouldHardenGroundChecks() {
        return this.hardenedGroundCheck;
    }

    public boolean shouldPlaySoundEffects() {
        return this.soundEffects;
    }

    public boolean shouldSpawnParticleEffects() {
        return this.particleEffects;
    }

    public int getCooldownTicks() {
        return this.cooldownTicks;
    }

    public int getHungerDrain() {
        return this.hungerDrain;
    }

    public int getParticleCount() {
        return this.particleCount;
    }

    public double getHorizontalForceMultiplier() {
        return this.horizontalForceMultiplier;
    }

    public double getVerticalVelocity() {
        return this.verticalVelocity;
    }

    public double getParticleSpread() {
        return this.particleSpread;
    }

    public float getSoundVolume() {
        return this.soundVolume;
    }

    public float getSoundPitch() {
        return this.soundPitch;
    }

    public String getSoundSource() {
        return this.soundSource;
    }

    public String getParticleType() {
        return this.particleType;
    }

    public ActivationMethod getActivationMethod() {
        return this.activationMethod;
    }

}
