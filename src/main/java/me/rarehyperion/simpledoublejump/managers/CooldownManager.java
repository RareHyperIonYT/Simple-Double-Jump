package me.rarehyperion.simpledoublejump.managers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final ConfigManager configManager;
    private final Map<UUID, Long> cooldowns;

    public CooldownManager(final ConfigManager configManager) {
        this.configManager = configManager;
        this.cooldowns = new HashMap<>();
    }

    public boolean isOnCooldown(final Player player) {
        final int cooldownTicks = this.configManager.getCooldownTicks();
        if(cooldownTicks <= 0) return false;

        final UUID uuid = player.getUniqueId();

        if(!this.cooldowns.containsKey(uuid)) {
            return false;
        }

        final long lastJumpTime = this.cooldowns.get(uuid);
        final long currentTime = System.currentTimeMillis();
        final long cooldownMillis = cooldownTicks * 50L;

        return (currentTime - lastJumpTime) < cooldownMillis;
    }

    public void cooldown(final Player player) {
        final int cooldownTicks = this.configManager.getCooldownTicks();

        if(cooldownTicks > 0) {
            this.cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    public void remove(final Player player) {
        this.cooldowns.remove(player.getUniqueId());
    }

    public void clear() {
        this.cooldowns.clear();
    }

    public long getTimeLeft(final Player player) {
        final int cooldownTicks = this.configManager.getCooldownTicks();
        if(cooldownTicks <= 0) return 0;

        final UUID uuid = player.getUniqueId();

        if(!this.cooldowns.containsKey(uuid)) {
            return 0;
        }

        final long lastJumpTime = this.cooldowns.get(uuid);
        final long currentTime = System.currentTimeMillis();
        final long cooldownMillis = cooldownTicks * 50L;
        final long elapsed = currentTime - lastJumpTime;

        return Math.max(0, cooldownMillis - elapsed);
    }

}
