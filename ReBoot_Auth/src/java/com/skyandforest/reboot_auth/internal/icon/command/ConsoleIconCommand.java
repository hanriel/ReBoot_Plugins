package com.skyandforest.reboot_auth.internal.icon.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.skyandforest.reboot_auth.internal.icon.IconCommand;

public class ConsoleIconCommand extends IconCommand {

	public ConsoleIconCommand(String command) {
		super(command);
	}

	@Override
	public void execute(Player player) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), getParsedCommand(player));
	}

}
