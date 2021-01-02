package halleg.bungee.commands;

import halleg.bungee.plugin.BungeePlugin;
import halleg.bungee.server.ServerProcess;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ServerStatusCommand extends Command {
    public ServerStatusCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length != 0) {
            commandSender.sendMessage(ChatColor.RED + "Incorrect command usage.");
            return;
        }

        String msg = "[StartOnDemmand] Servers:\n";
        for (ServerProcess proc : BungeePlugin.getInstance().getManager().getProcessMap().values()) {
            msg += ChatColor.WHITE + " - " + proc.getSettings().getName();
            if (proc.isAlive()) {
                msg += ChatColor.GREEN + " [ALIVE]";
                msg += ChatColor.WHITE + " (pid=" + proc.getProcess().pid() + ")";
            } else {
                msg += ChatColor.RED + " [DEAD]";
            }
            msg += ChatColor.YELLOW + " playercount: " + proc.getSettings().getInfo().getPlayers().size();
            msg += "\n";
        }
        msg += ChatColor.WHITE + "currently running connect-jobs: " + BungeePlugin.getInstance().getManager().getConnectJobsSize();
        commandSender.sendMessage(msg);
    }
}
