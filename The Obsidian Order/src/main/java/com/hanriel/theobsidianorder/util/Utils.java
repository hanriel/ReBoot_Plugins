package com.hanriel.theobsidianorder.util;

import com.hanriel.theobsidianorder.exception.FormatException;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Utils {

    // Default material names are ugly.
    private static Map<String, Material> materialMap = newHashMap();

    private static String bukkitVersion;

    public static String getBukkitVersion() {
        if (bukkitVersion == null) {
            String packageName = Bukkit.getServer().getClass().getPackage().getName();
            bukkitVersion = packageName.substring(packageName.lastIndexOf('.') + 1);
        }

        return bukkitVersion;
    }

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

    public static String addColorsTag(String tag, String input) {
        if (input == null || input.isEmpty()) return input;
        return ChatColor.translateAlternateColorCodes('&', tag + input);
    }


    public static <T> Set<T> newHashSet() {
        return new HashSet<>();
    }

    public static <T, V> Map<T, V> newHashMap() {
        return new HashMap<>();
    }

    public static <T> List<T> newArrayList() {
        return new ArrayList<>();
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


    public static String addYamlExtension(String input) {
        if (input == null) return null;
        return input.toLowerCase().endsWith(".yml") ? input : input + ".yml";
    }

    private static DecimalFormat decimalFormat = new DecimalFormat("0.##");

    public static String decimalFormat(double number) {
        return decimalFormat.format(number);
    }

    @SuppressWarnings("deprecation")

    public static Sound matchSound(String input) {
        if (input == null) return null;

        input = StringUtils.stripChars(input.toLowerCase(), " _-");

        for (Sound sound : Sound.values()) {
            if (StringUtils.stripChars(sound.toString().toLowerCase(), "_").equals(input)) return sound;
        }
        return null;
    }

    public static String formatMaterial(Material material) {
        return StringUtils.capitalizeFully(material.toString().replace("_", " "));
    }

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
                } catch (IOException ignored) {
                }
            }
        }
    }

    public static Color parseColor(String input) throws FormatException {
        String[] split = StringUtils.stripChars(input, " ").split(",");

        if (split.length != 3) {
            throw new FormatException("it must be in the format \"red, green, blue\".");
        }

        int red, green, blue;

        try {
            red = Integer.parseInt(split[0]);
            green = Integer.parseInt(split[1]);
            blue = Integer.parseInt(split[2]);
        } catch (NumberFormatException ex) {
            throw new FormatException("it contains invalid numbers.");
        }

        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new FormatException("it should only contain numbers between 0 and 255.");
        }

        return Color.fromRGB(red, green, blue);
    }

    public static void sendMsg(CommandSender player, String msg) {
        player.sendMessage(addColors(msg));
    }
    public static void sendMsg(Player player, String msg) {
        player.sendMessage(addColors(msg));
    }

}
