package com.mitsugaru.KarmicLotto;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class KarmicLotto extends JavaPlugin {
	public Logger syslog;
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
		syslog.info(prefix + " Plugin disabled");
	}

	@Override
	public void onEnable() {
		// Logger
		syslog = this.getServer().getLogger();
		// Config
		config = new Config(this);
		//Create permissions
		perm = new PermCheck(this);
		//Create commander
		commander = new Commander(this);
		getCommand("lotto").setExecutor(commander);
		//Get economy
		RegisteredServiceProvider<Economy> economyProvider = this.getServer()
				.getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null)
		{
			eco = economyProvider.getProvider();
		}
		else
		{
			// No economy system found, disable
			syslog.warning(prefix + " No economy found!");
			this.getServer().getPluginManager().disablePlugin(this);
		}

		PluginManager pm = this.getServer().getPluginManager();
		//Create listener
		KLBlockListener blockListener = new KLBlockListener(this);
		KLPlayerListener playerListener = new KLPlayerListener(this);
		pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, playerListener, Priority.Monitor, this);
		//Generate lotto object
		lotto = new Lotto(this);
		syslog.info(prefix + " v " + this.getDescription().getVersion() + " enabled");
	}

	public Lotto getLotto()
	{
		return lotto;
	}

	public Config getPluginConfig()
	{
		return config;
	}

	public Economy getEco()
	{
		return eco;
	}

	public PermCheck getPerm() {
		return perm;
	}
}
