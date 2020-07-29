package curtain.tag.game.count;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import curtain.tag.TagPlugin;

public class StartCountdown extends BukkitRunnable
{

	public int countdown = 6;
	
	public void run()
	{
		countdown--;
		if(countdown <= 6)
		{
			Bukkit.getOnlinePlayers().forEach((p) -> {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
				p.sendTitle("", "§7Starting in §e" + countdown, 4, 20, 4);
			});
		}
		if(countdown == 1)
		{
			this.cancel();
			TagPlugin.getManager().BeginGame();
		}

	}

}
