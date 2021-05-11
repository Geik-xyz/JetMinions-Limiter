package xyz.geik.minion.fixer;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.songoda.skyblock.api.SkyBlockAPI;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import me.jet315.minions.events.PreMinionPlaceEvent;

public class Main
	extends JavaPlugin
		implements Listener, CommandExecutor {
	
	/**
	 * API Catcher
	 * 
	 * @author Geik
	 *
	 */
	private enum ApiType { Asky, Superior, Fabled }
	private ApiType API = ApiType.Asky;
	
	// Instance
	public static Main instance;
	
	// Variable
	int limit;
	int extendedLimit;
	
	/**
	 * Enable events {@link Listener & Config}
	 * 
	 */
	public void onEnable()
	{
		instance = this;
		
		saveDefaultConfig();
		
		limit = getConfig().getInt("limit");
		extendedLimit = getConfig().getInt("extendedLimit");
		
		if (Bukkit.getPluginManager().getPlugin("SuperiorSkyblock2") != null)
			API = ApiType.Superior;
		
		else if (Bukkit.getPluginManager().getPlugin("FabledSkyBlock") != null)
			API = ApiType.Fabled;
		
		Bukkit.getPluginManager().registerEvents(this, this);
		Main.instance.getCommand("miniongeik").setExecutor(this);
		
		if (API.equals(ApiType.Asky))
			Bukkit.getPluginManager().registerEvents(new ASkyListener(this), this);
		
		else if (API.equals(ApiType.Fabled))
			Bukkit.getPluginManager().registerEvents(new FabledListener(this), this);
		
		else
			Bukkit.getPluginManager().registerEvents(new SuperiorListener(this), this);
		
	}
	
	/**
	 * Color trnaslator
	 * 
	 * @param input
	 * @return
	 */
	public String color(String input) { return ChatColor.translateAlternateColorCodes('&', input); }
	
	
	/**
	 * <b> Catch minion place event </b>
	 * 
	 * @EventHandler
	 */
	@EventHandler(priority = EventPriority.HIGHEST)
	public void minionPlaceEvent(PreMinionPlaceEvent e)
	{
		
		try
		{
		
			UUID owner = getIslandOwner(e.getMinionLocation());
			if (getConfig().isSet("data." + owner.toString()))
			{
				
				final int count = getConfig().getInt("data." + owner.toString())+1;
				int placeLimit = limit;
				if (Bukkit.getOfflinePlayer(owner).isOnline()
						&& Bukkit.getPlayer(owner).hasPermission("geikminions.extended"))
					placeLimit = extendedLimit;
				
				if (count > placeLimit) {
					e.setCancelled(true);
					e.getPlayer().sendMessage( color(  getConfig().getString("onLimit")  ) );
					return;
				}
				
				getConfig().set("data." + owner.toString(), count);
				
			}
			
			else
				getConfig().set("data." + owner.toString(), 1);
			
			saveConfig();
			
		}
		
		catch (NullPointerException e1) {  e.setCancelled(true);  return;  }
		
	}
	
	
	/**
	 *  <b> 
	 *  
	 *  	UTILS
	 *  
	 *  </b>
	 */
	
	/**
	 * Owner UUID Catcher
	 * 
	 * @param location
	 * @return
	 */
	private final UUID getIslandOwner(Location location)
	{
		if (API.equals(ApiType.Asky))
			return ASkyBlockAPI.getInstance().getIslandAt(location).getOwner();
		
		else if (API.equals(ApiType.Fabled))
			return SkyBlockAPI.getIslandManager().getIslandAtLocation(location).getOwnerUUID();
		
		else
			return SuperiorSkyblockAPI.getIslandAt(location).getOwner().getUniqueId();
	}
	
	/**
	 * Transfer oldOwner minion variable to new one
	 * {@link ownerChangeEventAsky}
	 * {@link ownerChangeEventSuperior}
	 * 
	 * @param oldOwner
	 * @param newOwner
	 */
	void transferOwner(String oldOwner, String newOwner)
	{
		
		if (getConfig().isSet("data." + oldOwner))
		{
			getConfig().set("data." + newOwner, getConfig().getInt("data." + oldOwner));
			getConfig().set("data." + oldOwner, null);
			
			saveConfig();
		}
		
		else
			return;
		
	}
	
	/**
	 * Command Zone
	 * 
	 * @Commands
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		
		if (cmd.getName().equalsIgnoreCase("miniongeik"))
		{
			
			if (sender instanceof Player && sender.isOp())
			{
				
				try
				{
					
					Player player = (Player) sender;
					UUID owner = getIslandOwner(player.getLocation());
					
					getConfig().set("data." + owner.toString(), null);
					saveConfig();
					
					player.sendMessage(color(  "&aOyuncunun limitleri başarıyla kaldırıldı."  ));
					
					return true;
					
				}
				
				catch (NullPointerException e1) {  return false; }
				
			}
			
			else return false;
			
		}
		
		return false;
		
	}
	
}
