package jiekie.command;

import jiekie.TeleportPlugin;
import jiekie.util.ChatUtil;
import jiekie.util.LocationManager;
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
        if(!(sender instanceof Player)) {
            ChatUtil.notPlayer(sender);
            return true;
        }

        Player player = (Player) sender;
        if(!player.isOp()) {
            ChatUtil.notOp(player);
            return true;
        }

        if(args == null || args.length == 0) {
            ChatUtil.warpTicketCommandHelper(player);
            return true;
        }

        switch (args[0]) {
            case "템플릿":
                registerTemplate(player, args);
                break;

            case "등록":
                registerWarpTicket(player, args);
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
            player.sendMessage(ChatUtil.wrongCommand() + " (/이동권 템플릿 템플릿명)");
            return;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack template = inventory.getItemInMainHand();
        if(template == null || template.getType() == Material.AIR) {
            ChatUtil.noItemInHand(player);
            return;
        }

        plugin.getWarpTicketManager().registerTemplate(args[1], template);

        ChatUtil.registerTemplate(player);
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
            ChatUtil.isNotRegisteredLocation(player);
            return;
        }

        String templateName = args[2];
        if(!plugin.getWarpTicketManager().existTemplate(templateName)) {
            ChatUtil.templateNotRegistered(player);
            return;
        }

        locationManager.setTemplateNameForLocation(locationName, templateName);

        ChatUtil.registerWarpTicket(player, locationName);
        SoundUtil.playNoteBlockBell(player);
    }

    private void getWarpTicket(Player player, String[] args) {
        if(args.length < 2) {
            player.sendMessage(ChatUtil.wrongCommand() + " (/이동권 받기 장소명)");
            return;
        }

        PlayerInventory inventory = player.getInventory();
        if(inventory.firstEmpty() == -1) {
            ChatUtil.inventoryFull(player);
            return;
        }

        LocationManager locationManager = plugin.getLocationManager();
        String locationName = args[1];
        if(!locationManager.exists(locationName)) {
            ChatUtil.isNotRegisteredLocation(player);
            return;
        }

        String templateName = locationManager.getTemplateNameForLocation(locationName);
        if(templateName == null) {
            ChatUtil.WarpTicketNotRegistered(player);
            return;
        }

        ItemStack warpTicket = plugin.getWarpTicketManager().getWarpTicket(templateName, locationName);
        inventory.addItem(warpTicket);

        ChatUtil.getWarpTicket(player);
        SoundUtil.playNoteBlockBell(player);
    }
}
