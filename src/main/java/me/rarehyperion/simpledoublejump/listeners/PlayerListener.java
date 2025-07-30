package me.rarehyperion.simpledoublejump.listeners;

import me.rarehyperion.simpledoublejump.api.events.JumpReason;
import me.rarehyperion.simpledoublejump.managers.ConfigManager;
import me.rarehyperion.simpledoublejump.managers.DoubleJumpManager;
import me.rarehyperion.simpledoublejump.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class PlayerListener implements Listener {

    private final ConfigManager configManager;
    private final DoubleJumpManager jumpManager;

    public PlayerListener(final ConfigManager configManager, final DoubleJumpManager jumpManager) {
        this.configManager = configManager;
        this.jumpManager = jumpManager;
    }

    @EventHandler
    public void onPreCommand(final PlayerCommandPreprocessEvent event) {
        if(!this.configManager.shouldAvoidFlyConflicts())
            return;

        final String message = event.getMessage().toLowerCase();

        if(!message.startsWith("/fly")) {
            return;
        }

        final String[] args = message.split(" ");

        if(args.length == 1) {
            final Player player = event.getPlayer();

            if(!this.jumpManager.isPrimed(player)) {
                return;
            }

            this.jumpManager.unprime(player);
        } else if(args.length == 2) {
            final String targetName = args[1];
            final Player player = Bukkit.getPlayer(targetName);

            if(player == null) {
                return;
            }

            if(!this.jumpManager.isPrimed(player)) {
                return;
            }

            this.jumpManager.unprime(player);
        }
    }

    @EventHandler
    @SuppressWarnings("deprecation")
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();

        if(this.jumpManager.getExemptPlayers().contains(player.getUniqueId()))
            return;

        final boolean onGround = this.configManager.shouldHardenGroundChecks()
                ? LocationUtil.onGround(player.getLocation())
                : player.isOnGround();

        // Prevent the player from sprinting if they are hungry.
        // This is needed because allowFlight allows sprinting during hunger.
        if(player.getFoodLevel() < 6)
            return;

        if(onGround) {
            if(player.getAllowFlight())
                return;

            this.jumpManager.prime(player);
        } else if(player.getVelocity().getY() < 0) {
            this.jumpManager.unprime(player);
        }
    }

    @EventHandler
    public void onToggleFlight(final PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();

        if(!this.jumpManager.isPrimed(player) || !this.jumpManager.canDoubleJump(player))
            return;

        this.jumpManager.performJump(player, JumpReason.NORMAL);
        event.setCancelled(true);
    }

}
