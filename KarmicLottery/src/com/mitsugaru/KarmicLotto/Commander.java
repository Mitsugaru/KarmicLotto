package com.mitsugaru.KarmicLotto;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.mitsugaru.KarmicLotto.permissions.PermCheck;
import com.mitsugaru.KarmicLotto.permissions.Permission;

public class Commander implements CommandExecutor {
	private KarmicLotto plugin;
	private PermCheck perm;
	private final static String bar = "======================";

	public Commander(KarmicLotto karmiclotto) {
		plugin = karmiclotto;
		//Create permission handler
		perm = plugin.getPerm();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(args.length == 0)
		{
			sender.sendMessage(ChatColor.BLUE + bar + "=====");
			sender.sendMessage(ChatColor.GREEN + "KarmicLotto v"
					+ plugin.getDescription().getVersion());
			sender.sendMessage(ChatColor.GREEN + "Coded by Mitsugaru");
			sender.sendMessage(ChatColor.BLUE + "===========" + ChatColor.GRAY
					+ "Config" + ChatColor.BLUE + "===========");
			sender.sendMessage(ChatColor.GRAY + "Effects: " + plugin.getPluginConfig().effects);
			for(String name : plugin.getConfig().getStringList("list"))
			{
				sender.sendMessage(ChatColor.LIGHT_PURPLE + name);
			}
			return true;
		}
		else
		{
			String com = args[0];
			if(com.equals("reload"))
			{
				if(perm.has(sender, Permission.ADMIN.getNode()))
				{
					plugin.getPluginConfig().reload();
					sender.sendMessage(ChatColor.GREEN + KarmicLotto.prefix + " Config reloaded");
				}
				else
				{
					sender.sendMessage(ChatColor.RED + KarmicLotto.prefix + " Lack permission: KarmicLotto.admin");
				}
				return true;
			}
			else if(com.equals("help") || com.equals("?"))
			{
				sender.sendMessage(ChatColor.GREEN + KarmicLotto.prefix + " Sign instructions:");
				sender.sendMessage(ChatColor.YELLOW + " Second line: [KarmicLotto]");
				sender.sendMessage(ChatColor.YELLOW + " Third line: lotto name");
				sender.sendMessage(ChatColor.YELLOW + " Fourth line: lotto price");
				return true;
			}
		}
		return false;
	}

}
