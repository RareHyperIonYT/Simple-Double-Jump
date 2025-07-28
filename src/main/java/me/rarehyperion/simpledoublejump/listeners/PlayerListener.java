package me.rarehyperion.simpledoublejump.listeners;

import me.rarehyperion.simpledoublejump.SDJ;
import me.rarehyperion.simpledoublejump.events.DoubleJumpEvent;
import me.rarehyperion.simpledoublejump.events.Reason;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class PlayerListener implements Listener {

    private final SDJ plugin;
    private final Set<UUID> primed = new HashSet<>();

    public PlayerListener(final SDJ plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPreCommand(final PlayerCommandPreprocessEvent event) {
        final String message = event.getMessage().toLowerCase();

        if(!message.startsWith("/fly")) {
            return;
        }

        final String[] args = message.split(" ");

        if(args.length == 1) {
            final Player player = event.getPlayer();
            final UUID uuid = player.getUniqueId();

            if(!this.primed.contains(uuid))
                return;

            player.setAllowFlight(false);
            this.primed.remove(uuid);
        } else if(args.length == 2) {
            final String targetName = args[1];
            final Player player = Bukkit.getPlayer(targetName);

            if(player == null) {
                return;
            }

            final UUID uuid = player.getUniqueId();

            if(!this.primed.contains(uuid)) {
                return;
            }

            player.setAllowFlight(false);
            this.primed.remove(uuid);
        }
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        if(player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.SPECTATOR)
            return;

        if(player.isOnGround()) {
            if(player.getAllowFlight())
                return;

            Bukkit.getScheduler().runTaskLater(this.plugin, () -> player.setAllowFlight(true), 1L);
            this.primed.add(uuid);
        } else if(this.primed.contains(uuid) && player.getVelocity().getY() < 0) {
            player.setAllowFlight(false);
            this.primed.remove(uuid);
        }
    }

    @EventHandler
    public void onToggleFlight(final PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        if(player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.SPECTATOR)
            return;

        if(!this.primed.contains(uuid))
            return;

        this.primed.remove(uuid);

        final DoubleJumpEvent doubleJumpEvent = new DoubleJumpEvent(player, Reason.NORMAL, null);
        this.plugin.getServer().getPluginManager().callEvent(doubleJumpEvent);

        if(doubleJumpEvent.isCancelled())
            return;

        event.setCancelled(true);
        player.setAllowFlight(false);

        final Vector velocity = player.getVelocity();
        final Vector direction = player.getEyeLocation().getDirection().clone().normalize().multiply(0.5);
        player.setVelocity(velocity.add(direction).setY(0.42));
    }

}
