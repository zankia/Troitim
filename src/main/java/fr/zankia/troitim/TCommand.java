package fr.zankia.troitim;

import com.booksaw.betterTeams.Team;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class TCommand implements CommandExecutor {
    public static final String PREFIX = "[" + ChatColor.GREEN + "Troitim" + ChatColor.RESET+ "] ";
    public static final String ERR_NO_TARGET_BLOCK = PREFIX + ChatColor.RED + "Erreur : Vous devez regarder un bloc";
    public static final String ERR_TEAM_NOT_FOUND = PREFIX + ChatColor.RED + "Erreur : Team non trouvée";
    public static final String NO_PERMISSION = PREFIX + ChatColor.RED + "Erreur : Vous n'avez pas la permission pour cette commande.";
    private final Application plugin;

    public TCommand(Application plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String name, String[] args) {
        if (!command.testPermission(sender)) {
            sender.sendMessage(NO_PERMISSION);
            return true;
        }
        return switch (command.getName()) {
            case "list" -> listCommand(sender);
            case "setup" -> setupCommand(sender, args);
            case "troitim" -> troitimCommand(sender, args);
            default -> false;
        };
    }


    private boolean listCommand(@NotNull CommandSender sender) {
        sender.sendMessage(PREFIX);
        ConfigurationSection config = plugin.getConfig().getConfigurationSection(Utils.BLOCKS);
        if (config == null) {
            return true;
        }
        config.getKeys(false).forEach(key -> sender.sendMessage(config.getString(key) + " -> " + key));
        return true;
    }

    private boolean setupCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length != 1) {
            return false;
        }

        Team team = Team.getTeam(args[0]);
        if (team == null) {
            sender.sendMessage(ERR_TEAM_NOT_FOUND);
            return true;
        }

        Player player = (Player) sender;
        Block targetBlock = player.getTargetBlock(null, 32);

        if (targetBlock.getType().isAir()) {
            sender.sendMessage(ERR_NO_TARGET_BLOCK);
            return true;
        }

        String targetFormat = Utils.getBlockLocationFormatted(targetBlock);
        plugin.getConfig().set(Utils.BLOCKS + "." + targetFormat, team.getName());

        plugin.saveConfig();
        sender.sendMessage(PREFIX + targetBlock.getType().name().toLowerCase() + "("
                + targetFormat.replaceAll("_", ", ") + ") enregistré pour l'équipe " + team.getName());

        return true;
    }

    private boolean troitimCommand(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length < 1) {
            return false;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            sender.sendMessage(PREFIX + ChatColor.GREEN + "Reload done.");
            return true;
        }
        PluginCommand command = plugin.getCommand(args[0]);
        if (command == null) {
            return false;
        }
        command.execute(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
        return true;
    }

}
