package jiekie.command;

import jiekie.TeleportPlugin;
import jiekie.api.NicknameAPI;
import jiekie.manager.LocationManager;
import jiekie.model.LocationData;
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
            ChatUtil.teleportCommandHelper(sender);
            return true;
        }

        switch (args[0]) {
            case "나침반":
                getCompass(sender);
                break;

            case "장소설정":
                setLocation(sender, args);
                break;

            case "권한설정":
                setPermission(sender, args);
                break;

            case "장소제거":
                removeLocation(sender, args);
                break;

            case "이동":
                move(sender, args);
                break;

            case "정보":
                showLocationInfo(sender, args);
                break;

            case "도움말":
                ChatUtil.teleportCommandList(sender);
                break;

            default:
                ChatUtil.teleportCommandHelper(sender);
                break;
        }

        return true;
    }

    /* 나침반 */
    public void getCompass(CommandSender sender) {
        if(!(sender instanceof Player player)) {
            ChatUtil.notPlayer(sender);
            return;
        }

        // 인벤토리 부족
        PlayerInventory inventory = player.getInventory();
        if(inventory.firstEmpty() == -1) {
            ChatUtil.showMessage(player, ChatUtil.INVENTORY_FULL);
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
        ChatUtil.showMessage(player, ChatUtil.GET_COMPASS);
        SoundUtil.playNoteBlockBell(player);
    }

    /* 장소 설정 */
    public void setLocation(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            ChatUtil.notPlayer(sender);
            return;
        }

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
                ChatUtil.showMessage(player, ChatUtil.LOCATION_IS_CHANGED);
            else
                ChatUtil.showMessage(player, ChatUtil.LOCATION_IS_SAVED);

            SoundUtil.playNoteBlockBell(player);

            Location location = new Location(world, x, y, z, yaw, pitch);
            plugin.getLocationManager().setLocation(name, location);

        } catch (NumberFormatException e) {
            ChatUtil.showMessage(player, ChatUtil.COORDINATES_NOT_NUMBER);
        }
    }

    /* 권한 설정 */
    public void setPermission(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            ChatUtil.notPlayer(sender);
            return;
        }

        if(args.length < 2) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/텔레포트 권한설정 장소명 영어권한명 한글권한명)");
            return;
        }

        // 장소 정보 없음
        String name = args[1];
        if(!plugin.getLocationManager().exists(name)) {
            ChatUtil.showMessage(player, ChatUtil.IS_NOT_REGISTERED_LOCATION);
            return;
        }

        // 권한 설정
        boolean setPermission = args.length >= 4;
        String englishPermission = args.length >= 4 ? args[2] : null;
        String koreanPermission = args.length >= 4 ? args[3] : null;
        plugin.getLocationManager().setPermission(name, englishPermission, koreanPermission);

        if(setPermission)
            ChatUtil.showMessage(player, ChatUtil.SET_PERMISSION);
        else
            ChatUtil.showMessage(player, ChatUtil.RESET_PERMISSION);
        
        SoundUtil.playNoteBlockBell(player);
    }

    /* 장소 제거 */
    public void removeLocation(CommandSender sender, String[] args) {
        if(!(sender instanceof Player player)) {
            ChatUtil.notPlayer(sender);
            return;
        }

        if(args.length < 2) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/텔레포트 제거 장소명)");
            return;
        }

        // 장소 정보 없음
        String name = args[1];
        if(!plugin.getLocationManager().exists(name)) {
            ChatUtil.showMessage(player, ChatUtil.IS_NOT_REGISTERED_LOCATION);
            return;
        }

        // 장소 정보 제거
        plugin.getLocationManager().removeLocation(name);
        ChatUtil.showMessage(player, ChatUtil.LOCATION_IS_REMOVED);
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
            ChatUtil.showMessage(sender, ChatUtil.IS_NOT_REGISTERED_LOCATION);
            return;
        }

        // 이동 대상 설정
        Player targetPlayer;
        if(args.length == 2) {
            if(!(sender instanceof Player player)) {
                ChatUtil.notPlayer(sender);
                return;
            }

            targetPlayer = player;

        } else {
            targetPlayer = NicknameAPI.getInstance().getPlayerByNameOrNickname(getContents(args, 2));

            if(targetPlayer == null) {
                ChatUtil.showMessage(sender, ChatUtil.PLAYER_DOES_NOT_EXIST);
                return;
            }
        }

        // 권한 체크
        LocationManager locationManager = plugin.getLocationManager();
        if(!locationManager.playerHasPermission(name, targetPlayer)) {
            ChatUtil.noPermission(targetPlayer, locationManager.getPermissionName(name));
            return;
        }

        // 텔레포트
        targetPlayer.teleport(locationManager.getLocation(name));
        SoundUtil.playTeleport(targetPlayer);

        if(sender instanceof Player) {
            ChatUtil.showMessage(sender, ChatUtil.MOVE_PLAYER_TO_LOCATION);
            SoundUtil.playNoteBlockBell((Player) sender);
        }
    }

    /* 정보 */
    private void showLocationInfo(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(ChatUtil.wrongCommand() + " (/텔레포트 정보 장소명)");
            return;
        }

        // 장소 정보 없음
        String name = args[1];
        if(!plugin.getLocationManager().exists(name)) {
            ChatUtil.showMessage(sender, ChatUtil.IS_NOT_REGISTERED_LOCATION);
            return;
        }

        LocationData locationData = plugin.getLocationManager().getLocationData(name);
        ChatUtil.locationInfoPrefix(sender);
        ChatUtil.locationInfo(sender, locationData);
        ChatUtil.horizontalLineSuffix(sender);

        if(sender instanceof Player)
            SoundUtil.playNoteBlockBell((Player) sender);
    }

    private String getContents(String[] args, int startIndex) {
        StringBuilder sb = new StringBuilder();
        for(int i = startIndex ; i < args.length ; i++) {
            if(i != startIndex)
                sb.append(" ");
            sb.append(args[i]);
        }

        String contents = sb.toString();
        return ChatColor.translateAlternateColorCodes('&', contents);
    }
}
