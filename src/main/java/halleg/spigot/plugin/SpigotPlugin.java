package halleg.spigot.plugin;

import org.bukkit.plugin.java.JavaPlugin;

public class SpigotPlugin extends JavaPlugin {
    private static SpigotPlugin plugin;

    public SpigotPlugin() throws Exception {
        if (plugin != null) {
            throw new Exception();
        }
        plugin = this;
    }

    public static SpigotPlugin getInstance() {
        return plugin;
    }

    ShutdownManager manager;

    @Override
    public void onEnable() {
        getLogger().info("Loading config...");

        getConfig().addDefault("stop-after", 60);
        getConfig().options().copyDefaults(true);
        saveConfig();

        int time = this.getConfig().getInt("stop-after");
        this.manager = new ShutdownManager(time);
        this.manager.updateTimer(0);
        getServer().getPluginManager().registerEvents(this.manager, this);
    }

    public void shutdown() {
        getLogger().info("Shutting down server due to inactivity.");
        getServer().shutdown();
    }
}
