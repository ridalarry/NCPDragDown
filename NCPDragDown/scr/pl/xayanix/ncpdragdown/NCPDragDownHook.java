package pl.xayanix.ncpdragdown;

import fr.neatmonster.nocheatplus.hooks.NCPHookManager;
import fr.neatmonster.nocheatplus.logging.LogManager;
import fr.neatmonster.nocheatplus.logging.Streams;
import fr.neatmonster.nocheatplus.players.DataManager;
import fr.neatmonster.nocheatplus.players.IPlayerData;

import org.bukkit.block.Block;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import fr.neatmonster.nocheatplus.checks.access.IViolationInfo;
import fr.neatmonster.nocheatplus.checks.moving.MovingData;

import org.bukkit.entity.Player;

import fr.neatmonster.nocheatplus.NCPAPIProvider;
import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPHook;

public class NCPDragDownHook implements NCPHook
{
	//private IGenericInstanceHandle<MCAccess> mcAccess = null;
	private final CheckType type[] = {CheckType.MOVING_SURVIVALFLY, CheckType.MOVING_PASSABLE};
	public NCPDragDownHook() {
        hook();
        //mcAccess = NCPAPIProvider.getNoCheatPlusAPI().getGenericInstanceHandle(MCAccess.class);
    }
    
    public boolean onCheckFailure(final CheckType checkType, final Player p, final IViolationInfo vlInfo) {
        return !p.hasPermission("ncpdd.bypass") && vlInfo.willCancel() && dragDown(p, checkType);
    }
    
    private boolean dragDown(final Player p, final CheckType checkType) {
        if (p.getLocation().getY() <= 2.0) {
            return false;
        }
        double MaxY = -1.0;
        float FallDist = -1.0f;
        boolean debug = p.hasPermission("ncpdd.debug");
        final Location ploc = p.getLocation();
        final Block bDown = p.getLocation().getBlock().getRelative(BlockFace.DOWN);
        final IPlayerData pData = DataManager.getPlayerData(p);
        if (pData != null) {
        	final MovingData mData = pData.getGenericInstance(MovingData.class);
            MaxY = mData.noFallMaxY;
            FallDist = mData.noFallFallDistance;
        }
        if (PlayerUtil.isOnGround(p, 1.0) || PlayerUtil.isOnGround(p, 0.0)) {
        	if (pData != null) {
        	    final MovingData mData = pData.getGenericInstance(MovingData.class);
        	    if (mData.hasSetBack()) if (mData.getSetBackY() >= p.getLocation().getY()) {
                    mData.noFallMaxY = MaxY;
            	    mData.noFallFallDistance = FallDist;
            	    mData.setTeleported(p.getLocation());
            	    if (debug) {
            	        log("Set position to current location!", p);
            	    }
            	    return false;
        	    }
        	}
        	return false;
        }
        ploc.setX(p.getLocation().getX());
        boolean is2 = false;
        if (PlayerUtil.isAir(bDown.getType())) {
        	final Block bDown2 = bDown.getRelative(BlockFace.DOWN);
        	if (PlayerUtil.isAir(bDown2.getType())) {
        		is2 = true;
        		ploc.setY((double)bDown2.getLocation().getBlockY());
        	} else ploc.setY((double)bDown.getLocation().getBlockY());
        }
        ploc.setZ(p.getLocation().getZ());
        ploc.setPitch(p.getLocation().getPitch());
        ploc.setYaw(p.getLocation().getYaw());
        if (MaxY != -1.0 && FallDist != -1.0) {
        	final MovingData mData = pData.getGenericInstance(MovingData.class);
        	mData.noFallMaxY = MaxY;
        	mData.setTeleported(ploc);
        	mData.noFallFallDistance = FallDist + (is2 ? 2.0f : 1.0f);
        }
        if (debug) {
            log("Set position to " + (is2 ? "2": "1") + " block(s) lower!", p);
	    }
        return false;
    }
    
    public void hook() {
        NCPHookManager.addHook(type, (NCPHook)this);
    }
    
    public String getHookName() {
        return "NCPDragDown";
    }
    
    public String getHookVersion() {
        return "1.1";
    }
    
    private void log(String s, Player p) {
    	final LogManager logManager = NCPAPIProvider.getNoCheatPlusAPI().getLogManager();
    	final StringBuilder builder = new StringBuilder(300);
    	builder.append(getHookName());
    	builder.append(" [" + ChatColor.YELLOW + p.getName());
        builder.append(ChatColor.WHITE + "] ");
    	builder.append(s);
    	final String message = builder.toString();
    	logManager.info(Streams.NOTIFY_INGAME, message);
    }
}
