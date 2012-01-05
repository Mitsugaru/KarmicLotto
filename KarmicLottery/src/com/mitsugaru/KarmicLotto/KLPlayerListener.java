package com.mitsugaru.KarmicLotto;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class KLPlayerListener extends PlayerListener {
	private KarmicLotto plugin;

	public KLPlayerListener(KarmicLotto karmicLotto) {
		plugin = karmicLotto;
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(!event.isCancelled())
		{
			if (event.getAction() == Action.LEFT_CLICK_BLOCK)
			{
				final Block block = event.getClickedBlock();
				if(event.getClickedBlock().getType().equals(Material.WALL_SIGN) || event.getClickedBlock().getType().equals(Material.SIGN) || event.getClickedBlock().getType().equals(Material.SIGN_POST))
				{
					Sign sign = (Sign) block.getState();
					if(ChatColor.stripColor(sign.getLine(1)).equalsIgnoreCase("[KarmicLotto]"))
					{
						if(plugin.getPerm().has(event.getPlayer(), "KarmicLotto.use"))
						plugin.getLotto().execute(event.getPlayer(), ChatColor.stripColor(sign.getLine(2)), ChatColor.stripColor(sign.getLine(3)));
					}
				}
			}
		}
	}


}
