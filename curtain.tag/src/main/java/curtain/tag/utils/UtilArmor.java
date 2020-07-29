package curtain.tag.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class UtilArmor
{

	@SuppressWarnings("deprecation") //haha fuck you
	public static ItemStack createLeather(Material leatherPiece, String name, org.bukkit.Color color)
	{
		ItemStack item = new ItemStack(leatherPiece, 1);
		LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
		meta.setColor(color);
		item.setItemMeta(meta);
		item.setDurability((short) 0);
		return item;
	}
	
}
