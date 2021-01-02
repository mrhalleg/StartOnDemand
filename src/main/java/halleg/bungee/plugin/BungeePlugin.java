package halleg.bungee.plugin;

import halleg.bungee.commands.*;
import halleg.bungee.config.ConfigLoadException;
import halleg.bungee.config.PluginSettings;
import halleg.bungee.server.ServerManager;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeePlugin extends Plugin {
    private static BungeePlugin plugin;

    public BungeePlugin() throws Exception {
        if (plugin != null) {
            throw new Exception();
        }
        plugin = this;
    }

    public static BungeePlugin getInstance() {
        return plugin;
    }


    private PluginSettings settings;
    private EventListener listener;
    private ServerManager manager;

    @Override
    public void onEnable() {
        getLogger().info("Yay! It loads!");
        try {
            this.settings = new PluginSettings();
        } catch (ConfigLoadException e) {
            getLogger().warning("Failed to load " + PluginSettings.FILE_NAME + ": " + e.getMessage());
            getLogger().warning("Cant continue to enable.");
            return;
        }
        this.listener = new EventListener();
        this.manager = new ServerManager();
        getProxy().getPluginManager().registerCommand(this, new ServerStatusCommand("serverstatus"));
        getProxy().getPluginManager().registerCommand(this, new StartServerCommand("startserver"));
        getProxy().getPluginManager().registerCommand(this, new StopServersCommand("stopserver"));
        getProxy().getPluginManager().registerCommand(this, new ReconnectServerCommand("reconnectserver"));
        getProxy().getPluginManager().registerCommand(this, new DisconnectServerCommand("disconnectserver"));
        getProxy().getPluginManager().registerCommand(this, new JoinServerCommand("joinserver"));
        getProxy().getPluginManager().registerCommand(this, new LeaveQueueCommand("leavequeue"));
        getProxy().getPluginManager().registerCommand(this, new PingAllCommand("pingall"));
        getProxy().getPluginManager().registerListener(this, this.listener);
        this.manager.onEnable();
    }

    @Override
    public void onDisable() {
        this.manager.onDisable();
    }

    public PluginSettings getSettings() {
        return this.settings;
    }

    public ServerManager getManager() {
        return this.manager;
    }
}
