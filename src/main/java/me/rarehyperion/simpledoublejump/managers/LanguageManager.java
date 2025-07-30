package me.rarehyperion.simpledoublejump.managers;

import me.rarehyperion.simpledoublejump.utils.MessageUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LanguageManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File file;

    public LanguageManager(final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        this.file = new File(this.plugin.getDataFolder(), "language.yml");

        if(!this.file.exists()) this.plugin.saveResource("language.yml", false);

        this.config = YamlConfiguration.loadConfiguration(this.file);

        final InputStream stream = this.plugin.getResource("language.yml");

        if(stream != null) {
            final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
            this.config.setDefaults(configuration);
        }
    }

    public void reload() {
        if(this.file == null) this.file = new File(this.plugin.getDataFolder(), "language.yml");
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    public String getReloadMessage() {
        return this.get("reload", "&aConfiguration successfully reloaded!");
    }

    public String getPermissionMessage() {
        return this.get("no-permission", "&cYou don't have permission to run this command.");
    }

    public String getCooldownMessage() {
        return this.get("cooldown", null);
    }

    public String get(final String key, final String def) {
        final String str = this.config.getString(key, def);
        if(str == null) return null;
        return MessageUtil.format(str);
    }

    public String getPlayerOnlyMessage() {
        return this.get("player-only", "&cYou cannot run this command via the console.");
    }

    public String getToggleOn() {
        return this.get("exempt-toggle.on", "&aDouble jumping enabled.");
    }

    public String getToggleOff() {
        return this.get("exempt-toggle.off", "&cDouble jumping disabled.");
    }

}
