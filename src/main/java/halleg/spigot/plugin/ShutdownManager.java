package halleg.spigot.plugin;

import halleg.tools.timer.Timer;
import halleg.tools.timer.TimerCallback;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ShutdownManager implements Listener, TimerCallback {
    private Timer timer;

    public ShutdownManager(long time) {
        SpigotPlugin.getInstance().getLogger().info("Creating timer with " + time + "s");
        this.timer = new Timer(this, time);
    }

    public void updateTimer(int count) {
        String msg = "Updating Timer. Online players: ";
        for (Player p : SpigotPlugin.getInstance().getServer().getOnlinePlayers()) {
            msg += p.getDisplayName() + " ";
        }
        SpigotPlugin.getInstance().getLogger().info(msg);
        if (count <= 0) {
            SpigotPlugin.getInstance().getLogger().info("Starting timer");
            this.timer.start();
        } else {
            SpigotPlugin.getInstance().getLogger().info("Stopping timer");
            this.timer.stop();
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        updateTimer(SpigotPlugin.getInstance().getServer().getOnlinePlayers().size() - 1);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        updateTimer(SpigotPlugin.getInstance().getServer().getOnlinePlayers().size());
    }

    @Override
    public void onTimerEnd() {
        int count = SpigotPlugin.getInstance().getServer().getOnlinePlayers().size();

        if (count <= 0) {
            SpigotPlugin.getInstance().getLogger().info("Timer ran out and server is empty");
            SpigotPlugin.getInstance().shutdown();
        } else {
            SpigotPlugin.getInstance().getLogger().info("Timer ran out but server is not empty");
        }
    }
}
