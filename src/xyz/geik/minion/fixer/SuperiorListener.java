package xyz.geik.minion.fixer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.bgsoftware.superiorskyblock.api.events.IslandTransferEvent;

public class SuperiorListener implements Listener {
	
	public SuperiorListener(Main instance) {}
	
	/**
	 * Superior owner change event listener
	 * 
	 * @param e
	 */
	@EventHandler
	public void ownerChangeEventSuperior(IslandTransferEvent e) {
		Main.instance.transferOwner(e.getOldOwner().getUniqueId().toString(), e.getNewOwner().getUniqueId().toString());
	}

}
