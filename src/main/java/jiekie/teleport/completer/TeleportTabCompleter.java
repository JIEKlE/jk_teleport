package jiekie.teleport.completer;

import jiekie.nickname.api.NicknameAPI;
import jiekie.teleport.TeleportPlugin;
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
        if(!(sender instanceof Player player)) return Collections.emptyList();
        Block targetBlock = player.getTargetBlockExact(10);

        if(args.length == 1)
            return Arrays.asList("나침반", "장소설정", "권한설정", "장소제거", "이동", "정보", "도움말");

        String commandType = args[0];
        if(args.length == 2) {
            if(commandType.equals("권한설정") || commandType.equals("장소제거") || commandType.equals("이동") || commandType.equals("정보"))
                return plugin.getLocationManager().getAllLocationNames();
        }

        if(args.length == 3) {
            if(commandType.equals("이동"))
                return NicknameAPI.getInstance().getPlayerNameAndNicknameList();

            if(commandType.equals("장소설정")) {
                if(targetBlock != null)
                    return List.of(String.valueOf(targetBlock.getX()));
            }

            if(commandType.equals("권한설정"))
                return List.of("영어권한명");
        }

        if(args.length == 4) {
            if(commandType.equals("장소설정")) {
                if(targetBlock != null)
                    return List.of(String.valueOf(targetBlock.getY()));
            }

            if(commandType.equals("권한설정"))
                return List.of("한글권한명");
        }

        if(args.length == 5 && commandType.equals("장소설정")) {
            if(targetBlock != null)
                return List.of(String.valueOf(targetBlock.getZ()));
        }

        return Collections.emptyList();
    }
}
