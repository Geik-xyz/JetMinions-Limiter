package xyz.geik.minion.fixer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.songoda.skyblock.api.event.island.IslandOwnershipTransferEvent;

public class FabledListener implements Listener {
	
	public FabledListener(Main instance) {}
	
	/**
	 * Fabled owner change event listener
	 * 
	 * @param e
	 */
	@EventHandler
	public void islandLeaderChangeEvent(IslandOwnershipTransferEvent e) {
		Main.instance.transferOwner(e.getOwner().getUniqueId().toString(), e.getIsland().getOwnerUUID().toString());
	}

}
