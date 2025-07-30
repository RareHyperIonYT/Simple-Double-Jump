package me.rarehyperion.simpledoublejump;

import me.rarehyperion.simpledoublejump.commands.SDJCommand;
import me.rarehyperion.simpledoublejump.listeners.PlayerListener;
import me.rarehyperion.simpledoublejump.managers.ConfigManager;
import me.rarehyperion.simpledoublejump.managers.CooldownManager;
import me.rarehyperion.simpledoublejump.managers.DoubleJumpManager;
import me.rarehyperion.simpledoublejump.managers.LanguageManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SDJ extends JavaPlugin implements Listener {

    public static String VERSION = "UNKNOWN";

    private ConfigManager configManager;
    private LanguageManager languageManager;
    private CooldownManager cooldownManager;

    @Override
    public void onLoad() {
        VERSION = this.getDescription().getVersion();
    }

    @Override
    public void onEnable() {
        final PluginManager pm = this.getServer().getPluginManager();

        this.configManager = new ConfigManager(this);
        this.languageManager = new LanguageManager(this);
        this.cooldownManager = new CooldownManager(this.configManager);

        final DoubleJumpManager jumpManager = new DoubleJumpManager(this, this.configManager, this.languageManager, this.cooldownManager, pm);

        this.configManager.load();
        this.languageManager.load();

        pm.registerEvents(new PlayerListener(this.configManager, jumpManager), this);

        final SDJCommand executor = new SDJCommand(this, jumpManager, this.languageManager);
        final PluginCommand command = Objects.requireNonNull(this.getCommand("sdj"));

        command.setExecutor(executor);
        command.setTabCompleter(executor);
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
