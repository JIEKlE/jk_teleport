package jiekie.teleport.util;

import jiekie.teleport.TeleportPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class GuiUtil {
    public static final String CHEST_NAME = "T E L E P O R T";
    public static final String COMPASS_NAME = ChatColor.LIGHT_PURPLE + "텔레포트 나침반";

    public static void openCompass(TeleportPlugin plugin, Player player) {
        List<String> locationNames = plugin.getLocationManager().getAllLocationNames();
        int inventorySize = getInventorySize(locationNames.size());
        Inventory inventory = Bukkit.createInventory(null, inventorySize, CHEST_NAME);

        int index = 0;
        for(String name : locationNames) {
            int remainder = index % 9;
            ItemStack item = getIndexedStainedGlass(remainder, name);
            inventory.setItem(index, item);
            index++;
        }

        player.openInventory(inventory);
    }

    private static int getInventorySize(int itemAmount) {
        int inventorySize = 0;
        if(itemAmount <= 9)
            inventorySize = 9;
        if(itemAmount > 9 && itemAmount <= 18)
            inventorySize = 18;
        if(itemAmount > 18 && itemAmount <= 27)
            inventorySize = 27;
        if(itemAmount > 27 && itemAmount <= 36)
            inventorySize = 36;
        if(itemAmount > 36 && itemAmount <= 45)
            inventorySize = 45;
        if(itemAmount > 45 && itemAmount <= 54)
            inventorySize = 54;

        return inventorySize;
    }

    private static ItemStack getIndexedStainedGlass(int index, String name) {
        Material material = Material.BLACK_STAINED_GLASS;
        if(index == 0)
            material = Material.RED_STAINED_GLASS;
        if(index == 1)
            material = Material.ORANGE_STAINED_GLASS;
        if(index == 2)
            material = Material.YELLOW_STAINED_GLASS;
        if(index == 3)
            material = Material.LIME_STAINED_GLASS;
        if(index == 4)
            material = Material.GREEN_STAINED_GLASS;
        if(index == 5)
            material = Material.LIGHT_BLUE_STAINED_GLASS;
        if(index == 6)
            material = Material.BLUE_STAINED_GLASS;
        if(index == 7)
            material = Material.PURPLE_STAINED_GLASS;
        if(index == 8)
            material = Material.PINK_STAINED_GLASS;

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + name);
        item.setItemMeta(meta);
        return item;
    }
}
