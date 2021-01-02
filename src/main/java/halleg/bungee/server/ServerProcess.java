package halleg.bungee.server;

import halleg.bungee.config.ServerSettings;
import halleg.bungee.plugin.BungeePlugin;
import halleg.bungee.timer.RestartableTimer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Timer;

public class ServerProcess {
    private Timer timer;
    private RestartableTimer stopTask;

    private ServerSettings settings;

    private ProcessHandle process;

    private Boolean shouldBeRunning = false;

    public ServerProcess(ServerSettings settings, Timer timer) {
        this.settings = settings;
        this.process = null;
        this.timer = timer;
        this.stopTask = new RestartableTimer(timer, () -> {
            timerStop();
        });
    }

    public void playerCountUpdate() {
        if (this.settings.isAutoStopEmpty()) {
            int count = this.settings.getInfo().getPlayers().size();
            if (count <= 0) {
                BungeePlugin.getInstance().getLogger().info(this.settings.getName() + " is empty");
                this.stopTask.start(this.settings.getAutoStopEmpty() * 1000);
            } else {
                BungeePlugin.getInstance().getLogger().info(this.settings.getName() + " is not empty");
                this.stopTask.stop();
            }
        }
    }

    public void autoStartEnable() {
        if (this.settings.isAutoStartEnable()) {
            start();
        }
    }

    public void autoStopDisable() {
        if (this.settings.isAutoStopDisable()) {
            stop();
        }
    }

    public void autoReconnect(int pid) {
        if (this.settings.isAutoReconnect()) {
            reconnect(pid);
        }
    }

    public void disconnect() {
        this.process = null;
        this.stopTask.stop();
    }

    public void reconnect(int pid) {
        try {
            setProcess(ProcessHandle.of(pid).get());
            this.shouldBeRunning = true;
            BungeePlugin.getInstance().getLogger().info("successfully reconnected " + this.settings.getName() + " to process " + pid);
        } catch (NoSuchElementException e) {
            BungeePlugin.getInstance().getLogger().info("failed to reconnect " + this.settings.getName());

        }
    }

    public void start() {
        this.shouldBeRunning = true;
        if (!this.isAlive()) {
            String cmd = ScreenBuilder.startCommand(this.settings.getStart(), this.settings.getSessionName());
            BungeePlugin.getInstance().getLogger().info("starting server " + this.settings.getName() + "with command " + cmd + " in working directory " + this.settings.getWork().getAbsolutePath());
            try {
                setProcess(Runtime.getRuntime().exec(cmd, null, this.settings.getWork()).toHandle());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            BungeePlugin.getInstance().getLogger().info("cant start server " + this.settings.getName() + " because its already running");
        }

    }

    public void stop() {
        this.shouldBeRunning = false;
        if (this.isAlive()) {
            String cmd = ScreenBuilder.stopCommand(this.settings.getStop(), this.settings.getSessionName());
            BungeePlugin.getInstance().getLogger().info("stopping server " + this.settings.getName() + " with command " + cmd);
            try {
                if (this.process != null) {
                    Runtime.getRuntime().exec(cmd);
                    this.process = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            BungeePlugin.getInstance().getLogger().info("cant stop server " + this.settings.getName() + " because its not running");
        }
    }

    private void onProcessExit() {
        BungeePlugin.getInstance().getLogger().info("process for " + this.settings.getName() + " exited");
        if (this.settings.isAutoRestart() && this.shouldBeRunning) {
            BungeePlugin.getInstance().getLogger().info("automatically restarting " + this.settings.getName());
            start();
        }
    }

    private void timerStop() {
        BungeePlugin.getInstance().getLogger().info("stop-timer ended for " + this.settings.getName());
        stop();
    }

    public ServerSettings getSettings() {
        return this.settings;
    }

    public void joinRequest(ProxiedPlayer player) {
        if (this.settings.isAutoStartConnect()) {
            BungeePlugin.getInstance().getLogger().info(player.getDisplayName() + " requesting to connect to " + this.settings.getName());
            start();
            ServerManager manager = BungeePlugin.getInstance().getManager();
            ConnectJob job = new ConnectJob(manager, player, this.settings.getInfo(), this.settings.getAutoStartConnectDelay() * 1000, this.settings.getAutoStartConnectPeriod() * 1000, this.settings.getAutoStartConnectAttempts());
            manager.newConnectJob(job);
            job.start();
        }
    }

    private void setProcess(ProcessHandle p) {
        this.process = p;
        this.process.onExit().thenAccept(process1 -> {
            onProcessExit();
        });
        if (p.isAlive()) {
            playerCountUpdate();
        }
    }

    public boolean isAlive() {
        if (this.process != null) {
            return this.process.isAlive();
        } else {
            return false;
        }
    }

    public ProcessHandle getProcess() {
        return this.process;
    }
}
