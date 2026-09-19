package net.tfminecraft.tfmccore.commands;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.PermissionNode;

public final class SilentPermissionCommand implements CommandExecutor, TabCompleter {

    private static final String PERMISSION = "tfmccore.silentpermission";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(PERMISSION)) {
            return true;
        }
        if (args.length < 3) {
            return true;
        }

        String playerName = args[0];
        String node = args[1];
        Boolean value = parseBool(args[2]);
        if (value == null || node.isBlank()) {
            return true;
        }

        LuckPerms api = luckPerms();
        if (api == null) {
            return true;
        }

        boolean granted = value.booleanValue();
        resolveUuid(api, playerName).thenAccept(uuid -> {
            if (uuid == null) {
                return;
            }
            api.getUserManager().modifyUser(uuid, user -> apply(user, node, granted));
        });
        return true;
    }

    private static void apply(User user, String permission, boolean value) {
        user.data().clear(NodeType.PERMISSION.predicate(n ->
                n.getPermission().equalsIgnoreCase(permission) && n.getContexts().isEmpty()));
        user.data().add(PermissionNode.builder(permission).value(value).build());
    }

    private static CompletableFuture<UUID> resolveUuid(LuckPerms api, String name) {
        Player online = Bukkit.getPlayerExact(name);
        if (online != null) {
            return CompletableFuture.completedFuture(online.getUniqueId());
        }
        try {
            return CompletableFuture.completedFuture(UUID.fromString(name));
        } catch (IllegalArgumentException ignored) {
            return api.getUserManager().lookupUniqueId(name);
        }
    }

    private static LuckPerms luckPerms() {
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") == null) {
            return null;
        }
        RegisteredServiceProvider<LuckPerms> provider =
                Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        return provider == null ? null : provider.getProvider();
    }

    private static Boolean parseBool(String raw) {
        String s = raw.toLowerCase(Locale.ROOT);
        if (s.equals("true") || s.equals("yes") || s.equals("1")) {
            return true;
        }
        if (s.equals("false") || s.equals("no") || s.equals("0")) {
            return false;
        }
        return null;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!sender.hasPermission(PERMISSION)) {
            return List.of();
        }
        if (args.length == 1) {
            String prefix = args[0].toLowerCase(Locale.ROOT);
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(n -> n.toLowerCase(Locale.ROOT).startsWith(prefix))
                    .collect(Collectors.toList());
        }
        if (args.length == 3) {
            String prefix = args[2].toLowerCase(Locale.ROOT);
            return List.of("true", "false").stream()
                    .filter(s -> s.startsWith(prefix))
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}
