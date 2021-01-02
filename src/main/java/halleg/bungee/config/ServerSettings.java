package halleg.bungee.config;


import halleg.bungee.plugin.BungeePlugin;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.config.Configuration;

import java.io.File;

public class ServerSettings {
    private ServerInfo info;
    private String sessionName;
    private String start;
    private String stop;
    private File work;
    private boolean autoStartEnable;
    private boolean autoStopDisable;
    private boolean autoStartConnect;
    private int autoStartConnectDelay;
    private int autoStartConnectAttempts;
    private int autoStartConnectPeriod;
    private int autoStopEmpty;
    private boolean autoRestart;
    private boolean autoReconnect;

    public static final String SESSION_NAME_KEY = "session-name";
    public static final String START_PATH_KEY = "start";
    public static final String STOP_PATH_KEY = "stop";
    public static final String WORK_PATH_KEY = "work";
    public static final String AUTO_START_ENABLE_KEY = "auto-start-enable";
    public static final String AUTO_STOP_DISABLE_KEY = "auto-stop-disable";
    public static final String AUTO_START_CONNECT_KEY = "auto-start-connect";
    public static final String AUTO_START_CONNECT_DELAY_KEY = "auto-start-connect-delay";
    public static final String AUTO_START_CONNECT_PERIOD_KEY = "auto-start-connect-period";
    public static final String AUTO_START_CONNECT_ATTEMPTS_KEY = "auto-start-connect-attempts";
    public static final String AUTO_STOP_EMPTY_KEY = "auto-stop-empty";
    public static final String AUTO_RESTART_KEY = "auto-restart";
    public static final String AUTO_RECONNECT_KEY = "auto-reconnect";

    public ServerSettings(Configuration con, String name) throws ConfigLoadException {
        this.info = BungeePlugin.getInstance().getProxy().getServers().get(name);
        if (this.info == null) {
            throw new ConfigLoadException("Server " + name + " not found.");
        }
        this.sessionName = SettingsTools.requireStringValue(con, SESSION_NAME_KEY);
        this.start = SettingsTools.requireStringValue(con, START_PATH_KEY);
        this.stop = SettingsTools.requireStringValue(con, STOP_PATH_KEY);
        this.work = SettingsTools.requireFileValue(con, WORK_PATH_KEY);
        this.autoStartEnable = SettingsTools.requireBooleanValue(con, AUTO_START_ENABLE_KEY);
        this.autoStartConnect = SettingsTools.requireBooleanValue(con, AUTO_START_CONNECT_KEY);
        this.autoStartConnectDelay = SettingsTools.requireIntValue(con, AUTO_START_CONNECT_DELAY_KEY);
        this.autoStartConnectAttempts = SettingsTools.requireIntValue(con, AUTO_START_CONNECT_ATTEMPTS_KEY);
        this.autoStartConnectPeriod = SettingsTools.requireIntValue(con, AUTO_START_CONNECT_PERIOD_KEY);
        this.autoStopDisable = SettingsTools.requireBooleanValue(con, AUTO_STOP_DISABLE_KEY);
        this.autoStopEmpty = SettingsTools.requireIntValue(con, AUTO_STOP_EMPTY_KEY);
        this.autoRestart = SettingsTools.requireBooleanValue(con, AUTO_RESTART_KEY);
        this.autoReconnect = SettingsTools.requireBooleanValue(con, AUTO_RECONNECT_KEY);
    }

    public ServerInfo getInfo() {
        return this.info;
    }

    public String getName() {
        return this.info.getName();
    }

    public String getSessionName() {
        return this.sessionName;
    }

    public String getStart() {
        return this.start;
    }

    public String getStop() {
        return this.stop;
    }

    public File getWork() {
        return this.work;
    }

    public boolean isAutoStartEnable() {
        return this.autoStartEnable;
    }

    public boolean isAutoStopDisable() {
        return this.autoStopDisable;
    }

    public int getAutoStartConnectDelay() {
        return this.autoStartConnectDelay;
    }

    public long getAutoStartConnectPeriod() {
        return this.autoStartConnectPeriod;
    }

    public int getAutoStartConnectAttempts() {
        return this.autoStartConnectAttempts;
    }

    public int getAutoStopEmpty() {
        return this.autoStopEmpty;
    }

    public boolean isAutoStopEmpty() {
        return this.autoStopEmpty > 0;
    }

    public boolean isAutoRestart() {
        return this.autoRestart;
    }

    public boolean isAutoReconnect() {
        return this.autoReconnect;
    }

    public boolean isAutoStartConnect() {
        return this.autoStartConnect;
    }
}
