package xyz.geik.minion.fixer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.wasteofplastic.askyblock.events.IslandChangeOwnerEvent;

public class ASkyListener implements Listener {
	
	public ASkyListener(Main instance) {}
	
	/**
	 * Askyblock owner change event listener
	 * 
	 * @param e
	 */
	@EventHandler
	public void ownerChangeEventAsky(IslandChangeOwnerEvent e) {
		Main.instance.transferOwner(e.getOldOwner().toString(), e.getNewOwner().toString());
	}

}
