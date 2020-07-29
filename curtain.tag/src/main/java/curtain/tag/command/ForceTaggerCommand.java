package curtain.tag.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import curtain.tag.TagPlugin;
import curtain.tag.game.GameManager;
import curtain.tag.utils.F;

public class ForceTaggerCommand implements CommandExecutor
{

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			if(player.hasPermission("tag.admin"))
			{
				switch(args.length)
				{
					case (0):
						F.player(player, "Usage: /forceit (player)");
						return false;
					case(1):
						if(GameManager.gameActive)
						{
							TagPlugin.getManager().yourIt(Bukkit.getPlayer(args[0]));
							return true;
						}
						else
						{
							F.player(player, "Cannot do this now.");
							return false;
						}
				}
			}
			else
			{
				F.f(TagPlugin.getPlugin(TagPlugin.class).getConfig().getString("permissionMessage"));
			}
		}
		return false;
	}

	
	
}
