package jiekie.completer;

import jiekie.TeleportPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WarpTicketTabCompleter implements TabCompleter {
    private final TeleportPlugin plugin;

    public WarpTicketTabCompleter(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("jk.warp_ticket.command")) return Collections.emptyList();
        if(!(sender instanceof Player)) return Collections.emptyList();

        if(args.length == 1)
            return Arrays.asList("템플릿", "등록", "받기", "도움말");

        if(args.length == 2 && (args[0].equals("등록") || args[0].equals("받기")))
            return plugin.getLocationManager().getAllLocationNames();

        if(args.length == 3 && args[0].equals("등록"))
            return plugin.getWarpTicketManager().getTemplateNames();

        return Collections.emptyList();
    }
}
