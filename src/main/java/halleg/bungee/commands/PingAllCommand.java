package halleg.bungee.commands;

import halleg.bungee.plugin.BungeePlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Command;

public class PingAllCommand extends Command {
    public PingAllCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (strings.length != 0) {
            commandSender.sendMessage(ChatColor.RED + "Incorrect command usage.");
            return;
        }
        commandSender.sendMessage("Pinging all Servers:");
        for (ServerInfo i : BungeePlugin.getInstance().getProxy().getServers().values()) {
            i.ping((serverPing, throwable) -> {
                if (throwable != null) {
                    commandSender.sendMessage(" successfully pinged " + ChatColor.GREEN + i.getName());
                } else {
                    commandSender.sendMessage(" failed to pinged " + ChatColor.RED + i.getName());
                }
            });
        }
    }
}
