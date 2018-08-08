package com.skyandforest.reboot_dungeons;

import org.bukkit.*;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

public class Utils {

	private static String bukkitVersion;
	
	public static String getBukkitVersion() {
		if (bukkitVersion == null) {
			String packageName = Bukkit.getServer().getClass().getPackage().getName();
			bukkitVersion = packageName.substring(packageName.lastIndexOf('.') + 1);
		}
		
		return bukkitVersion;
	}

	static String addColors(String input) {
		if (input == null || input.isEmpty()) return input;
		return ChatColor.translateAlternateColorCodes('&', input);
	}
	
	public static List<String> addColors(List<String> input) {
		if (input == null || input.isEmpty()) return input;
		for (int i = 0; i < input.size(); i++) {
			input.set(i, addColors(input.get(i)));
		}
		return input;
	}
	
	public static String addYamlExtension(String input) {
		if (input == null) return null;
		return input.toLowerCase().endsWith(".yml") ? input : input + ".yml";
	}
	
	private static DecimalFormat decimalFormat = new DecimalFormat("0.##");
	public static String decimalFormat(double number) {
		return decimalFormat.format(number);
	}
	
	@SuppressWarnings("deprecation")

	
	public static int makePositive(int i) {
		return i < 0 ? 0 : i;
	}
	
	public static boolean isValidInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	public static boolean isValidPositiveInteger(String input) {
		try {
			return Integer.parseInt(input) > 0;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	public static boolean isValidShort(String input) {
		try {
			Short.parseShort(input);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	public static boolean isValidPositiveDouble(String input) {
		try {
			return Double.parseDouble(input) > 0.0;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
	
	public static List<String> readLines(File file) throws IOException, Exception {
		BufferedReader br = null;

		try {
			List<String> lines = newArrayList();

			if (!file.exists()) {
				throw new FileNotFoundException();
			}

			br = new BufferedReader(new FileReader(file));
			String line = br.readLine();

			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}

			return lines;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
	}
	

	
	
	public static void saveResourceSafe(Plugin plugin, String name) {
		try {
			plugin.saveResource(name, false);
		} catch (Exception ex) {
			// Shhh...
		}
	}
	
	public static <T> Set<T> newHashSet() {
		return new HashSet<T>();
	}
	
	public static <T, V> Map<T, V> newHashMap() {
		return new HashMap<T, V>();
	}
	
	public static <T> List<T> newArrayList() {
		return new ArrayList<T>();
	}
	
	public static String join(Iterable<?> iterable, String separator) {
		StringBuilder builder = new StringBuilder();
		Iterator<?> iter = iterable.iterator();
		
		boolean first = true;
		
		while (iter.hasNext()) {
			if (first) {
				first = false;
			} else {
				builder.append(separator);
			}
			
			builder.append(iter.next());
		}
		
		return builder.toString();
	}
	
	public static boolean isClassLoaded(String name) {
		try {
			Class.forName(name);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
