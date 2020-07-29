package curtain.tag.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import curtain.tag.TagPlugin;
import curtain.tag.game.GameManager;
import curtain.tag.utils.F;

public class TagCommand implements CommandExecutor
{

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
	{
		if(sender instanceof Player)
		{
			Player player = (Player) sender;
			if(!player.hasPermission("tag.admin"))
			{
				F.player(player, TagPlugin.getPlugin(TagPlugin.class).getConfig().getString("permissionMessage"));
				return false;
			}
			
			switch(args.length)
			{
				case (0):
					F.player(player, "Tag Commands:");
					player.sendMessage("§e/tag start §7Starts the tag game.");
					player.sendMessage("§e/tag stop §7Stops the the tag game.");
					player.sendMessage("§e/tag position (player, tagger) §7Sets the spawnpoint position for either players or taggers");
					return false;
				case (1):
					if(args[0].equals("start") || args[0].equals("stop"))
					{
						if(args[0].equals("start"))
						{
							if(Bukkit.getOnlinePlayers().size() < 2)
							{
								F.player(player, "Unable to begin game, not enough players!");
								return false;
							}
							if(GameManager.gameActive)
							{
								F.player(player, "Game is currently active, cannot start!");
								return false;
							}
							if(!TagPlugin.configOK)
							{
								F.player(player, "Config not validated. Please set positions before starting the game!");
								return false;
							}
							TagPlugin.getManager().StartGame(player);
							return true;
						}
						else
						{
							if(!GameManager.gameActive)
							{
								F.player(player, "Cannot stop game now.");
								return false;
							}
							else
							{
								TagPlugin.getManager().StopGame(player);
								return true;
							}
						}
					}
				case (2):
					if(args[1].equals("player") || args[1].equals("tagger"))
					{
						if(args[1].equals("player"))
						{
							F.player(player, "Successfully set the player spawnpoint to your location.");
							TagPlugin.getPlugin(TagPlugin.class).getConfig().set("playerX", player.getLocation().getX());
							TagPlugin.getPlugin(TagPlugin.class).getConfig().set("playerY", player.getLocation().getY());
							TagPlugin.getPlugin(TagPlugin.class).getConfig().set("playerZ", player.getLocation().getZ());
							TagPlugin.getPlugin(TagPlugin.class).getConfig().set("playerWorld", player.getLocation().getWorld().getName());
							TagPlugin.getPlugin(TagPlugin.class).saveConfig();
							return true;
						}
						if(args[1].equals("tagger"))
						{
							F.player(player, "Successfully set the tagger spawnpoint to your location.");
							TagPlugin.getPlugin(TagPlugin.class).getConfig().set("taggerX", player.getLocation().getX());
							TagPlugin.getPlugin(TagPlugin.class).getConfig().set("taggerY", player.getLocation().getY());
							TagPlugin.getPlugin(TagPlugin.class).getConfig().set("taggerZ", player.getLocation().getZ());
							TagPlugin.getPlugin(TagPlugin.class).getConfig().set("taggerWorld", player.getLocation().getWorld().getName());
							TagPlugin.getPlugin(TagPlugin.class).saveConfig();
							return true;
						}
					}
					else
					{
						F.player(player, "Invalid Type of Position: Must be §eplayer or tagger");
						return false;
					}
			}
			return false;
		}
		else
		{
			System.out.println("Command must be used by player");
			return false;
		}
	}

	
	
}
