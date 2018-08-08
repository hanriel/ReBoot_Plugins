package com.skyandforest.reboot_auth.internal.icon.command;

import org.bukkit.entity.Player;

import com.skyandforest.reboot_auth.internal.icon.IconCommand;

public class PlayerIconCommand extends IconCommand {

	public PlayerIconCommand(String command) {
		super(command);
	}

	@Override
	public void execute(Player player) {
		player.chat('/' + getParsedCommand(player));
	}

}
