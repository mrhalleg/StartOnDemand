package halleg.bungee.commands;

import halleg.bungee.plugin.BungeePlugin;
import halleg.bungee.server.ServerProcess;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class JoinServerCommand extends Command implements TabExecutor {
    public JoinServerCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length != 1) {
            commandSender.sendMessage(ChatColor.RED + "Incorrect command usage.");
            return;
        }

        ServerProcess proc = BungeePlugin.getInstance().getManager().getProcess(strings[0]);
        if (proc != null) {
            if (commandSender instanceof ProxiedPlayer) {
                proc.joinRequest((ProxiedPlayer) commandSender);
            }
        } else {
            commandSender.sendMessage(ChatColor.RED + "Server not found.");
            return;
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        if (strings.length == 1) {
            return BungeePlugin.getInstance().getManager().getProcessMap().values().stream().map(proc -> {
                return proc.getSettings().getName();
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
