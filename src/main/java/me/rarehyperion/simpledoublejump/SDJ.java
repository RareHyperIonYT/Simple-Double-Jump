package me.rarehyperion.simpledoublejump;

import me.rarehyperion.simpledoublejump.events.DoubleJumpEvent;
import me.rarehyperion.simpledoublejump.events.Reason;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SDJ extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        if(player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.SPECTATOR)
            return;

        if(player.isOnGround()) {
           player.setAllowFlight(true);
        }
    }

    @EventHandler
    public void onToggleFlight(final PlayerToggleFlightEvent event) {
        final Player player = event.getPlayer();
        final UUID uuid = player.getUniqueId();

        if(player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.SPECTATOR)
            return;

        final DoubleJumpEvent doubleJumpEvent = new DoubleJumpEvent(player, Reason.NORMAL, null);
        this.getServer().getPluginManager().callEvent(doubleJumpEvent);

        if(doubleJumpEvent.isCancelled())
            return;

        event.setCancelled(true);
        player.setAllowFlight(false);

        final Vector velocity = player.getVelocity();
        final Vector direction = player.getEyeLocation().getDirection().clone().normalize().multiply(0.5);
        player.setVelocity(velocity.add(direction).setY(0.42));
    }
}
