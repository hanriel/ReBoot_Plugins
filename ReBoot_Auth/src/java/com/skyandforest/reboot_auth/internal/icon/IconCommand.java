package com.skyandforest.reboot_auth.internal.icon;

import java.util.ArrayList;
import java.util.List;

import com.skyandforest.reboot_auth.config.AsciiPlaceholders;
import com.skyandforest.reboot_auth.internal.Variable;
import org.bukkit.entity.Player;


public abstract class IconCommand {
	
	protected String command;
	private List<Variable> containedVariables;
	
	public IconCommand(String command) {
		this.command = AsciiPlaceholders.placeholdersToSymbols(command).trim();
		this.containedVariables = new ArrayList<Variable>();
		
		for (Variable variable : Variable.values()) {
			if (command.contains(variable.getText())) {
				containedVariables.add(variable);
			}
		}
	}
	
	public String getParsedCommand(Player executor) {
		if (containedVariables.isEmpty()) {
			return command;
		}
		
		String commandCopy = command;
		for (Variable variable : containedVariables) {
			commandCopy = commandCopy.replace(variable.getText(), variable.getReplacement(executor));
		}
		return commandCopy;
	}
	
	public abstract void execute(Player player);
	
}
