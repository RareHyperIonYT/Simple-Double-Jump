package me.rarehyperion.simpledoublejump.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class DoubleJumpEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private final JumpReason reason;
    private final ItemStack item;

    public DoubleJumpEvent(final Player player, final JumpReason reason, final ItemStack item) {
        super(player);
        this.reason = reason;
        this.item = item;
    }

    public JumpReason getReason() {
        return this.reason;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
