package com.mitsugaru.KarmicLotto;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commander implements CommandExecutor {
	private PermCheck perm;

	public Commander(KarmicLotto plugin) {
		//Create permission handler
		perm = plugin.getPerm();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if(args.length == 0)
		{
			//TODO version?
		}
		else
		{
			String com = args[0];
			if(com.equals("reload"))
			{
				if(perm.has(sender, "KarmicLotto.admin"))
				{
					//TODO reload
					sender.sendMessage(ChatColor.GREEN + KarmicLotto.prefix + " Config reloaded");
				}
				else
				{
					sender.sendMessage(ChatColor.RED + KarmicLotto.prefix + " Lack permission: KarmicLotto.admin");
				}
			}
		}
		return false;
	}

}
