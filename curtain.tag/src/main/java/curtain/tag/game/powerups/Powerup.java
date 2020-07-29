package curtain.tag.game.powerups;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Powerup
{

	public void onUse(Player user);
	public ItemStack getItem();
	
}
