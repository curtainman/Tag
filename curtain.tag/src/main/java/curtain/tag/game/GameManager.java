package curtain.tag.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import curtain.tag.TagPlugin;
import curtain.tag.game.count.StartCountdown;
import curtain.tag.game.count.SubCountdown;
import curtain.tag.game.powerups.PowerupLocationTask;
import curtain.tag.game.powerups.PowerupManager;
import curtain.tag.game.powerups.PowerupSpawnTask;
import curtain.tag.utils.F;
import curtain.tag.utils.UtilArmor;

public class GameManager
{

	//TODO Convert most if not all of this to lambda statements cause I want to
	
	
	public static boolean gameActive;
	public static boolean gameBeginning = false;
	
	public void Init()
	{
		gameActive = false;
	}

	public PowerupManager getPowerupManager()
	{
		return new PowerupManager();
	}
	
	public void BeginGame()
	{
		Bukkit.broadcastMessage(F.f("Starting..."));
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TagPlugin.getPlugin(TagPlugin.class), new Runnable()
		{
			public void run()
			{
				// Teleport Players, Assign Teams
				assignTeams();
				playerBegin();
			}
		}, (2 * 20)); // Always multiply by twenty because that's the amount of ticks in Minecraft

	}

	public void StopGame(Player player)
	{
		Bukkit.broadcastMessage(ChatColor.YELLOW + F.f(player.getName() + " §7has stopped the tag game."));
		for (Player p : Bukkit.getOnlinePlayers())
		{
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
			p.getEquipment().clear();
		}
		GamePlayer.tagger = null;
		gameActive = false;
		this.getPowerupManager().getPowerupLocList().clear();
		Bukkit.getOnlinePlayers().forEach((p) -> {
			p.setPlayerListName(ChatColor.WHITE + p.getName());
		});
		
	}
	
	
	public void StopGame()
	{
//		Bukkit.broadcastMessage(F.f(player.getName() + " has stopped the tag game."));
		GamePlayer.tagger = null;
		gameActive = false;
		
		Bukkit.getOnlinePlayers().forEach((p) -> {
			p.setPlayerListName(ChatColor.WHITE + p.getName());
		});
		this.getPowerupManager().getPowerupLocList().clear();
		
		
		
	}
	
	public void StartGame(Player player)
	{
		Bukkit.getOnlinePlayers().forEach((p) -> {
			p.setPlayerListName(ChatColor.WHITE + p.getName());
		});
		this.getPowerupManager().getPowerupLocList().clear();
		Bukkit.broadcastMessage(ChatColor.YELLOW + F.f(player.getName() + "§7 has started a tag game."));
		for (Player p : Bukkit.getOnlinePlayers())
		{
			p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
		}
		//Run countdown
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TagPlugin.getPlugin(TagPlugin.class), new Runnable()
		{
			public void run()
			{
				StartCountdown _count = new StartCountdown();
				_count.runTaskTimer(TagPlugin.getPlugin(TagPlugin.class), 0L, 20L);
			}
		}, (2 * 20)); // Always multiply by twenty because that's the amount of ticks in Minecraft
		
	}

	private void playerBegin()
	{
		GamePlayer _gp = new GamePlayer();
		gameBeginning = true;
		Location taggerLoc = new Location(Bukkit.getWorld(TagPlugin.getPlugin(TagPlugin.class).getConfig().getString("taggerWorld")),
				TagPlugin.getPlugin(TagPlugin.class).getConfig().getInt("taggerX"),
				TagPlugin.getPlugin(TagPlugin.class).getConfig().getInt("taggerY"),
				TagPlugin.getPlugin(TagPlugin.class).getConfig().getInt("taggerZ"));
		Player tagger = Bukkit.getServer().getPlayer(GamePlayer.tagger.getName());
		tagger.teleport(taggerLoc);
		tagger.sendTitle("§c§lYou are the tagger", "§7Find players, and tag them", 4, 40, 4);
		tagger.getEquipment().setHelmet(UtilArmor.createLeather(Material.LEATHER_HELMET, "Leather Helmet", Color.RED));
		tagger.getEquipment().setChestplate(UtilArmor.createLeather(Material.LEATHER_CHESTPLATE, "Leather Chestplate", Color.RED));
		tagger.getEquipment().setLeggings(UtilArmor.createLeather(Material.LEATHER_LEGGINGS, "Leather Leggings", Color.RED));
		tagger.getEquipment().setBoots(UtilArmor.createLeather(Material.LEATHER_BOOTS, "Leather Boots", Color.RED));
		_gp.setTaggerFrozen(true);
		Location playerLoc = new Location(Bukkit.getWorld(TagPlugin.getPlugin(TagPlugin.class).getConfig().getString("playerWorld")),
				TagPlugin.getPlugin(TagPlugin.class).getConfig().getInt("playerX"),
				TagPlugin.getPlugin(TagPlugin.class).getConfig().getInt("playerY"),
				TagPlugin.getPlugin(TagPlugin.class).getConfig().getInt("playerZ"));
		for (Player p : Bukkit.getOnlinePlayers())
		{
			p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
			p.setGameMode(GameMode.SURVIVAL);
			if (p.getName() != tagger.getName())
			{
				p.teleport(playerLoc);
				p.getEquipment().setHelmet(UtilArmor.createLeather(Material.LEATHER_HELMET, "Leather Helmet", Color.WHITE));
				p.getEquipment().setChestplate(UtilArmor.createLeather(Material.LEATHER_CHESTPLATE, "Leather Chestplate", Color.WHITE));
				p.getEquipment().setLeggings(UtilArmor.createLeather(Material.LEATHER_LEGGINGS, "Leather Leggings", Color.WHITE));
				p.getEquipment().setBoots(UtilArmor.createLeather(Material.LEATHER_BOOTS, "Leather Boots", Color.WHITE));
				p.sendTitle("§a§lYou are not the tagger", "§7Collect powerups to escape the tagger", 4, 40, 4);
			}
		}
		// begin sub count, it will take over from there
		SubCountdown sub = new SubCountdown();
		sub.runTaskTimer(TagPlugin.getPlugin(TagPlugin.class), 0L, 20L);
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TagPlugin.getPlugin(TagPlugin.class), new Runnable()
		{
			public void run()
			{
				GamePlayer.tagger.sendTitle("§c§lYou are the tagger", "§7Be careful, they may have powerups", 4, 40, 4);
				for(Player p : Bukkit.getOnlinePlayers())
				{
					if(p.getName() != GamePlayer.tagger.getName())
					{
						p.sendTitle("§a§lYou are not the tagger", "§7Your glow will appear every 1 minute.", 4, 40, 4);
					}
					
				}
				
				Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TagPlugin.getPlugin(TagPlugin.class), new Runnable()
				{
					public void run()
					{
						GamePlayer.tagger.sendTitle("§c§lYou are the tagger", "§7Players appear every 1 minute. Good luck!", 4, 60, 4);
						for(Player p : Bukkit.getOnlinePlayers())
						{
							if(p.getName() != GamePlayer.tagger.getName())
							{
								p.sendTitle("§a§lYou are not the tagger", "§7Good luck!", 4, 60, 4);
							}

						}
					}
				}, (2 * 20)); // Always multiply by twenty because that's the amount of ticks in Minecraft
				
			}
		}, (2 * 20)); // Always multiply by twenty because that's the amount of ticks in Minecraft

	}

	public void gameStart()
	{
		
		//maybe add a truncate locs thing??
		
		new PowerupLocationTask().runTaskTimer(TagPlugin.getPlugin(TagPlugin.class), 3400L, 3400L); //2-3 minutes
		new PowerupSpawnTask().runTaskTimer(TagPlugin.getPlugin(TagPlugin.class), 7200L, 7200L); //6 min
		gameActive = true;
		GamePlayer _gp = new GamePlayer();
		_gp.setTaggerFrozen(false);
		PlayerRevealTask _prt = new PlayerRevealTask();
		_prt.runTaskTimer(TagPlugin.getPlugin(TagPlugin.class), 1200L, 1200L);
		gameBeginning = false;
		GamePlayer.tagger.setPlayerListName("§c" + GamePlayer.tagger.getName());

	}
	
	private void assignTeams()
	{
		List<String> players = new ArrayList<String>();
		for (Player loopie : Bukkit.getOnlinePlayers())
		{
			players.add(loopie.getName());
		}
		Random random = new Random();
		Player tagger = Bukkit.getServer().getPlayerExact(players.get(random.nextInt(players.size())));
		GamePlayer.tagger = tagger;
		System.out.println("Tagger: " + tagger.getName());
	}

	
	public void yourIt(Player tagger)
	{
		tagger.sendTitle("§c§lYou are the tagger", "§7Find players, and tag them", 4, 2, 4);
		tagger.getEquipment().setHelmet(UtilArmor.createLeather(Material.LEATHER_HELMET, "Leather Helmet", Color.RED));
		tagger.getEquipment().setChestplate(UtilArmor.createLeather(Material.LEATHER_CHESTPLATE, "Leather Chestplate", Color.RED));
		tagger.getEquipment().setLeggings(UtilArmor.createLeather(Material.LEATHER_LEGGINGS, "Leather Leggings", Color.RED));
		tagger.getEquipment().setBoots(UtilArmor.createLeather(Material.LEATHER_BOOTS, "Leather Boots", Color.RED));
		GamePlayer.tagger = tagger;
		Location taggerLoc = new Location(Bukkit.getWorld("world"),
				TagPlugin.getPlugin(TagPlugin.class).getConfig().getInt("taggerX"),
				TagPlugin.getPlugin(TagPlugin.class).getConfig().getInt("taggerY"),
				TagPlugin.getPlugin(TagPlugin.class).getConfig().getInt("taggerZ"));
		tagger.teleport(taggerLoc);
		tagger.setPlayerListName("§c" + tagger.getName());
	}
	
	public void notIt(Player player)
	{
		
	}
	
}
