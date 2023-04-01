package fr.zankia.troitim;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Application extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new TListener(this), this);

        var executor = new TCommand(this);
        var tabCompleter = new TCommandTab(this);
        PluginCommand listCommand = getCommand("list");
        listCommand.setExecutor(executor);
        listCommand.setTabCompleter(tabCompleter);
        PluginCommand setupCommand = getCommand("setup");
        setupCommand.setExecutor(executor);
        setupCommand.setTabCompleter(tabCompleter);
        PluginCommand troitimCommand = getCommand("troitim");
        troitimCommand.setExecutor(executor);
        troitimCommand.setTabCompleter(tabCompleter);

        getLogger().info("Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }

}
