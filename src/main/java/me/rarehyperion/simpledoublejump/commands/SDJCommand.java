package me.rarehyperion.simpledoublejump.commands;

import me.rarehyperion.simpledoublejump.SDJ;
import me.rarehyperion.simpledoublejump.managers.LanguageManager;
import me.rarehyperion.simpledoublejump.utils.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("NullableProblems")
public class SDJCommand implements CommandExecutor, TabCompleter {

    private final SDJ plugin;
    private final LanguageManager languageManager;

    public SDJCommand(final SDJ plugin, final LanguageManager languageManager) {
        this.plugin = plugin;
        this.languageManager = languageManager;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command cmd, final String alias, final String[] args) {
        if(args.length == 1) {
            final String subCommand = args[0].toLowerCase();

            if (subCommand.equals("reload")) {
                if (!sender.hasPermission("sdj.admin")) {
                    sender.sendMessage(this.languageManager.getPermissionMessage());
                    return true;
                }

                this.plugin.reload();
                sender.sendMessage(this.languageManager.getReloadMessage());
            }
        } else {
            sender.sendMessage(MessageUtil.format(
                    String.format("&d&lSimple Double Jump &7(%s)", SDJ.VERSION)
            ));

            sender.sendMessage(MessageUtil.format(
                    "&8❘ &7Adds a fully customizable double‑jump mechanic to your game."
            ));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
        if(args.length == 1) {
            return Stream.of("reload")
                    .filter(option ->option.startsWith(args[0].toLowerCase()))
                    .toList();
        }

        return List.of();
    }
}
