package halleg.bungee.server;

import halleg.bungee.plugin.BungeePlugin;

public class ScreenBuilder {
    public static String startCommand(String start, String sessionName) {
        String cmd = BungeePlugin.getInstance().getSettings().getStartCommand();
        cmd = cmd.replace("{session-name}", sessionName);
        cmd = cmd.replace("{start}", start);
        return cmd;
    }

    public static String stopCommand(String stop, String sessionName) {
        String cmd = BungeePlugin.getInstance().getSettings().getStopCommand();
        cmd = cmd.replace("{session-name}", sessionName);
        cmd = cmd.replace("{stop}", stop);
        return cmd;
    }

    public static String reconnectCommand() {
        String cmd = BungeePlugin.getInstance().getSettings().getReconnectCommand();
        return cmd;
    }
}
