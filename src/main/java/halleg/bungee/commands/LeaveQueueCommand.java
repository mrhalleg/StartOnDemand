package halleg.bungee.commands;

import halleg.bungee.plugin.BungeePlugin;
import halleg.bungee.server.ConnectJob;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class LeaveQueueCommand extends Command {
    public LeaveQueueCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length != 0) {
            commandSender.sendMessage(ChatColor.RED + "Incorrect command usage.");
            return;
        }

        ConnectJob job = BungeePlugin.getInstance().getManager().getConnectJob(commandSender.getName());
        if (job != null) {
            job.stop();
        } else {
            commandSender.sendMessage("You are currently not in any queue.");
        }
    }
}
