package jiekie.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatUtil {
    public static String getWarnPrefix() {
        return "[ " + ChatColor.YELLOW + "❗" + ChatColor.WHITE + " ] ";
    }
    
    /* 유효성 검사 */
    public static void notPlayer(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "플레이어가 아닙니다.");
    }

    public static void notOp(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "권한이 없습니다.");
    }

    public static String wrongCommand() {
        return getWarnPrefix() + "명령어 사용법이 잘못되었습니다.";
    }

    public static void noItemInHand(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "손에 아이템을 들고 설정해주시기 바랍니다.");
    }

    public static void inventoryFull(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "인벤토리가 가득 찼습니다. 인벤토리를 1칸 이상 비워주시기 바랍니다.");
    }

    public static void isNotRegisteredLocation(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "장소에 대한 정보를 찾을 수 없습니다.");
    }

    public static void coordinatesNotNumber(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "좌표 값은 숫자여야 합니다.");
    }

    public static void playerDoesNotExist(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "해당 이름을 가진 플레이어가 없습니다.");
    }

    public static void templateNotRegistered(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "템플릿에 대한 정보를 찾을 수 없습니다.");
    }

    public static void WarpTicketNotRegistered(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "이동권이 등록되지 않았습니다.");
    }
    
    /* 피드백 */
    public static void getCompass(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "텔레포트 나침반을 받았습니다.");
    }

    public static void locationIsSaved(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "장소 정보를 등록했습니다.");
    }

    public static void locationIsChanged(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "장소 정보를 수정했습니다.");
    }

    public static void locationIsRemoved(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "장소 정보를 제거했습니다.");
    }

    public static void movePlayerToWorld(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "플레이어를 이동시켰습니다.");
    }

    public static void registerTemplate(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "이동권 템플릿을 등록했습니다.");
    }

    public static void registerWarpTicket(CommandSender sender, String name) {
        sender.sendMessage(getWarnPrefix() + name + " 이동권을 등록했습니다.");
    }

    public static void getWarpTicket(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "이동권을 지급받았습니다.");
    }

    /* 명령어 설명 */
    public static void teleportCommandHelper(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "/텔레포트 도움말" + ChatColor.GRAY + " : 사용 가능한 명령어를 확인할 수 있습니다.");
    }

    public static void teleportCommandList(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "텔레포트 명령어 목록");
        sender.sendMessage("　　　① /텔레포트 나침반");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 텔레포트를 위한 나침반을 제공받습니다.");
        sender.sendMessage("　　　② /텔레포트 설정 장소명 x y z [yaw] [pitch]");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 장소를 설정 및 저장합니다. (시야 정보는 선택 값입니다.)");
        sender.sendMessage("　　　③ /텔레포트 제거 장소명");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 장소 정보를 제거합니다.");
        sender.sendMessage("　　　④ /텔레포트 이동 장소명");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 입력한 장소로 이동합니다.");
        sender.sendMessage("　　　⑤ /텔레포트 이동 장소명 플레이어ID|닉네임");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 플레이어를 입력한 장소로 이동합니다.");
        sender.sendMessage("　　　⑥ /텔레포트 도움말");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 사용 가능한 명령어를 확인할 수 있습니다.");
    }

    public static void warpTicketCommandHelper(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "/이동권 도움말" + ChatColor.GRAY + " : 사용 가능한 명령어를 확인할 수 있습니다.");
    }

    public static void warpTicketCommandList(CommandSender sender) {
        sender.sendMessage(getWarnPrefix() + "이동권 명령어 목록");
        sender.sendMessage("　　　① /이동권 템플릿 템플릿명");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 손에 든 아이템을 이동권 템플릿으로 등록합니다.");
        sender.sendMessage("　　　② /이동권 등록 장소명 템플릿명");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 입력한 장소의 이동권을 등록합니다.");
        sender.sendMessage("　　　③ /이동권 받기 장소명");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 입력한 장소의 이동권을 받습니다.");
        sender.sendMessage("　　　④ /이동권 도움말");
        sender.sendMessage(ChatColor.GRAY + "　　　　　: 사용 가능한 명령어를 확인할 수 있습니다.");
    }
}
