package com.hanriel.reboot_auth.internal.icon.command;

import org.bukkit.entity.Player;

import com.hanriel.reboot_auth.internal.icon.IconCommand;
import com.hanriel.reboot_auth.util.Utils;

public class TellIconCommand extends IconCommand {

	public TellIconCommand(String command) {
		super(Utils.addColors(command));
	}

	@Override
	public void execute(Player player) {
		player.sendMessage(getParsedCommand(player));
	}

}
