package curtain.tag.utils;

import org.bukkit.entity.Player;

import curtain.tag.TagPlugin;

public class F
{

	public static String f(String text)
	{
		String thing = TagPlugin.getPlugin(TagPlugin.class).getConfig().getString("prefix");
		String newThing = thing.replace("&", "§") + text;
		return newThing;
	}
	
	public static void player(Player player, String text)
	{
		player.sendMessage(F.f(text));
	}
	
}
