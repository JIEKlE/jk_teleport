package jiekie.completer;

import jiekie.TeleportPlugin;
import jiekie.api.NicknameAPI;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TeleportTabCompleter implements TabCompleter {
    private final TeleportPlugin plugin;

    public TeleportTabCompleter(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!sender.hasPermission("jk.teleport.command")) return Collections.emptyList();
        if(!(sender instanceof Player)) return Collections.emptyList();
        Player player = (Player) sender;
        Block targetBlock = player.getTargetBlockExact(10);

        if(args.length == 1)
            return Arrays.asList("나침반", "설정", "제거", "이동", "도움말");

        if(args.length == 2) {
            if(args[0].equals("제거") || args[0].equals("이동"))
                return plugin.getLocationManager().getAllLocationNames();
        }

        if(args.length == 3 && args[0].equals("이동"))
            return NicknameAPI.getInstance().getPlayerNameAndNicknameList();

        if(args.length == 3 && args[0].equals("설정")) {
            if(targetBlock != null)
                return List.of(String.valueOf(targetBlock.getX()));
        }

        if(args.length == 4 && args[0].equals("설정")) {
            if(targetBlock != null)
                return List.of(String.valueOf(targetBlock.getY()));
        }

        if(args.length == 5 && args[0].equals("설정")) {
            if(targetBlock != null)
                return List.of(String.valueOf(targetBlock.getZ()));
        }

        return Collections.emptyList();
    }
}
