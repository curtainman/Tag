package curtain.tag.game.powerups;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import curtain.tag.utils.F;
import curtain.tag.utils.UtilItem;

public class FlyerPowerup implements Listener, Powerup
{

	@Override
	public ItemStack getItem()
	{
		// TODO Auto-generated method stub
		return UtilItem.createItem(Material.PAPER, "§7Flyer Powerup §e(Right Click)", new String[]
		{ "§7Right clicking this will allow you to", "§7fly in the air for 5 seconds." });
	}

	@Override
	public void onUse(Player user)
	{
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "effect give" + user.getName() + " minecraft:levitation 5 5");
		F.player(user, "You used §eFlyer Powerup§7!");
		user.getInventory().remove(UtilItem.createItem(Material.PAPER, "§7Flyer Powerup §e(Right Click)"));
		user.playSound(user.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
	}


}
