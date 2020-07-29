package curtain.tag.game.powerups;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import curtain.tag.TagPlugin;
import curtain.tag.game.GameManager;

//Handle the spawning and using of powerups
public class PowerupManager implements Listener
{
	
	private static ArrayList<Location> powerupLocs;
	
	public void Init()
	{
		powerupLocs = new ArrayList<Location>();
		
		//Init powerup listeners
		Bukkit.getPluginManager().registerEvents(this, TagPlugin.getPlugin(TagPlugin.class));
	}
	
	public void addPowerupLoc(Location loc)
	{
		powerupLocs.add(loc);
	}
	
	public ArrayList<Location> getPowerupLocList()
	{
		return powerupLocs;
	}
	
	public ItemStack getPowerupItemInt(int index)
	{
		return new FlyerPowerup().getItem();
	}
	
	
	//Powerup listeners
	@EventHandler
	public void onRightclick(PlayerInteractEvent event)
	{
		if(GameManager.gameActive)
		{
			if (event.getAction() == Action.PHYSICAL || event.getItem() == null || event.getItem().getType() == Material.STICK)
				return;
			if(event.getItem().getItemMeta().getDisplayName().equals("§7Flyer Powerup §e(Right Click)"))
			{
				new FlyerPowerup().onUse(event.getPlayer());
				event.getPlayer().sendMessage("boomer time");
			}
		}
	}
	
}
