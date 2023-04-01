package fr.zankia.troitim;

import com.booksaw.betterTeams.Team;
import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Arrays;
import java.util.Optional;

public class TListener implements Listener {
    private final Application plugin;

    public TListener(Application plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockDestroy(BlockBreakEvent e) {
        Optional<Team> blockTeam = getTeam(e.getBlock());

        if (blockTeam.isEmpty()) {
            return;
        }

        if (isSameTeam(blockTeam.get(), e.getPlayer())) {
            plugin.getLogger().info(e.getPlayer().getName() + " tried to destroy his own block");
            e.setCancelled(true);
            return;
        }

        plugin.getLogger().info(e.getBlock().getType().name() + " detroyed by " + e.getPlayer().getName() + " at " + e.getBlock().getLocation());

        banAll(blockTeam.get());

        plugin.getConfig().set(Utils.BLOCKS + "." + Utils.getBlockLocationFormatted(e.getBlock()), null);
        plugin.saveConfig();

    }

    private void banAll(Team team) {
        Arrays.stream(plugin.getServer().getOfflinePlayers()).forEach(player -> {
            Team playerTeam = Team.getTeam(player);
            if (team.equals(playerTeam)) {
                plugin.getLogger().info("Banning " + player.getName());
                player.banPlayer("Tu as perdu.");
                if (player.isOnline()) {
                    ((Player) player).kick(Component.text("Tu as perdu."));
                }
            }
        });
    }

    private boolean isSameTeam(Team team, Player player) {
        Team playerTeam = Team.getTeam(player);
        return playerTeam != null && playerTeam.equals(team);
    }

    private Optional<Team> getTeam(Block block) {
        String blockConfig = plugin.getConfig().getString(Utils.BLOCKS + "." + Utils.getBlockLocationFormatted(block));
        if (blockConfig == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(Team.getTeam(blockConfig));
    }
}
