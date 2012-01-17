package com.mitsugaru.KarmicLotto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;

public class Config {
	private KarmicLotto plugin;
	public boolean debug, effects;
	public int amount;
	public int percent;

	public Config(KarmicLotto karmiclottery) {
		plugin = karmiclottery;
		ConfigurationSection config = plugin.getConfig();
		// Hashmap of defaults
		final Map<String, Object> defaults = new HashMap<String, Object>();
		defaults.put("defaultAmount", 1);
		defaults.put("defaultPercent", 1);
		defaults.put("effects", true);
		defaults.put("list", new ArrayList<String>());
		defaults.put("version", plugin.getDescription().getVersion());
		boolean gen = false;
		for (final Entry<String, Object> e : defaults.entrySet())
		{
			if (!config.contains(e.getKey()))
			{
				config.set(e.getKey(), e.getValue());
				gen = true;
			}
		}
		if (gen)
		{
			plugin.syslog
					.info(KarmicLotto.prefix
							+ " No KarmicLotto config file found. Creating config file.");
		}
		debug = config.getBoolean("debug", false);
		amount = config.getInt("defaultAmount", 1);
		percent = config.getInt("defaultPercent", 1);
		effects = config.getBoolean("effects", true);
		// Bounds check
		boundsCheck();
		// Check for update
		checkUpdate();
	}

	private void checkUpdate() {
		// TODO Auto-generated method stub

	}

	public void reload() {
		// Reload
		plugin.reloadConfig();
		// Grab config
		ConfigurationSection config = plugin.getConfig();
		// Set variables
		debug = config.getBoolean("debug", false);
		amount = config.getInt("defaultAmount", 1);
		percent = config.getInt("defaultPercent", 1);
		effects = config.getBoolean("effects", true);
		boundsCheck();
	}

	public void boundsCheck() {
		// Bounds check
		if (amount <= 0)
		{
			plugin.syslog.warning(KarmicLotto.prefix
					+ " Zero or negative amount for default");
			amount = 1;
		}
		if (percent <= 0)
		{
			plugin.syslog.warning(KarmicLotto.prefix
					+ " Zero or negative percent for default");
			percent = 1;
		}
	}

	public Map<Item, Double> getLotto(String lotto) {
		final Map<Item, Double> list = new HashMap<Item, Double>();
		final ConfigurationSection section = plugin.getConfig()
				.getConfigurationSection(lotto);
		if(section == null)
		{
			//NPE check, return empty list
			plugin.syslog.warning("Attempted to get lotto '" + lotto + "', but the section does not exist...");
			plugin.syslog.warning("!!!!!!CHECK YOUR CONFIG!!!!!!");
			return list;

		}
		for (final String entry : section.getKeys(false))
		{
			try
			{
				int item = 1;
				int data = 0;
				if (entry.contains("&"))
				{
					// Split
					final String[] split = entry.split("&");
					item = Integer.parseInt(split[0]);
					data = Integer.parseInt(split[1]);
					if (data < 0)
					{
						plugin.syslog.warning(KarmicLotto.prefix
								+ " Negative data value for entry: " + entry);
						data = 0;
					}
				}
				else
				{
					item = Integer.parseInt(entry);
				}
				if (item <= 0)
				{
					plugin.syslog.warning(KarmicLotto.prefix
							+ " Zero or negative item id for entry: " + entry);
					item = 1;
				}

				// If it has child nodes, parse those as well
				if (section.isConfigurationSection(entry))
				{
					int prizeAmount = plugin.getConfig().getInt(
							lotto + "." + entry + ".amount", amount);
					double prizePercent = plugin.getConfig().getDouble(
							lotto + "." + entry + ".percent", percent);
					// Bounds check on the values
					if (prizeAmount <= 0)
					{
						plugin.syslog.warning(KarmicLotto.prefix
								+ " Zero or negative amount for entry: "
								+ entry);
						prizeAmount = amount;
					}
					if (prizePercent <= 0)
					{
						plugin.syslog.warning(KarmicLotto.prefix
								+ " Zero or negative percent for entry: "
								+ entry);
						prizePercent = 1;
					}
					if (item != 373)
					{
						list.put(new Item(item, Byte.parseByte("" + data),
								(short) data, prizeAmount), prizePercent);
					}
					else
					{
						list.put(new Item(item, Byte.parseByte("" + 0),
								(short) data, prizeAmount), prizePercent);
					}
				}
				else
				{
					double prizePercent = section.getDouble(entry, percent);
					if (item != 373)
					{
						list.put(new Item(item, Byte.valueOf("" + data),
								(short) data, amount), prizePercent);
					}
					else
					{
						list.put(new Item(item, Byte.valueOf("" + 0),
								(short) data, amount), prizePercent);
					}
				}
			}
			catch (final NumberFormatException ex)
			{
				plugin.syslog.warning("Non-integer value in karma list");
				ex.printStackTrace();
			}
		}
		return list;
	}

	public void save() {
		plugin.saveConfig();
	}

	public void setProperty(String path, Object o) {
		plugin.getConfig().set(path, o);
	}

	public Object getProperty(String path) {
		return plugin.getConfig().get(path);
	}

	public List<String> getStringList(String path) {
		List<String> list = plugin.getConfig().getStringList(path);
		if (list != null)
		{
			return list;
		}
		return new ArrayList<String>();
	}

	public void removeProperty(String path) {
		plugin.getConfig().set(path, null);
	}

	public boolean getBoolean(String path) {
		return (Boolean) (this.getProperty(path));
	}

	public double getDouble(String path) {
		return (Double) (this.getProperty(path));
	}

	public int getInteger(String path) {
		return (Integer) (this.getProperty(path));
	}

	public String getString(String path) {
		return (String) this.getProperty(path);
	}
}
