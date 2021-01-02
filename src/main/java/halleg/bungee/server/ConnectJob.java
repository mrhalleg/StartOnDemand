package halleg.bungee.server;

import halleg.bungee.plugin.BungeePlugin;
import halleg.bungee.timer.RestartableTimer;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ConnectJob implements Callback<Boolean>, Runnable {
    private ServerManager manager;
    private ProxiedPlayer player;
    private ServerInfo info;
    private RestartableTimer timer;
    private long delay;
    private int count;
    private int maxCount;
    private long period;

    public ConnectJob(ServerManager manager, ProxiedPlayer player, ServerInfo info, long delay, long period, int maxCount) {
        this.player = player;
        this.info = info;
        this.manager = manager;
        this.delay = delay;
        this.period = period;
        this.maxCount = maxCount;
        this.timer = new RestartableTimer(manager.getTimer(), () -> {
            run();
        });
    }

    public void start() {
        BungeePlugin.getInstance().getLogger().info("starting connect-job for " + this.player.getDisplayName() + " to " + this.info.getName());
        this.player.sendMessage("Please wait while " + this.info.getName() + " starts. You will be automatically connected. This process might show you some errors, this is normal. You can exit the queue with \"/leavequeue\"");
        this.count = 0;
        this.timer.startRepeat(this.delay, this.period);
    }

    public void stop() {
        BungeePlugin.getInstance().getLogger().info("stopping connect-job for " + this.player.getDisplayName() + " to " + this.info.getName());
        this.player.sendMessage("You have Exited the connect-queue for " + this.info.getName() + ".");
        this.timer.stop();
        this.manager.removeConnectJob(this.getName());
    }

    private boolean checkStop() {
        if (this.count >= this.maxCount) {
            BungeePlugin.getInstance().getLogger().info("connect-job for " + this.player.getDisplayName() + " to " + this.info.getName() + " reached max count of " + this.maxCount);
            stop();
            return true;
        } else if (this.player.getServer().getInfo().getName().equals(this.info.getName())) {
            BungeePlugin.getInstance().getLogger().info("successfully connected " + this.player.getDisplayName() + " to " + this.info.getName());
            stop();
            return true;
        }

        return false;
    }

    @Override
    public void run() {
        if (checkStop()) {
            return;
        }
        BungeePlugin.getInstance().getLogger().info("checking serverstatus for " + this.player.getDisplayName() + " to " + this.info.getName());
        this.player.sendMessage("Attempting to connect you to " + this.info.getName() + "...");
        this.player.connect(this.info, this);
        this.count++;
    }

    @Override
    public void done(Boolean a, Throwable throwable) {
        if (checkStop()) {
            this.player.connect(this.info);
            stop();
        } else {
            BungeePlugin.getInstance().getLogger().info("failed to connect " + this.player.getDisplayName() + " to " + this.info.getName());
        }
    }

    public String getName() {
        return this.player.getName();
    }
}
