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
            return Arrays.asList("템플릿등록", "템플릿제거", "등록", "해제", "받기", "도움말");

        String commandType = args[0];
        if(args.length == 2) {
            if((commandType.equals("등록") || commandType.equals("해제")) || commandType.equals("받기"))
                return plugin.getLocationManager().getAllLocationNames();
            if(commandType.equals("템플릿제거"))
                return plugin.getWarpTicketManager().getTemplateNames();
        }

        if(args.length == 3 && commandType.equals("등록"))
            return plugin.getWarpTicketManager().getTemplateNames();

        return Collections.emptyList();
    }
}
