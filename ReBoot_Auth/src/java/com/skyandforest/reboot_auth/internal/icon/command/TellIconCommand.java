package com.skyandforest.reboot_auth.internal.icon.command;

import org.bukkit.entity.Player;

import com.skyandforest.reboot_auth.internal.icon.IconCommand;
import com.skyandforest.reboot_auth.util.Utils;

public class TellIconCommand extends IconCommand {

	public TellIconCommand(String command) {
		super(Utils.addColors(command));
	}

	@Override
	public void execute(Player player) {
		player.sendMessage(getParsedCommand(player));
	}

}
