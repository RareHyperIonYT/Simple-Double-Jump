package me.rarehyperion.simpledoublejump;

import me.rarehyperion.simpledoublejump.listeners.PlayerListener;
import me.rarehyperion.simpledoublejump.managers.ConfigManager;
import me.rarehyperion.simpledoublejump.managers.DoubleJumpManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SDJ extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        final PluginManager pm = this.getServer().getPluginManager();
        final ConfigManager configManager = new ConfigManager(this);
        final DoubleJumpManager jumpManager = new DoubleJumpManager(this, configManager, pm);

        configManager.loadConfig();

        pm.registerEvents(new PlayerListener(configManager, jumpManager), this);
    }

    @Override
    public void onDisable() {

    }

}
