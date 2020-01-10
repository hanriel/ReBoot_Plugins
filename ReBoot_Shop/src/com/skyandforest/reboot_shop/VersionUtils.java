package com.skyandforest.reboot_shop;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;

public class VersionUtils {

	public static Collection<? extends Player> getOnlinePlayers() {
		try {
			return Bukkit.getOnlinePlayers();
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
}
