package curtain.tag.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import curtain.tag.TagPlugin;
import curtain.tag.utils.F;
import curtain.tag.utils.UtilArmor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class GamePlayer implements Listener
{

	public static Player tagger;
	private static boolean taggerFrozen;
	private static boolean tagCooldown;
	public Player lastTagger;

	public void setTagger(Player p)
	{
		tagger = p;
	}

	public void setTaggerFrozen(Boolean adsf)
	{
		taggerFrozen = adsf;
	}

	public boolean getTaggerFrozen()
	{
		return taggerFrozen;
	}

	// Main tag handler
	@EventHandler
	public void onDamageByEntity(EntityDamageByEntityEvent event)
	{
		if (event.getDamager() instanceof Player)
		{
			if (event.getEntity() instanceof Player)
			{
				if (GameManager.gameActive)
				{
					Player tagger = (Player) event.getDamager();
					Player player = (Player) event.getEntity();
					if (tagger.getName().equals(GamePlayer.tagger.getName()))
					{
						if (tagCooldown == false)
						{
							tagger.setPlayerListName(tagger.getName());
							setTagger(player);
							lastTagger = tagger;
							for (Player p : Bukkit.getOnlinePlayers())
							{
								p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
							}
							player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
									TextComponent.fromLegacyText("§c§lTag another player!"));
							Player guy = (Player) event.getDamager();
							guy.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§a§lRun!"));
							Bukkit.broadcastMessage(
									F.f("Start running from §e" + player.getName() + ", §7they are now it!"));
							tagger.getEquipment().setHelmet(
									UtilArmor.createLeather(Material.LEATHER_HELMET, "Leather Helmet", Color.WHITE));
							tagger.getEquipment().setChestplate(UtilArmor.createLeather(Material.LEATHER_CHESTPLATE,
									"Leather Chestplate", Color.WHITE));
							tagger.getEquipment().setLeggings(UtilArmor.createLeather(Material.LEATHER_LEGGINGS,
									"Leather Leggings", Color.WHITE));
							tagger.getEquipment().setBoots(
									UtilArmor.createLeather(Material.LEATHER_BOOTS, "Leather Boots", Color.WHITE));
							player.getEquipment().setHelmet(
									UtilArmor.createLeather(Material.LEATHER_HELMET, "Leather Helmet", Color.RED));
							player.getEquipment().setChestplate(UtilArmor.createLeather(Material.LEATHER_CHESTPLATE,
									"Leather Chestplate", Color.RED));
							player.getEquipment().setLeggings(
									UtilArmor.createLeather(Material.LEATHER_LEGGINGS, "Leather Leggings", Color.RED));
							player.getEquipment().setBoots(
									UtilArmor.createLeather(Material.LEATHER_BOOTS, "Leather Boots", Color.RED));
							tagCooldown = true;
							player.setPlayerListName("§c"+ player.getName());
							Bukkit.getServer().getScheduler()
									.scheduleSyncDelayedTask(TagPlugin.getPlugin(TagPlugin.class), new Runnable()
									{
										public void run()
										{
											tagCooldown = false;
										}
									}, (5 * 20)); // Always multiply by twenty because that's the amount of ticks in
													// Minecraft
						} else
						{
							F.player(tagger, "Please wait the no tag back cooldown.");
							event.setCancelled(true);
						}
					}
					else
					{
						event.setCancelled(true);
					}
				}
			}
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event)
	{
		if(GameManager.gameActive)
		{
			if (event.getEntity() instanceof Player)
			{
				if (event.getCause().equals(DamageCause.FALL))
				{
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{

		TagPlugin.getManager();
		if (GameManager.gameActive)
		{
			Player player = event.getPlayer();
			player.getEquipment()
					.setHelmet(UtilArmor.createLeather(Material.LEATHER_HELMET, "Leather Helmet", Color.WHITE));
			player.getEquipment().setChestplate(
					UtilArmor.createLeather(Material.LEATHER_CHESTPLATE, "Leather Chestplate", Color.WHITE));
			player.getEquipment()
					.setLeggings(UtilArmor.createLeather(Material.LEATHER_LEGGINGS, "Leather Leggings", Color.WHITE));
			player.getEquipment().setBoots(UtilArmor.createLeather(Material.LEATHER_BOOTS, "Leather Boots", Color.WHITE));
			F.player(player, "Run away from " + GamePlayer.tagger.getName() + ", they are the tagger!");
		}
	}
	
	//Rage quit handler
	@EventHandler
	public void onQuit(PlayerQuitEvent event)
	{
		TagPlugin.getManager();
		if(GameManager.gameActive == true)
		{
			//Check if only 1 person is left, if so stop else assign new tagger
			if(Bukkit.getOnlinePlayers().size() == 1)
			{
				//Stop
				TagPlugin.getManager().StopGame();
				Bukkit.broadcastMessage(F.f("Gameplay stopped due to no players."));
			}
			else
			{
				if(event.getPlayer().getName().equals(GamePlayer.tagger.getName()))
				{
					List<Player> oofer = new ArrayList<Player>();
					for(Player p : Bukkit.getOnlinePlayers())
					{
						oofer.add(p);
					}
					Player newTagger = oofer.get(new Random().nextInt(oofer.size()));
					TagPlugin.getManager().yourIt(newTagger);
					Bukkit.broadcastMessage(F.f("The player who was it has logged out. §a" + newTagger.getName() + "§7 is now the new tagger!"));
				}
			}
		}
	}
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent event)
	{
		final Location to = event.getTo();
		final Location from = event.getFrom();
		if(to.getX() != from.getX() || to.getZ() != from.getZ())
		{
			if(GameManager.gameBeginning)
			{
				if(event.getPlayer().getName().equals(GamePlayer.tagger.getName()))
				{
					if(this.getTaggerFrozen() == true)
					{
						event.setCancelled(true);
					}
				}
			}

		}
	}
	
	
	//Basic needs handlers
	
	@EventHandler
	public void onHungerBarChange(FoodLevelChangeEvent event)
	{
		event.setFoodLevel(20);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event)
	{
		if(GameManager.gameActive)
		{
			event.setCancelled(true);
		}
	}
	
	

}
