package fr.zankia.troitim;

import org.bukkit.block.Block;

public class Utils {
    public static final String BLOCKS = "blocks";

    public static String getBlockLocationFormatted(Block targetBlock) {
        return targetBlock.getLocation().getBlockX() + "_" + targetBlock.getLocation().getBlockY() + "_"
                + targetBlock.getLocation().getBlockZ();
    }

}
