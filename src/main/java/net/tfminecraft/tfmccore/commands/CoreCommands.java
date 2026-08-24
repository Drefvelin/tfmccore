package net.tfminecraft.tfmccore.commands;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CoreCommands implements CommandExecutor {
    public String cmd1 = "tfmc";

    private final StatsCommand statsCommand = new StatsCommand();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase(cmd1)) {
            return false;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("stats")) {
            return statsCommand.handle(sender, Arrays.copyOfRange(args, 1, args.length));
        }

        return true;
    }
}
