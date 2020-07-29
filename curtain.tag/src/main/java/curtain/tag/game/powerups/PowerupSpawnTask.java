package curtain.tag.game.powerups;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import curtain.tag.TagPlugin;
import curtain.tag.game.GameManager;

public class PowerupSpawnTask extends BukkitRunnable
{

	public void run()
	{
		if(GameManager.gameActive)
		{
			//TODO change this to lambda haha fuck java 7
			for(Location l : TagPlugin.getManager().getPowerupManager().getPowerupLocList())
			{
				//Random to determine which type of powerup will spawn at the loc
		// coming soon to a theatre near you		Random random = new Random();
				World locWorld = Bukkit.getWorld(l.getWorld().getName());
				//0 cause it doesn't matter yet
				locWorld.dropItem(l, TagPlugin.getManager().getPowerupManager().getPowerupItemInt(0));
			}
			Bukkit.broadcastMessage("§a§lPowerups have spawned!");
			Bukkit.getOnlinePlayers().forEach((p) -> {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
			});
		}
		else
		{
			cancel();
		}
	}

	
	
}
