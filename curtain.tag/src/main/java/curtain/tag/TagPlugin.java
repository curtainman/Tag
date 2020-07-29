package curtain.tag;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import curtain.tag.command.ForceTaggerCommand;
import curtain.tag.command.TagCommand;
import curtain.tag.game.GameManager;
import curtain.tag.game.GamePlayer;
import curtain.tag.game.powerups.PowerupManager;

public class TagPlugin extends JavaPlugin
{

	public static boolean configOK;
	
	
	//TODO make a lang file uwu yes
	
	@Override
	public void onEnable()
	{
		//Config shits
		saveDefaultConfig();
		if(getConfig().getDouble("taggerX") == 0 || getConfig().getDouble("taggerY") == 0 ||
				getConfig().getDouble("taggerZ") == 0 || getConfig().getDouble("playerX") == 0 || 
				getConfig().getDouble("playerY") == 0 || getConfig().getDouble("playerZ") == 0)
		{
			configOK = false;
			Bukkit.getLogger().log(Level.SEVERE, "INVALID CONFIG! IMPORTANT! Before you use the tag plugin, be sure to use the /tag command to set spawn locations! The plugin will not work if you do not set locations!");
		}
		else
		{
			configOK = true;
		}
		//Register shit
		this.getCommand("tag").setExecutor(new TagCommand());
		this.getCommand("forceit").setExecutor(new ForceTaggerCommand());
		new GameManager().Init();
		
		new PowerupManager().Init();
		Bukkit.getPluginManager().registerEvents(new GamePlayer(), this);
		Bukkit.getLogger().log(Level.INFO, "Tag minigame plugin enabled!");
	}
	
	@Override
	public void onDisable()
	{
		
	}

	
	public static GameManager getManager()
	{
		return new GameManager();
	}
	
}
