package jiekie.command;

import jiekie.TeleportPlugin;
import jiekie.util.ChatUtil;
import jiekie.manager.LocationManager;
import jiekie.util.SoundUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

public class WarpTicketCommand implements CommandExecutor {
    private final TeleportPlugin plugin;

    public WarpTicketCommand(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            ChatUtil.notPlayer(sender);
            return true;
        }

        if(!player.isOp()) {
            ChatUtil.notOp(player);
            return true;
        }

        if(args == null || args.length == 0) {
            ChatUtil.warpTicketCommandHelper(player);
            return true;
        }

        switch (args[0]) {
            case "템플릿등록":
                registerTemplate(player, args);
                break;

            case "템플릿제거":
                removeTemplate(player, args);
                break;

            case "등록":
                registerWarpTicket(player, args);
                break;

            case "해제":
                resetWarpTicket(player, args);
                break;

            case "받기":
                getWarpTicket(player, args);
                break;

            case "도움말":
                ChatUtil.warpTicketCommandList(sender);
                break;

            default:
                ChatUtil.warpTicketCommandHelper(sender);
                break;
        }

        return true;
    }

    private void registerTemplate(Player player, String[] args) {
        if(args.length < 2) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/이동권 템플릿등록 템플릿명)");
            return;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack template = inventory.getItemInMainHand();
        if(template.getType() == Material.AIR) {
            ChatUtil.showMessage(player, ChatUtil.NO_ITEM);
            return;
        }

        plugin.getWarpTicketManager().registerTemplate(args[1], template);

        ChatUtil.showMessage(player, ChatUtil.REGISTER_TEMPLATE);
        SoundUtil.playNoteBlockBell(player);
    }

    private void removeTemplate(Player player, String[] args) {
        if(args.length < 2) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/이동권 템플릿제거 템플릿명)");
            return;
        }

        String templateName = args[1];
        if(!plugin.getWarpTicketManager().existTemplate(templateName)) {
            ChatUtil.showMessage(player, ChatUtil.TEMPLATE_NOT_REGISTERED);
            return;
        }

        plugin.getWarpTicketManager().removeTemplate(templateName);

        ChatUtil.showMessage(player, ChatUtil.REMOVE_TEMPLATE);
        SoundUtil.playNoteBlockBell(player);
    }

    private void registerWarpTicket(Player player, String[] args) {
        if(args.length < 3) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/이동권 등록 장소명 템플릿명)");
            return;
        }

        LocationManager locationManager = plugin.getLocationManager();
        String locationName = args[1];
        if(!locationManager.exists(locationName)) {
            ChatUtil.showMessage(player, ChatUtil.IS_NOT_REGISTERED_LOCATION);
            return;
        }

        String templateName = args[2];
        if(!plugin.getWarpTicketManager().existTemplate(templateName)) {
            ChatUtil.showMessage(player, ChatUtil.TEMPLATE_NOT_REGISTERED);
            return;
        }

        locationManager.setTemplateNameForLocation(locationName, templateName);

        ChatUtil.registerWarpTicket(player, locationName);
        SoundUtil.playNoteBlockBell(player);
    }

    private void resetWarpTicket(Player player, String[] args) {
        if(args.length < 2) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/이동권 해제 장소명)");
            return;
        }

        LocationManager locationManager = plugin.getLocationManager();
        String locationName = args[1];
        if(!locationManager.exists(locationName)) {
            ChatUtil.showMessage(player, ChatUtil.IS_NOT_REGISTERED_LOCATION);
            return;
        }

        String templateName = locationManager.getTemplateNameForLocation(locationName);
        if(templateName == null) {
            ChatUtil.showMessage(player, ChatUtil.WARP_TICKET_NOT_REGISTERED);
            return;
        }

        locationManager.setTemplateNameForLocation(locationName, null);

        ChatUtil.resetWarpTicket(player, locationName);
        SoundUtil.playNoteBlockBell(player);
    }

    private void getWarpTicket(Player player, String[] args) {
        if(args.length < 2) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/이동권 받기 장소명)");
            return;
        }

        PlayerInventory inventory = player.getInventory();
        if(inventory.firstEmpty() == -1) {
            ChatUtil.showMessage(player, ChatUtil.INVENTORY_FULL);
            return;
        }

        LocationManager locationManager = plugin.getLocationManager();
        String locationName = args[1];
        if(!locationManager.exists(locationName)) {
            ChatUtil.showMessage(player, ChatUtil.IS_NOT_REGISTERED_LOCATION);
            return;
        }

        String templateName = locationManager.getTemplateNameForLocation(locationName);
        if(templateName == null) {
            ChatUtil.showMessage(player, ChatUtil.WARP_TICKET_NOT_REGISTERED);
            return;
        }

        ItemStack warpTicket = plugin.getWarpTicketManager().getWarpTicket(templateName, locationName);
        inventory.addItem(warpTicket);

        ChatUtil.showMessage(player, ChatUtil.GET_WARP_TICKET);
        SoundUtil.playNoteBlockBell(player);
    }
}
