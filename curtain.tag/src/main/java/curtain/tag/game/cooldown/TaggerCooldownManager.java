package curtain.tag.game.cooldown;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class TaggerCooldownManager
{

	private final Map<Player, Long> cooldowns = new HashMap<Player, Long>();

	public static final int cooldown = 10;

	public void setCooldown(Player player, long time)
	{
		if (time < 1)
		{
			cooldowns.remove(player);
		} else
		{
			cooldowns.put(player, time);
		}
	}

	public Long getCooldown(Player player)
	{
		return cooldowns.getOrDefault(player, (long) 0);
	}

}
