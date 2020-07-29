package curtain.tag.game.powerups;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import curtain.tag.TagPlugin;
import curtain.tag.game.GameManager;

public class PowerupLocationTask extends BukkitRunnable
{

	public void run()
	{
		if(GameManager.gameActive)
		{
			//FUCK YES LAMBDA
			Bukkit.getOnlinePlayers().forEach(((i) -> TagPlugin.getManager().getPowerupManager().addPowerupLoc(i.getLocation())));
		}
		else
		{
			cancel();
			TagPlugin.getManager().getPowerupManager().getPowerupLocList().clear();
		}
		
	}

	
	
}
