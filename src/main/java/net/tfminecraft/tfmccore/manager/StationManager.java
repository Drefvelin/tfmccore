package net.tfminecraft.tfmccore.manager;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.Plugins.TLibs.TLibs;
import net.tfminecraft.tfmccore.loader.StationLoader;

public class StationManager implements Listener{
    @EventHandler
	public void openStationEvent(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if(!(e.getAction().equals(Action.RIGHT_CLICK_BLOCK))) return;
		for(Map.Entry<String, String> entry : StationLoader.get().entrySet()) {
            if(TLibs.getBlockAPI().getChecker().checkBlock(e.getClickedBlock(), entry.getKey())) {
                openStation(p, entry.getValue());
                e.setCancelled(true);
            }
        }
	}
	
	private void openStation(Player p, String station) {
	    Bukkit.dispatchCommand(
            Bukkit.getConsoleSender(),
            "mi stations open " + station + " " + p.getName()
        );
	}
}
