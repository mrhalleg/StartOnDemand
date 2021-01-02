package halleg.bungee.server;

import halleg.bungee.config.ServerSettings;
import halleg.bungee.plugin.BungeePlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;

public class ServerManager {
    private Map<String, ServerProcess> processMap;
    private Map<String, ConnectJob> connectJobs;
    private Timer timer;

    public ServerManager() {
        this.timer = new Timer();
        this.processMap = new HashMap<>();
        this.connectJobs = new HashMap<>();
        for (ServerSettings settings : BungeePlugin.getInstance().getSettings().getValues()) {
            ServerProcess process = new ServerProcess(settings, this.timer);
            this.processMap.put(settings.getName(), process);
        }
    }

    public void onDisable() {
        BungeePlugin.getInstance().getLogger().info("stopping all servers onDisable");
        for (ServerProcess proc : this.processMap.values()) {
            proc.autoStopDisable();
        }
    }


    public void onEnable() {
        String cmd = ScreenBuilder.reconnectCommand();
        try {
            BungeePlugin.getInstance().getLogger().info("executing reconnectcommand " + cmd);
            Process p = Runtime.getRuntime().exec(cmd);
            p.onExit().thenAccept(process1 -> {
                Scanner scanner = new Scanner(p.getInputStream()).useDelimiter("\\A");
                String result = scanner.hasNext() ? scanner.next() : "";

                BungeePlugin.getInstance().getLogger().info("reconnecting all servers onEnable");
                for (ServerProcess proc : this.processMap.values()) {
                    proc.autoReconnect(getPid(result, proc.getSettings().getSessionName()));
                }

                BungeePlugin.getInstance().getLogger().info("starting all servers onEnable");
                for (ServerProcess proc : this.processMap.values()) {
                    proc.autoStartEnable();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getPid(String result, String session) {
        String matchingLine = null;
        for (String s : result.split("\n")) {
            if (s.contains(session)) {
                if (matchingLine != null) {
                    return -1;
                }
                matchingLine = s;
            }
        }

        if (matchingLine == null) {
            return -1;
        }

        matchingLine = matchingLine.substring(0, matchingLine.indexOf("."));
        matchingLine = matchingLine.trim();
        BungeePlugin.getInstance().getLogger().info("extracted pid for " + session + " is " + matchingLine);
        try {
            return Integer.parseInt(matchingLine);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void newConnectJob(ConnectJob job) {
        if (this.connectJobs.get(job.getName()) != null) {
            this.connectJobs.get(job.getName()).stop();
        }
        this.connectJobs.put(job.getName(), job);
    }

    public ServerProcess getProcess(String string) {
        return this.processMap.get(string);
    }

    void removeConnectJob(String name) {
        this.connectJobs.remove(name);
    }

    public int getConnectJobsSize() {
        return this.connectJobs.size();
    }

    public Map<String, ServerProcess> getProcessMap() {
        return this.processMap;
    }

    public Timer getTimer() {
        return this.timer;
    }

    public ConnectJob getConnectJob(String name) {
        return this.connectJobs.get(name);
    }
}
