package com.skyandforest.reboot_core.util;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class Utils {

    public static String addColors(String input) {
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


    public static void saveResourceSafe(Plugin plugin, String name) {
        try {
            plugin.saveResource(name, false);
        } catch (Exception ex) {
            // Shhh...
        }
    }
}
