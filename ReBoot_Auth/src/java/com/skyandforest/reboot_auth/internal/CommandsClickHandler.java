package com.skyandforest.reboot_auth.internal;

import java.util.List;

import org.bukkit.entity.Player;

import com.skyandforest.reboot_auth.api.ClickHandler;
import com.skyandforest.reboot_auth.internal.icon.IconCommand;
import com.skyandforest.reboot_auth.internal.icon.command.OpenIconCommand;

public class CommandsClickHandler implements ClickHandler {

	private List<IconCommand> commands;
	private boolean closeOnClick;
	
	public CommandsClickHandler(List<IconCommand> commands, boolean closeOnClick) {
		this.commands = commands;
		this.closeOnClick = closeOnClick;
		
		if (commands != null && commands.size() > 0) {
			for (IconCommand command : commands) {
				if (command instanceof OpenIconCommand) {
					// Fix GUI closing if KEEP-OPEN is not set, and a command should open another GUI.
					this.closeOnClick = false;
				}
			}
		}
	}
	
	@Override
	public boolean onClick(Player player) {
		if (commands != null && commands.size() > 0) {
			for (IconCommand command : commands) {
				command.execute(player);
			}
		}
		
		return closeOnClick;
	}

	
	
}
