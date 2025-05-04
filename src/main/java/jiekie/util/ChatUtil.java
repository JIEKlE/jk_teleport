package jiekie.util;

import jiekie.model.LocationData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class ChatUtil {
    /* error */
    public static final String NO_ITEM = getXPrefix() + "손에 아이템을 들고 설정해주시기 바랍니다.";
    public static final String INVENTORY_FULL = getXPrefix() + "인벤토리가 가득 찼습니다. 인벤토리를 1칸 이상 비워주시기 바랍니다.";
    public static final String IS_NOT_REGISTERED_LOCATION = getXPrefix() + "장소에 대한 정보를 찾을 수 없습니다.";
    public static final String COORDINATES_NOT_NUMBER = getXPrefix() + "좌표 값은 숫자여야 합니다.";
    public static final String PLAYER_DOES_NOT_EXIST = getXPrefix() + "해당 이름을 가진 플레이어가 없습니다.";
    public static final String TEMPLATE_NOT_REGISTERED = getXPrefix() + "템플릿에 대한 정보를 찾을 수 없습니다.";
    public static final String WARP_TICKET_NOT_REGISTERED = getXPrefix() + "이동권이 등록되지 않았습니다.";

    /* feedback */
    public static final String GET_COMPASS = getCheckPrefix() + "텔레포트 나침반을 받았습니다.";
    public static final String LOCATION_IS_SAVED = getCheckPrefix() + "장소 정보를 등록했습니다.";
    public static final String LOCATION_IS_CHANGED = getCheckPrefix() + "장소 정보를 수정했습니다.";
    public static final String SET_PERMISSION = getCheckPrefix() + "권한을 설정했습니다.";
    public static final String RESET_PERMISSION = getCheckPrefix() + "권한을 해제했습니다.";
    public static final String LOCATION_IS_REMOVED = getCheckPrefix() + "장소 정보를 제거했습니다.";
    public static final String MOVE_PLAYER_TO_LOCATION = getCheckPrefix() + "플레이어를 이동시켰습니다.";
    public static final String REGISTER_TEMPLATE = getCheckPrefix() + "이동권 템플릿을 등록했습니다.";
    public static final String REMOVE_TEMPLATE = getCheckPrefix() + "이동권 템플릿을 제거했습니다.";
    public static final String GET_WARP_TICKET = getCheckPrefix() + "이동권을 지급받았습니다.";

    /* prefix */
    public static String getCheckPrefix() {
        return "\uA001 ";
    }

    public static String getXPrefix() {
        return "\uA002 ";
    }

    public static String getWarnPrefix() {
        return "\uA003 ";
    }

    public static void showMessage(CommandSender sender, String message) {
        sender.sendMessage(message);
    }
    
    /* validate */
    public static void notPlayer(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "플레이어가 아닙니다.");
    }

    public static void notOp(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "권한이 없습니다.");
    }

    public static String wrongCommand() {
        return getWarnPrefix() + "명령어 사용법이 잘못되었습니다.";
    }

    /* feedback */
    public static void noPermission(CommandSender sender, String permission) {
        sender.sendMessage(getWarnPrefix() + "필요한 권한이 없습니다. (필요 권한 : " + permission + ")");
    }

    public static void registerWarpTicket(CommandSender sender, String name) {
        sender.sendMessage(getCheckPrefix() + name + " 이동권을 등록했습니다.");
    }

    public static void resetWarpTicket(CommandSender sender, String name) {
        sender.sendMessage(getCheckPrefix() + name + " 이동권을 해제했습니다.");
    }

    /* info */
    public static void locationInfoPrefix(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("─────────── 장소정보 ───────────");
        sender.sendMessage("");
    }

    public static void locationInfo(CommandSender sender, LocationData locationData) {
        sender.sendMessage("　장소명 : " + ChatColor.GREEN + ChatColor.BOLD + locationData.getName());
        Location location = locationData.getLocation();
        sender.sendMessage("　월드 : " + location.getWorld().getName());
        sender.sendMessage("　X : " + location.getBlockX());
        sender.sendMessage("　Y : " + location.getBlockY());
        sender.sendMessage("　Z : " + location.getBlockZ());
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        sender.sendMessage("　yaw : " + yaw);
        sender.sendMessage("　pitch : " + pitch);
        sender.sendMessage("　시야방향 : " + DirectionUtil.getDirection(yaw, pitch));

        if(locationData.getTemplateName() != null)
            sender.sendMessage("　템플릿명 : " + locationData.getTemplateName());
        if(locationData.getEnglishPermission() != null)
            sender.sendMessage("　영어 권한명 : " + locationData.getEnglishPermission());
        if(locationData.getKoreanPermission() != null)
            sender.sendMessage("　한글 권한명 : " + locationData.getKoreanPermission());
    }

    public static void horizontalLineSuffix(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage("──────────────────────────");
        sender.sendMessage("");
    }

    /* command */
    public static void teleportCommandHelper(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "/텔레포트 도움말" + ChatColor.GRAY + " : 사용 가능한 명령어를 확인할 수 있습니다.");
    }

    public static void teleportCommandList(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(getWarnPrefix() + "텔레포트 명령어 목록");
        sender.sendMessage("　　　① /텔레포트 나침반");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 텔레포트를 위한 나침반을 제공받습니다.");
        sender.sendMessage("　　　② /텔레포트 장소설정 장소명 x y z [yaw] [pitch]");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 장소를 정보를 생성하거나 수정합니다. (시야 - 선택 값)");
        sender.sendMessage("　　　③ /텔레포트 권한설정 장소명 [영어권한명] [한글권한명]");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 텔레포트에 필요한 권한을 설정합니다. (공백 입력 시 권한 해제)");
        sender.sendMessage("　　　④ /텔레포트 장소제거 장소명");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 장소 정보를 제거합니다.");
        sender.sendMessage("　　　⑤ /텔레포트 이동 장소명");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 입력한 장소로 이동합니다.");
        sender.sendMessage("　　　⑥ /텔레포트 이동 장소명 플레이어ID|닉네임");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 플레이어를 입력한 장소로 이동시킵니다.");
        sender.sendMessage("　　　⑦ /텔레포트 정보 장소명");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 장소 정보를 조회합니다.");
        sender.sendMessage("　　　⑧ /텔레포트 도움말");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 사용 가능한 명령어를 확인할 수 있습니다.");
        sender.sendMessage("");
    }

    public static void warpTicketCommandHelper(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "/이동권 도움말" + ChatColor.GRAY + " : 사용 가능한 명령어를 확인할 수 있습니다.");
    }

    public static void warpTicketCommandList(CommandSender sender) {
        sender.sendMessage("");
        sender.sendMessage(getWarnPrefix() + "이동권 명령어 목록");
        sender.sendMessage("　　　① /이동권 템플릿등록 템플릿명");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 손에 든 아이템을 이동권 템플릿으로 등록합니다.");
        sender.sendMessage("　　　② /이동권 템플릿제거 템플릿명");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 이동권 템플릿을 제거합니다.");
        sender.sendMessage("　　　③ /이동권 등록 장소명 템플릿명");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 입력한 장소의 이동권을 등록합니다.");
        sender.sendMessage("　　　④ /이동권 해제 장소명");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 입력한 장소의 이동권을 해제합니다.");
        sender.sendMessage("　　　⑤ /이동권 받기 장소명");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 입력한 장소의 이동권을 받습니다.");
        sender.sendMessage("　　　⑥ /이동권 도움말");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 사용 가능한 명령어를 확인할 수 있습니다.");
        sender.sendMessage("");
    }
}
