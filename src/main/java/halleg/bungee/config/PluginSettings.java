package halleg.bungee.config;

import halleg.bungee.plugin.BungeePlugin;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PluginSettings {
    private static final String START_COMMAND_KEY = "screen-start-command";
    private static final String STOP_COMMAND_KEY = "screen-stop-command";
    private static final String RECONNECT_COMMAND_KEY = "screen-reconnect-command";

    public static String FILE_NAME = "config.yml";

    private Map<String, ServerSettings> settings;
    private String startCommand;
    private String stopCommand;
    private String reconnectCommand;

    public PluginSettings() throws ConfigLoadException {
        this.settings = new HashMap<>();
        BungeePlugin plugin = BungeePlugin.getInstance();
        Configuration configuration = loadConfiguration(plugin);
        initPluginSettings(plugin, configuration);
        initServerSettings(plugin, configuration);
    }

    private void initPluginSettings(BungeePlugin plugin, Configuration configuration) throws ConfigLoadException {
        this.startCommand = SettingsTools.requireStringValue(configuration, START_COMMAND_KEY);
        this.stopCommand = SettingsTools.requireStringValue(configuration, STOP_COMMAND_KEY);
        this.reconnectCommand = SettingsTools.requireStringValue(configuration, RECONNECT_COMMAND_KEY);
    }

    private void initServerSettings(BungeePlugin plugin, Configuration configuration) throws ConfigLoadException {
        try {
            Configuration servers = configuration.getSection("servers");
            for (String s : servers.getKeys()) {
                try {
                    ServerSettings settings = new ServerSettings(servers.getSection(s), s);
                    this.settings.put(s, settings);
                } catch (ConfigLoadException e) {
                    throw new ConfigLoadException("could not load server-config for " + s + ": " + e.getMessage());
                }
            }
        } catch (ClassCastException e) {
            throw new ConfigLoadException("ClassCastException " + e.getStackTrace()[0].getLineNumber());
        }
    }

    private Configuration loadConfiguration(Plugin plugin) {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
            plugin.getLogger().info("created data-folder.");
        }

        File file = new File(plugin.getDataFolder(), FILE_NAME);


        Configuration configuration = getDefaultConfiguration();
        if (file.exists()) {
            plugin.getLogger().info("found existing config.");
            try {
                configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).
                        load(file);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            plugin.getLogger().info("config not found, creating default.");
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, file);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return configuration;
    }

    private Configuration getDefaultConfiguration() {
        Configuration configuration = new Configuration();
        configuration.set("servers", new Configuration());
        return configuration;
    }

    public ServerSettings getConfig(String server) {
        return this.settings.get(server);
    }

    public Collection<ServerSettings> getValues() {
        return this.settings.values();
    }

    public String getStartCommand() {
        return this.startCommand;
    }

    public String getStopCommand() {
        return this.stopCommand;
    }

    public String getReconnectCommand() {
        return this.reconnectCommand;
    }
}
