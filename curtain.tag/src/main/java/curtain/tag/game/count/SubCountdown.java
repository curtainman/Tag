package curtain.tag.game.count;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import curtain.tag.TagPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class SubCountdown extends BukkitRunnable
{

	private int countdown = 6;
	
	public void run()
	{
		countdown--;
		if(countdown > 0)
		{
			Bukkit.getOnlinePlayers().forEach((p) -> {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§aGame Start: §e" + countdown + " Seconds"));
			});
		//	for(Player p : Bukkit.getOnlinePlayers())
		//	{
		//		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
		//		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§aGame Start: §e" + countdown + " Seconds"));
		//	}
		}
		if(countdown == 0)
		{
			
			Bukkit.getOnlinePlayers().forEach((p) -> {
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§eBegin!"));
			});
			
			//for(Player p : Bukkit.getOnlinePlayers())
		//	{
		//		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2);
		//		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§eBegin!"));
		//	}
			this.cancel();
			TagPlugin.getManager().gameStart();
		}
		
	}

	
	
}
