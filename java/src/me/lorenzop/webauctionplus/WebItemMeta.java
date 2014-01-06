package me.lorenzop.webauctionplus;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class WebItemMeta {
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	private WebItemMeta() {}


	// encode/decode enchantments for database storage
	public static String encodeEnchantments(Player p, ItemStack stack) {
		if(stack == null) return "";
		Map<Enchantment, Integer> enchantments = stack.getEnchantments();
		if(enchantments==null || enchantments.isEmpty()) return "";
		// get enchantments
		HashMap<Integer, Integer> enchMap = new HashMap<Integer, Integer>();
		boolean removedUnsafe = false;
		for(Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
			// check safe enchantments
			int level = checkSafeEnchantments(stack, entry.getKey(), entry.getValue() );
			if(level == 0) {
				removedUnsafe = true;
				continue;
			}
			enchMap.put(getEnchId(entry.getKey()), level);
		}
		if(removedUnsafe && p != null) p.sendMessage(WebAuctionPlus.logPrefix+WebAuctionPlus.Lang.getString("removed_enchantments"));
		// sort by enchantment id
		SortedSet<Integer> enchSorted = new TreeSet<Integer> (enchMap.keySet());
		// build string
		String enchStr = "";
		for(int enchId : enchSorted) {
			int level = enchMap.get(enchId);
			if(!enchStr.isEmpty()) enchStr += ",";
			enchStr += Integer.toString(enchId)+":"+Integer.toString(level);
		}
		return enchStr;
	}
	// decode enchantments from database
	public static boolean decodeEnchantments(Player p, ItemStack stack, String enchStr) {
		if(enchStr == null || enchStr.isEmpty()) return false;
		Map<Enchantment, Integer> ench = new HashMap<Enchantment, Integer>();
		String[] parts = enchStr.split(",");
		boolean removedUnsafe = false;
		for(String part : parts) {
			if(part==null || part.isEmpty()) continue;
			String[] split = part.split(":");
			if(split.length != 2) {
				WebAuctionPlus.log.warning(WebAuctionPlus.logPrefix+"Invalid enchantment data found: "+part);
				continue;
			}
			int enchId = -1;
			int level  = -1;
			try {
				enchId = Integer.valueOf(split[0]);
				level  = Integer.valueOf(split[1]);
			} catch(Exception ignore) {}
			if(enchId<0 || level<1) {
				WebAuctionPlus.log.warning(WebAuctionPlus.logPrefix+"Invalid enchantment data found: "+part);
				continue;
			}
			Enchantment enchantment = getEnchById(enchId);
			if(enchantment == null) {
				WebAuctionPlus.log.warning(WebAuctionPlus.logPrefix+"Invalid enchantment id found: "+part);
				continue;
			}
			// check safe enchantments
			level = checkSafeEnchantments(stack, enchantment, level);
			if(level == 0) {
				removedUnsafe = true;
				continue;
			}
			// add enchantment to map
			ench.put(enchantment, level);
		}
		if(removedUnsafe) p.sendMessage(WebAuctionPlus.logPrefix+WebAuctionPlus.Lang.getString("removed_enchantments"));
		// add enchantments to stack
		if(WebAuctionPlus.timEnabled())
			stack.addUnsafeEnchantments(ench);
		else
			stack.addEnchantments(ench);
		return removedUnsafe;
	}
	// check natural enchantment
	public static int checkSafeEnchantments(ItemStack stack, Enchantment enchantment, int level) {
		if(stack == null || enchantment == null) return 0;
		if(level < 1) return 0;
		// can enchant item
		if(!enchantment.canEnchantItem(stack)) {
			WebAuctionPlus.log.warning(WebAuctionPlus.logPrefix+"Removed unsafe enchantment: "+stack.toString()+"  "+enchantment.toString());
			return 0;
		}
		if(WebAuctionPlus.timEnabled()) {
			if(level > 127) level = 127;
		} else {
			// level too low
			if(level < enchantment.getStartLevel()) {
				WebAuctionPlus.log.warning(WebAuctionPlus.logPrefix+"Raised unsafe enchantment: "+
					Integer.toString(level)+"  "+stack.toString()+"  "+enchantment.toString()+"  to level: "+enchantment.getStartLevel() );
				level = enchantment.getStartLevel();
			}
			// level too high
			if(level > enchantment.getMaxLevel()) {
				WebAuctionPlus.log.warning(WebAuctionPlus.logPrefix+"Lowered unsafe enchantment: "+
					Integer.toString(level)+"  "+stack.toString()+"  "+enchantment.toString()+"  to level: "+enchantment.getMaxLevel() );
				level = enchantment.getMaxLevel();
			}
		}
		return level;
	}


	@SuppressWarnings("deprecation")
	private static Integer getEnchId(Enchantment ench) {
		if(ench == null)
			return null;
		return ench.getId();
	}
	@SuppressWarnings("deprecation")
	private static Enchantment getEnchById(Integer value) {
		if(value == null)
			return null;
		return Enchantment.getById(value);
	}


}
