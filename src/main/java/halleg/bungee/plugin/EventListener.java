package halleg.bungee.plugin;

import halleg.bungee.server.ServerProcess;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventListener implements Listener {

    public EventListener() {
    }

    @EventHandler
    public void onServerConnectEvent(ServerConnectEvent e) {
        ServerProcess proc = BungeePlugin.getInstance().getManager().getProcess(e.getTarget().getName());
        if (proc != null && !proc.isAlive()) {
            proc.joinRequest(e.getPlayer());
        }
    }

    @EventHandler
    public void onServerConnectedEvent(ServerConnectedEvent event) {
        ServerProcess proc = BungeePlugin.getInstance().getManager().getProcess(event.getServer().getInfo().getName());
        if (proc != null) {
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                proc.playerCountUpdate();
            });
            t.start();
        }
    }

    @EventHandler
    public void onServerDisconnectEvent(ServerDisconnectEvent event) {
        ServerProcess proc = BungeePlugin.getInstance().getManager().getProcess(event.getTarget().getName());
        if (proc != null) {
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                proc.playerCountUpdate();
            });
            t.start();
        }
    }
}
