package com.hanriel.reboot_auth.internal.icon.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.hanriel.reboot_auth.internal.icon.IconCommand;
import com.hanriel.reboot_auth.util.Utils;

public class BroadcastIconCommand extends IconCommand {

	public BroadcastIconCommand(String command) {
		super(Utils.addColors(command));
	}

	@Override
	public void execute(Player player) {
		Bukkit.broadcastMessage(getParsedCommand(player));
		
	}

}
