package me.lorenzop.webauctionplus;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;


public class WebItemMeta {
	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	private WebItemMeta() {}


	// encode enchantments for database storage
	public static String encodeEnchants(final ItemStack stack, final Player player) {
		if(player == null) throw new NullPointerException();
		if(stack  == null) throw new NullPointerException();
		return encodeEnchants(getItemEnchants(stack, player));
	}
	private static String encodeEnchants(final Map<Enchantment, Integer> enchants) {
		if(enchants == null || enchants.isEmpty()) return null;
		// convert enchantments to int id for sorting
		final Map<Integer, Integer> intMap = new HashMap<Integer, Integer>();
		for(final Entry<Enchantment, Integer> entry : enchants.entrySet()) {
			intMap.put(
				getEnchId(entry.getKey()),
				entry.getValue()
			);
		}
		// sort by enchantment id
		final SortedSet<Integer> sorted = new TreeSet<Integer>(intMap.keySet());
		// build string
		final StringBuilder str = new StringBuilder();
		for(final Integer id : sorted) {
			final int level = intMap.get(id);
			if(str.length() > 0)
				str.append(",");
			str.append(Integer.toString(id));
			str.append(":");
			str.append(Integer.toString(level));
		}
		return str.toString();
	}


	// decode enchantments from string
	public static void decodeEnchants(final ItemStack stack, final Player player, final String str) {
		if(str == null || str.isEmpty()) return;
		// parse string
		final Map<Enchantment, Integer> tmpEnchants = decodeEnchants(str);
		if(tmpEnchants == null || tmpEnchants.isEmpty()) return;
		// check safe enchantments
		boolean removedUnsafe = false;
		final Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
		for(Entry<Enchantment, Integer> entry : tmpEnchants.entrySet()) {
			final Enchantment ench = entry.getKey();
			final int level = checkSafeEnchantment(stack, ench, entry.getValue());
			if(level < 1) {
				removedUnsafe = true;
				continue;
			}
			enchants.put(ench, level);
		}
		if(removedUnsafe)
			player.sendMessage(WebAuctionPlus.logPrefix+WebAuctionPlus.Lang.getString("removed_enchantments"));
		applyItemEnchants(stack, player, enchants);
	}
	private static Map<Enchantment, Integer> decodeEnchants(final String str) {
		if(str == null || str.isEmpty()) return null;
		final Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
		// parse string
		for(String part : str.split(",")) {
			if(part==null || part.isEmpty()) continue;
			String[] split = part.split(":");
			if(split.length != 2) {
				WebAuctionPlus.log.warning(WebAuctionPlus.logPrefix+"Invalid enchantment data: "+part);
				continue;
			}
			int enchId = -1;
			int level  = -1;
			try {
				enchId = Integer.valueOf(split[0]);
				level  = Integer.valueOf(split[1]);
			} catch(Exception ignore) {}
			if(enchId < 0 || level < 1) {
				WebAuctionPlus.log.warning(WebAuctionPlus.logPrefix+"Invalid enchantment data: "+part);
				continue;
			}
			final Enchantment enchantment = getEnchById(enchId);
			if(enchantment == null) {
				WebAuctionPlus.log.warning(WebAuctionPlus.logPrefix+"Invalid enchantment id: "+part);
				continue;
			}
			// add enchantment to map
			enchants.put(enchantment, level);
		}
		if(enchants.isEmpty())
			return null;
		return enchants;
	}


	// safely get enchantments from an item
	public static Map<Enchantment, Integer> getItemEnchants(final ItemStack stack, final Player player) {
		if(stack == null) return null;
		final Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
		Map<Enchantment, Integer> tmpEnchants = null;

		// item meta
//		if(stack.hasItemMeta()) {
			final ItemMeta meta = stack.getItemMeta();
			// enchanted book
			if(meta instanceof EnchantmentStorageMeta) {
				EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) meta;
				tmpEnchants = bookMeta.getStoredEnchants();
			}
//		}
		// normal item
		if(tmpEnchants == null)
			tmpEnchants = stack.getEnchantments();

		// check safe enchantments
		boolean removedUnsafe = false;
		for(Entry<Enchantment, Integer> entry : tmpEnchants.entrySet()) {
			final Enchantment ench = entry.getKey();
			final int level = checkSafeEnchantment(stack, ench, entry.getValue());
			if(level < 1) {
				removedUnsafe = true;
				continue;
			}
			enchants.put(ench, level);
		}
		if(removedUnsafe && player != null)
			player.sendMessage(WebAuctionPlus.logPrefix+WebAuctionPlus.Lang.getString("removed_enchantments"));
		return enchants;
	}
	// safely apply enchantments to a stack
	private static void applyItemEnchants(final ItemStack stack, final Player player,
			Map<Enchantment, Integer> enchants) {
		if(stack == null || enchants == null || enchants.isEmpty()) return;
		boolean removedUnsafe = false;
		for(Entry<Enchantment, Integer> entry : enchants.entrySet()) {
			final Enchantment ench = entry.getKey();
			final int level = checkSafeEnchantment(stack, ench, entry.getValue());
			if(level < 1) {
				removedUnsafe = true;
				continue;
			}

			// item meta
			final ItemMeta meta = stack.getItemMeta();
			// enchanted book
			if(meta instanceof EnchantmentStorageMeta) {
				EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) meta;
				bookMeta.addStoredEnchant(ench, level, WebAuctionPlus.timEnabled());
				stack.setItemMeta(bookMeta);
				continue;
			}
			// normal item
			if(WebAuctionPlus.timEnabled())
				stack.addUnsafeEnchantment(ench, level);
			else
				stack.addEnchantment(ench, level);
		}
		if(removedUnsafe && player != null)
			player.sendMessage(WebAuctionPlus.logPrefix+WebAuctionPlus.Lang.getString("removed_enchantments"));
	}


	// check natural enchantment
	private static int checkSafeEnchantment(final ItemStack stack, final Enchantment ench, int level) {
		final int FAIL_VALUE = -1;
		if(stack == null || ench == null) return FAIL_VALUE;
		if(level < 1) return FAIL_VALUE;
		// can enchant item
		if(!ench.canEnchantItem(stack)) {
			WebAuctionPlus.log.warning(WebAuctionPlus.logPrefix+"Removed unsafe enchantment: "+stack.toString()+"  "+ench.toString());
			return FAIL_VALUE;
		}
		if(WebAuctionPlus.timEnabled()) {
			if(level > 127) level = 127;
		} else {
			// level too low
			if(level < ench.getStartLevel()) {
				WebAuctionPlus.log.warning(WebAuctionPlus.logPrefix+"Raised unsafe enchantment: "+
					Integer.toString(level)+"  "+stack.toString()+"  "+ench.toString()+"  to level: "+ench.getStartLevel() );
				level = ench.getStartLevel();
			}
			// level too high
			if(level > ench.getMaxLevel()) {
				WebAuctionPlus.log.warning(WebAuctionPlus.logPrefix+"Lowered unsafe enchantment: "+
					Integer.toString(level)+"  "+stack.toString()+"  "+ench.toString()+"  to level: "+ench.getMaxLevel() );
				level = ench.getMaxLevel();
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
