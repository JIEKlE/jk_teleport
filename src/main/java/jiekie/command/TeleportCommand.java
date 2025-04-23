package jiekie.command;

import jiekie.TeleportPlugin;
import jiekie.api.NicknameAPI;
import jiekie.util.ChatUtil;
import jiekie.util.GuiUtil;
import jiekie.util.SoundUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class TeleportCommand implements CommandExecutor {
    private final TeleportPlugin plugin;

    public TeleportCommand(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player && !sender.isOp()) {
            ChatUtil.notOp(sender);
            return true;
        }

        if(args == null || args.length == 0) {
            ChatUtil.commandHelper(sender);
            return true;
        }

        switch (args[0]) {
            case "나침반":
                getCompass(sender);
                break;

            case "설정":
                setLocation(sender, args);
                break;

            case "제거":
                removeLocation(sender, args);
                break;

            case "이동":
                move(sender, args);
                break;

            case "도움말":
                ChatUtil.commandList(sender);
                break;

            default:
                ChatUtil.commandHelper(sender);
                break;
        }

        return true;
    }

    /* 나침반 */
    public void getCompass(CommandSender sender) {
        if(!(sender instanceof Player)) {
            ChatUtil.notPlayer(sender);
            return;
        }

        // 인벤토리 부족
        Player player = (Player) sender;
        PlayerInventory inventory = player.getInventory();
        if(inventory.firstEmpty() == -1) {
            ChatUtil.inventoryFull(player);
            return;
        }

        // 나침반 생성
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta meta = compass.getItemMeta();

        meta.setDisplayName(GuiUtil.COMPASS_NAME);
        meta.addEnchant(Enchantment.LUCK, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        compass.setItemMeta(meta);
        
        // 나침반 지급
        inventory.addItem(compass);
        ChatUtil.getCompass(player);
        SoundUtil.playNoteBlockBell(player);
    }

    /* 설정 */
    public void setLocation(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            ChatUtil.notPlayer(sender);
            return;
        }

        Player player = (Player) sender;
        if(args.length != 5 && args.length != 7) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/텔레포트 설정 장소명 x y z [yaw] [pitch])");
            return;
        }

        try {
            String name = args[1];
            World world = player.getWorld();
            int x = Integer.parseInt(args[2]);
            int y = Integer.parseInt(args[3]);
            int z = Integer.parseInt(args[4]);
            int yaw = 0;
            int pitch = 0;

            if(args.length == 7) {
                yaw = Integer.parseInt(args[5]);
                pitch = Integer.parseInt(args[6]);
            }

            if(plugin.getLocationManager().exists(name))
                ChatUtil.locationIsChanged(player);
            else
                ChatUtil.locationIsSaved(player);

            SoundUtil.playNoteBlockBell(player);

            Location location = new Location(world, x, y, z, yaw, pitch);
            plugin.getLocationManager().setLocation(name, location);

        } catch (NumberFormatException e) {
            ChatUtil.coordinatesNotNumber(player);
        }
    }

    /* 제거 */
    public void removeLocation(CommandSender sender, String[] args) {
        if(!(sender instanceof Player)) {
            ChatUtil.notPlayer(sender);
            return;
        }

        Player player = (Player) sender;
        if(args.length < 2) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/텔레포트 제거 장소명)");
            return;
        }

        // 장소 정보 없음
        String name = args[1];
        if(!plugin.getLocationManager().exists(name)) {
            ChatUtil.isNotRegisteredLocation(player);
            return;
        }

        // 장소 정보 제거
        plugin.getLocationManager().removeLocation(name);
        ChatUtil.locationIsRemoved(player);
        SoundUtil.playNoteBlockBell(player);
    }

    /* 이동 */
    public void move(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(ChatUtil.wrongCommand() + " (/텔레포트 이동 장소명 [플레이어ID|닉네임])");
            return;
        }

        // 장소 정보 없음
        String name = args[1];
        if(!plugin.getLocationManager().exists(name)) {
            ChatUtil.isNotRegisteredLocation(sender);
            return;
        }

        // 장소로 이동
        Location location = plugin.getLocationManager().getLocation(name);
        if(args.length == 2) {
            if(!(sender instanceof Player)) {
                ChatUtil.notPlayer(sender);
                return;
            }

            Player player = (Player) sender;
            player.teleport(location);
            SoundUtil.playTeleport(player);
            return;
        }

        // 플레이어 텔레포트
        String targetPlayerName = getContents(args, 2);
        Player targetPlayer = NicknameAPI.getInstance().getPlayerByNameOrNickname(targetPlayerName);
        if(targetPlayer == null) {
            ChatUtil.playerDoesNotExist(sender);
            return;
        }

        targetPlayer.teleport(location);
        SoundUtil.playTeleport(targetPlayer);

        ChatUtil.movePlayerToWorld(sender);
        if(sender instanceof Player)
            SoundUtil.playNoteBlockBell((Player) sender);
    }

    private String getContents(String[] args, int startIndex) {
        StringBuffer sb = new StringBuffer();
        for(int i = startIndex ; i < args.length ; i++) {
            if(i != startIndex)
                sb.append(" ");
            sb.append(args[i]);
        }

        String contents = sb.toString();
        return ChatColor.translateAlternateColorCodes('&', contents);
    }
}
