package halleg.bungee.config;

import net.md_5.bungee.config.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SettingsTools {


    public static void checkKey(Configuration con, String path) throws ConfigLoadException {
        if (!con.contains(path)) {
            throw new ConfigLoadException("key \"" + path + "\" is missing.");
        }
    }

    public static String requireStringValue(Configuration con, String path) throws ConfigLoadException {
        checkKey(con, path);
        Object obj = con.get(path);
        return obj.toString();
    }

    public static File requireFileValue(Configuration con, String path) throws ConfigLoadException {
        File file = new File(requireStringValue(con, path));
        if (!file.exists()) {
            throw new ConfigLoadException("key " + path + " is a invalid directory. value: " + file.getAbsolutePath());
        }
        return file;
    }

    public static int requireIntValue(Configuration con, String path) throws ConfigLoadException {
        String str = requireStringValue(con, path);
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            throw new ConfigLoadException("key \"" + path + "\" is not a Integer.");
        }
    }

    public static <T extends Enum> T requireEnumValue(Configuration con, String path, Class<T> e) throws ConfigLoadException {
        String str = requireStringValue(con, path);
        try {
            T ret = (T) Enum.valueOf(e, str);
            return ret;
        } catch (IllegalArgumentException ex) {
            throw new ConfigLoadException("possible values for " + path + " are " + Arrays.stream(e.getEnumConstants()).map(entry -> entry.name())
                    .collect(Collectors.joining(", ")));
        }
    }

    public static boolean requireBooleanValue(Configuration con, String path) throws ConfigLoadException {
        String str = requireStringValue(con, path);
        if (str.equals("true")) {
            return true;
        } else if (str.equals("false")) {
            return false;
        }
        throw new ConfigLoadException("key \"" + path + "\" is not a Boolean.");
    }
}
