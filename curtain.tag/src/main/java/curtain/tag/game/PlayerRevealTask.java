package curtain.tag.game;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import curtain.tag.TagPlugin;
import curtain.tag.utils.F;

public class PlayerRevealTask extends BukkitRunnable
{

	public void run()
	{
		if(GameManager.gameActive)
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "effect give @a minecraft:glowing 10 255 true");
			Bukkit.broadcastMessage(F.f("Players have been revealed!"));
			Bukkit.getOnlinePlayers().forEach((p) -> {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, (float) 1.6);
			});
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TagPlugin.getPlugin(TagPlugin.class), new Runnable()
			{
				public void run()
				{
					for(Player p : Bukkit.getOnlinePlayers())
					{
						p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, (float) 1.6);
				//		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "minecraft:effect " + p.getName() + " minecraft:glowing 10 255 true");
					}
					Bukkit.broadcastMessage(F.f("Players hidden."));
				}
			}, (10 * 20));
		}
		else
		{
			Bukkit.getLogger().log(Level.INFO, "Show player task for Tag canceled.");
			cancel();
		}
		
	}

	
	
}
