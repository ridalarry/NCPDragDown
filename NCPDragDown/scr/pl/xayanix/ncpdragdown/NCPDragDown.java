package pl.xayanix.ncpdragdown;

import org.bukkit.plugin.java.JavaPlugin;

public class NCPDragDown extends JavaPlugin
{
    private static NCPDragDown instance;
    
    public void onEnable() {
        NCPDragDown.instance = this;
        new NCPDragDownHook();
    }
    
    public void onDisable() {
    }
    
    public static NCPDragDown getInstance() {
        return NCPDragDown.instance;
    }
}
