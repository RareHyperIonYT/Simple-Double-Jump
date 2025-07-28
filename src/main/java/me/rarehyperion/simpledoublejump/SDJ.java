package me.rarehyperion.simpledoublejump;

import me.rarehyperion.simpledoublejump.listeners.PlayerListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SDJ extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        final PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {

    }

}
