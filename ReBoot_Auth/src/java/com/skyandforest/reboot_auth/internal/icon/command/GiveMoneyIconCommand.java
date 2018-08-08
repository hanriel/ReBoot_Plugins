package com.skyandforest.reboot_auth.internal.icon.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.skyandforest.reboot_auth.bridge.EconomyBridge;
import com.skyandforest.reboot_auth.internal.icon.IconCommand;
import com.skyandforest.reboot_auth.util.Utils;

public class GiveMoneyIconCommand extends IconCommand {
	
	private double moneyToGive;
	private String errorMessage;
	
	public GiveMoneyIconCommand(String command) {
		super(command);
		
		if (!Utils.isValidPositiveDouble(command)) {
			errorMessage = ChatColor.RED + "Invalid money amount: " + command;
			return;
		}
		
		moneyToGive = Double.parseDouble(command);
	}

	@Override
	public void execute(Player player) {
		if (errorMessage != null) {
			player.sendMessage(errorMessage);
			return;
		}
		
		if (EconomyBridge.hasValidEconomy()) {
			EconomyBridge.giveMoney(player, moneyToGive);
		} else {
			player.sendMessage(ChatColor.RED + "Vault with a compatible economy plugin not found. Please inform the staff.");
		}
	}

}
