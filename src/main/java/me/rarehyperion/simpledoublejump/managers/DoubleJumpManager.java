package me.rarehyperion.simpledoublejump.managers;

import com.cryptomorin.xseries.XSound;
import com.cryptomorin.xseries.particles.XParticle;
import me.rarehyperion.simpledoublejump.api.events.DoubleJumpEvent;
import me.rarehyperion.simpledoublejump.api.events.JumpReason;
import me.rarehyperion.simpledoublejump.enums.ActivationMethod;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DoubleJumpManager {

    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final PluginManager pluginManager;
    private final CooldownManager cooldownManager;
    private final LanguageManager languageManager;
    private final Set<UUID> primed;

    public DoubleJumpManager(final JavaPlugin plugin, final ConfigManager configManager, final LanguageManager languageManager, final CooldownManager cooldownManager, final PluginManager pluginManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.pluginManager = pluginManager;
        this.cooldownManager = cooldownManager;
        this.languageManager = languageManager;
        this.primed = new HashSet<>();
    }

    public boolean canDoubleJump(final Player player) {
        final GameMode gameMode = player.getGameMode();

        if(gameMode != GameMode.SURVIVAL && gameMode != GameMode.ADVENTURE) {
            return false;
        }

        final ActivationMethod method = this.configManager.getActivationMethod();

        if (method == ActivationMethod.PERMISSION) {
            return player.hasPermission("sdj.use");
        }

        return true;
    }

    public boolean isPrimed(final Player player) {
        return this.primed.contains(player.getUniqueId());
    }

    public void prime(final Player player) {
        if(!this.canDoubleJump(player))
            return;

        final UUID uuid = player.getUniqueId();

        if(!this.primed.contains(uuid)) {
            this.primed.add(uuid);

            if(this.configManager.shouldPreserveFallDamage()) {
                // Delay allow flight by 1 tick so it's applied after fall damage.
                Bukkit.getScheduler().runTaskLater(this.plugin,
                        () -> player.setAllowFlight(true), 1L);
            } else {
                player.setAllowFlight(true);
            }
        }
    }

    public void unprime(final Player player) {
        final UUID uuid = player.getUniqueId();

        if(this.primed.contains(uuid)) {
            this.primed.remove(uuid);

            final GameMode gameMode = player.getGameMode();

            if(gameMode != GameMode.CREATIVE && gameMode != GameMode.SPECTATOR) {
                player.setAllowFlight(false);
            }
        }
    }

    public boolean performJump(final Player player, final JumpReason reason) {
        if(this.cooldownManager.isOnCooldown(player)) {
            final String cooldownMessage = this.languageManager.getCooldownMessage();
            if(cooldownMessage == null) return false;
            player.sendMessage(cooldownMessage
                    .replace("<cooldown>", String.format("%.1f", this.cooldownManager.getTimeLeft(player) / 1000.0F)));
            return false;
        }

        final DoubleJumpEvent doubleJumpEvent = new DoubleJumpEvent(player, reason, null);
        this.pluginManager.callEvent(doubleJumpEvent);

        if(doubleJumpEvent.isCancelled())
            return false;

        if(this.configManager.shouldPlaySoundEffects()) {
            final String soundSource = this.configManager.getSoundSource();
            final XSound.Record record = XSound.parse(soundSource);

            if(record != null) {
                record.withVolume(this.configManager.getSoundVolume());
                record.withPitch(this.configManager.getSoundPitch());

                final XSound.SoundPlayer sound = record.soundPlayer().forPlayers(player);
                sound.play();
            } else {
                this.plugin.getLogger().warning(String.format("The sound '%s' does not exist.", soundSource));
            }
        }

        if(this.configManager.shouldSpawnParticleEffects()) {
            final String particleType = this.configManager.getParticleType();
            final Particle particle = XParticle.valueOf(particleType).get();

            if(particle != null) {
                final int particleCount = this.configManager.getParticleCount();
                final double particleSpread = this.configManager.getParticleSpread();

                final Location spawnLocation = player.getLocation().clone().subtract(0.0D, 0.5D, 0.0D);
                player.spawnParticle(particle, spawnLocation, particleCount, particleSpread, particleSpread, particleSpread, 0.0D);
            } else {
                this.plugin.getLogger().warning(String.format("The particle '%s' does not exist.", particleType));
            }
        }

        this.applyForces(player);
        this.cooldownManager.cooldown(player);
        this.unprime(player);
        return true;
    }

    private void applyForces(final Player player) {
        final Vector velocity = player.getVelocity();
        final Vector direction = player.getEyeLocation().getDirection().clone().normalize();

        final double horizontalMultiplier = this.configManager.getHorizontalForceMultiplier();
        final double verticalVelocity = this.configManager.getVerticalVelocity();

        direction.multiply(horizontalMultiplier);
        velocity.add(direction).setY(verticalVelocity);

        player.setVelocity(velocity);

        final int hungerDrain = this.configManager.getHungerDrain();

        if(hungerDrain > 0) {
            final int currentLevel = player.getFoodLevel();
            player.setFoodLevel(Math.max(0, currentLevel - hungerDrain));
        }
    }

}
