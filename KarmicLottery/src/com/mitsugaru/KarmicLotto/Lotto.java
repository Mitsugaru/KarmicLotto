package com.mitsugaru.KarmicLotto;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Lotto {
	private KarmicLotto plugin;

	public Lotto(KarmicLotto karmiclotto)
	{
		plugin = karmiclotto;
	}

	public boolean validateLotto(Player player, String name, String amount)
	{
		try
		{
			double a = Double.parseDouble(amount);
			if(a < 0)
			{
				//Cannot have negative lottery
				return false;
			}
			List<String> lottos = plugin.getConfig().getStringList("list");
			ListIterator<String> iterator = lottos.listIterator();
			while (iterator.hasNext())
			{
				if (iterator.next().equals(name))
				{
					return true;
				}
			}
			player.sendMessage(ChatColor.RED
					+ " Lotto does not exist.");
		}
		catch(NumberFormatException e)
		{
			player.sendMessage(ChatColor.RED + KarmicLotto.prefix + " Not a valid amount.");
		}
		return false;
	}

	public void execute(Player player, String name, String amount) {
		try
		{
			double price = Double.parseDouble(amount);
			boolean has = false;
			List<String> lottos = plugin.getConfig().getStringList("list");
			ListIterator<String> iterator = lottos.listIterator();
			while (iterator.hasNext())
			{
				if (iterator.next().equals(name))
				{
					has = true;
				}
			}
			if(has)
			{
				final Economy econ = plugin.getEco();
				EconomyResponse er = econ.bankBalance(player.getName());
				double balance = er.balance;
				if (er.type == EconomyResponse.ResponseType.SUCCESS)
				{
					if(balance >= price)
					{
						ItemStack item = generateItem(name);
						HashMap<Integer, ItemStack> residue = player.getInventory().addItem(item);
						if(!residue.isEmpty())
						{
							player.sendMessage(ChatColor.RED +KarmicLotto.prefix + " Could not give all items...");
						}
						else
						{
							this.smokePlayer(player);
							er = econ.withdrawPlayer(player.getName(), price);
							if (er.type == EconomyResponse.ResponseType.SUCCESS)
							{
								final Item i = new Item(item.getTypeId(), item.getData().getData(), item.getDurability());
								player.sendMessage(ChatColor.GREEN + KarmicLotto.prefix + " Got " + ChatColor.AQUA + plugin.getPluginConfig().amount + ChatColor.GREEN + " of " + ChatColor.GOLD + i.name);
							}
							else
							{
								player.sendMessage(ChatColor.YELLOW + KarmicLotto.prefix + " Could not retrieve money from bank account...");
							}
						}
					}
					else
					{
						player.sendMessage(ChatColor.RED + KarmicLotto.prefix + " Not enough money to pay for lottery.");
					}
				}
				else
				{
					player.sendMessage(ChatColor.RED + KarmicLotto.prefix + " You do not appear to have an economy account.");
				}
			}
			else
			{
				player.sendMessage(ChatColor.RED + KarmicLotto.prefix + " Lotto no longer exists.");
			}
		}
		catch(NumberFormatException e)
		{
			player.sendMessage(ChatColor.RED + KarmicLotto.prefix + " Not a valid amount.");
		}
	}

	private ItemStack generateItem(String name) {
		Map<Item, Double> list = plugin.getPluginConfig().getLotto(name);
		double total = 0;
		for(final Double d : list.values().toArray(new Double[0]))
		{
			total += d.doubleValue();
		}
		double high = 1.0;
		if(total != 100)
		{
			final double adj = 100 / total;
			Iterator<Entry<Item, Double>> it = list.entrySet().iterator();
			//adjust
			while(it.hasNext())
			{
				final Map.Entry<Item, Double> entry = it.next();
				final double d = (entry.getValue().doubleValue() * adj) / 100;
				high -= d;
				list.put(entry.getKey(), high);
			}
		}
		else
		{
			Iterator<Entry<Item, Double>> it = list.entrySet().iterator();
			//adjust
			while(it.hasNext())
			{
				final Map.Entry<Item, Double> entry = it.next();
				final double d = entry.getValue().doubleValue() / 100;
				high -= d;
				list.put(entry.getKey(), high);
			}
		}
		Random r = new Random();
		double value = r.nextDouble();
		Item i = new Item(0,Byte.valueOf("" + 0), (short)0);
		Iterator<Entry<Item, Double>> it = list.entrySet().iterator();
		boolean first = true;
		//adjust
		while(it.hasNext())
		{
			final Map.Entry<Item, Double> entry = it.next();
			if(first)
			{
				i = entry.getKey();
			}
			else if(entry.getValue().doubleValue() >= value)
			{
				if(list.get(entry.getKey()).doubleValue() <= list.get(i))
				{
					i = entry.getKey();
				}
			}
			first = false;
		}
		if(i.isPotion())
		{
			return new ItemStack(i.getItemTypeId(), plugin.getPluginConfig().amount, i.itemDurability());
		}
		return new ItemStack(i.getItemTypeId(), plugin.getPluginConfig().amount, i.getData());
	}

	/**
	 * Provides a smoke effect for the player.
	 *
	 * http://forums.bukkit.org/threads/smoke-effect-yes-i-know-others-have-
	 * asked.29492/
	 *
	 * @param Player
	 *            that should get the effect
	 * @author Adamki11s
	 */
	private void smokePlayer(Player player) {
		if (plugin.getPluginConfig().effects)
		{
			Location loc = player.getLocation();
			World w = loc.getWorld();
			int repeat = 0;
			while (repeat < 1)
			{
				for (double x = (loc.getX() - 3); x <= (loc.getX() + 3); x++)
				{
					for (double y = (loc.getY() - 3); y <= (loc.getY() + 3); y++)
					{
						for (double z = (loc.getZ() - 3); z <= (loc.getZ() + 3); z++)
						{
							w.playEffect(new Location(w, x, y, z),
									Effect.SMOKE, 1);
						}
					}
				}
				repeat++;
			}
		}
	}
}
