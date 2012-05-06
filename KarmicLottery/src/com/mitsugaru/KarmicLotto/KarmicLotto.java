package com.mitsugaru.KarmicLotto;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.mitsugaru.KarmicLotto.events.KLBlockListener;
import com.mitsugaru.KarmicLotto.events.KLPlayerListener;
import com.mitsugaru.KarmicLotto.permissions.PermCheck;

public class KarmicLotto extends JavaPlugin {
	private Commander commander;
	private Config config;
	private Lotto lotto;
	private Economy eco;
	private PermCheck perm;
	public static final String prefix = "[KarmicLotto]";

	@Override
	public void onDisable() {
		// Save config
		this.saveConfig();
		getLogger().info(prefix + " Plugin disabled");
	}

	@Override
	public void onEnable() {
		// Config
		config = new Config(this);
		// Create permissions
		perm = new PermCheck(this);
		// Create commander
		commander = new Commander(this);
		getCommand("lotto").setExecutor(commander);
		// Grab plugin manager
		final PluginManager pm = getServer().getPluginManager();
		// Create listeners
		KLBlockListener blockListener = new KLBlockListener(this);
		KLPlayerListener playerListener = new KLPlayerListener(this);
		//Register listeners
		pm.registerEvents(blockListener, this);
		pm.registerEvents(playerListener, this);
		// Get economy
		RegisteredServiceProvider<Economy> economyProvider = this.getServer()
				.getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			eco = economyProvider.getProvider();
		} else {
			// No economy system found, disable
			getLogger().warning(prefix + " No economy found!");
			this.getServer().getPluginManager().disablePlugin(this);
		}
		// Generate lotto object
		lotto = new Lotto(this);
		getLogger().info(prefix + " v " + this.getDescription().getVersion()
				+ " enabled");
	}

	public Lotto getLotto() {
		return lotto;
	}

	public Config getPluginConfig() {
		return config;
	}

	public Economy getEco() {
		return eco;
	}

	public PermCheck getPerm() {
		return perm;
	}
}
