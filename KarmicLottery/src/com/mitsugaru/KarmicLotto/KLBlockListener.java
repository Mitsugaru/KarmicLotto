package com.mitsugaru.KarmicLotto;

import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class KLBlockListener extends BlockListener {
	private KarmicLotto plugin;

	public KLBlockListener(KarmicLotto karmicLotto) {
		plugin = karmicLotto;
	}

	@Override
	public void onSignChange(final SignChangeEvent event) {
		if (!event.isCancelled())
		{
			if (ChatColor.stripColor(event.getLine(1)).equalsIgnoreCase(
					"[KarmicLotto]"))
			{


				boolean valid = false;
				if(plugin.getPerm().has(event.getPlayer(), "KarmicLotto.create"))
				{
					if(plugin.getLotto().validateLotto(event.getPlayer(), event.getLine(2), event.getLine(3)))
					{
						//Colorize to tell that it has gone into effect
						event.setLine(1, ChatColor.GOLD + "[KarmicLotto]");
						final String name = ChatColor.AQUA + event.getLine(2);
						final String amount = ChatColor.GREEN + event.getLine(3);
						event.setLine(2, name);
						event.setLine(3, amount);
						event.getPlayer().sendMessage(ChatColor.GREEN + KarmicLotto.prefix + " Successully made lotto sign");
						valid = true;
					}
					else
					{
						event.getPlayer().sendMessage(ChatColor.RED + KarmicLotto.prefix + " Not a valid lotto sign");
					}
				}
				else
				{
					event.getPlayer().sendMessage(ChatColor.RED + KarmicLotto.prefix + " Lack permission: KarmicLotto.create");
				}
				if(!valid)
				{
					// Reformat sign
					event.setLine(0, "");
					event.setLine(1, "");
					event.setLine(2, "");
					event.setLine(3, "");
				}
			}
		}
	}
}
