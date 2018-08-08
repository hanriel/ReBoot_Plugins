package com.skyandforest.reboot_auth.internal.icon.command;

import org.bukkit.entity.Player;

import com.skyandforest.reboot_auth.bridge.bungee.BungeeCordUtils;
import com.skyandforest.reboot_auth.internal.icon.IconCommand;

public class ServerIconCommand extends IconCommand {

	public ServerIconCommand(String command) {
		super(command);
	}

	@Override
	public void execute(Player player) {
		BungeeCordUtils.connect(player, command);
	}

}
