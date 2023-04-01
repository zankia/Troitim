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
        registerCommand("list", executor, tabCompleter);
        registerCommand("setup", executor, tabCompleter);
        registerCommand("troitim", executor, tabCompleter);

        getLogger().info("Enabled");
    }

    private void registerCommand(String list, TCommand executor, TCommandTab tabCompleter) {
        PluginCommand command = getCommand(list);
        if (command == null) {
            return;
        }
        command.setExecutor(executor);
        command.setTabCompleter(tabCompleter);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled");
    }

}
