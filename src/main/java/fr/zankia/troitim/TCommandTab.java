package fr.zankia.troitim;

import com.booksaw.betterTeams.Team;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TCommandTab implements TabCompleter {
    private final Application plugin;
    public TCommandTab(Application plugin) {
        this.plugin = plugin;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!command.testPermission(sender)) {
            sender.sendMessage(ChatColor.RED + TCommand.NO_PERMISSION);
            return null;
        }
        return switch (command.getName()) {
            case "setup" -> getTeamsList(args);
            case "troitim" -> getCommandList(sender, args);
            default -> Collections.emptyList();
        };
    }

    @NotNull
    private List<String> getCommandList(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length < 2) {
            return List.of("list", "setup", "reload");
        }
        PluginCommand command = plugin.getCommand(args[0]);
        if (command == null) {
            return Collections.emptyList();
        }
        return command.tabComplete(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
    }

    private List<String> getTeamsList(@NotNull String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }

        return Team.getTeamList().values().stream()
                .map(Team::getName)
                .filter(s -> s.startsWith(args[0]))
                .collect(Collectors.toList());
    }

}
