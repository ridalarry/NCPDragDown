package pl.xayanix.ncpdragdown;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import fr.neatmonster.nocheatplus.utilities.map.BlockProperties;

public class PlayerUtil
{
    public static boolean isOnGround(final Player player, final double dec) {
        final Material blockmat = player.getLocation().add(0.0, -dec, 0.0).getBlock().getType();
        return !isAir(blockmat);
    }
    public static boolean isAir(final Material blocktype) {
        return !BlockProperties.isLiquid(blocktype) && !BlockProperties.isNewLiq(blocktype) && (BlockProperties.isPassable(blocktype) || BlockProperties.isAir(blocktype));
    }
}
