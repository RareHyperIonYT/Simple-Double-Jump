package me.rarehyperion.simpledoublejump;

import me.rarehyperion.simpledoublejump.listeners.PlayerListener;
import me.rarehyperion.simpledoublejump.managers.ConfigManager;
import me.rarehyperion.simpledoublejump.managers.CooldownManager;
import me.rarehyperion.simpledoublejump.managers.DoubleJumpManager;
import me.rarehyperion.simpledoublejump.managers.LanguageManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SDJ extends JavaPlugin implements Listener {

    private ConfigManager configManager;
    private LanguageManager languageManager;
    private CooldownManager cooldownManager;
    private DoubleJumpManager jumpManager;

    @Override
    public void onEnable() {
        final PluginManager pm = this.getServer().getPluginManager();

        this.configManager = new ConfigManager(this);
        this.languageManager = new LanguageManager(this);
        this.cooldownManager = new CooldownManager(this.configManager);
        this.jumpManager = new DoubleJumpManager(this, this.configManager, this.languageManager, this.cooldownManager, pm);

        this.configManager.load();
        this.languageManager.load();

        pm.registerEvents(new PlayerListener(this.configManager, this.jumpManager), this);
    }

    @Override
    public void onDisable() {
        if(this.cooldownManager != null) {
            this.cooldownManager.clear();
        }
    }

    public void reload() {
        this.configManager.reload();
        this.languageManager.reload();
    }

}
