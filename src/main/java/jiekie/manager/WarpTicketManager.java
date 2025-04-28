package jiekie.manager;

import jiekie.TeleportPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

public class WarpTicketManager {
    private final TeleportPlugin plugin;
    private final Map<String, ItemStack> warpTicketMap = new HashMap<>();
    private final String WARP_TICKETS_PREFIX = "warp_tickets";
    private final List<String> warpTicketLore = List.of(
            " "
            , ChatColor.DARK_GRAY + "우클릭 시 이동합니다."
    );

    public WarpTicketManager(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        warpTicketMap.clear();
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection warpTickets = config.getConfigurationSection(WARP_TICKETS_PREFIX);

        if(warpTickets == null) return;
        for(String name : warpTickets.getKeys(false)) {
            try {
                String path = WARP_TICKETS_PREFIX + "." + name;
                String encodedItem = config.getString(path);
                warpTicketMap.put(name, itemFromBase64(encodedItem));

            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                plugin.getLogger().info("이동권 아이템 불러오기 실패");
            }
        }
    }

    public void registerTemplate(String name, ItemStack item) {
        ItemStack template = item.clone();
        template.setAmount(1);

        ItemMeta templateMeta = template.getItemMeta();
        templateMeta.setLore(warpTicketLore);
        template.setItemMeta(templateMeta);

        warpTicketMap.put(name, template);
    }

    public boolean existTemplate(String name) {
        return warpTicketMap.containsKey(name);
    }

    public ItemStack getWarpTicket(String name, String locationName) {
        ItemStack warpTicket = warpTicketMap.get(name).clone();

        ItemMeta meta = warpTicket.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + locationName + " 이동권");
        warpTicket.setItemMeta(meta);

        return warpTicket;
    }

    public boolean compareItem(ItemStack item) {
        // item
        if(item == null || item.getType() == Material.AIR) return false;
        if(!item.hasItemMeta()) return false;

        // display name
        ItemMeta itemMeta = item.getItemMeta();
        if(!itemMeta.hasDisplayName()) return false;

        String itemName = itemMeta.getDisplayName();
        if(!itemName.startsWith(ChatColor.WHITE + "")) return false;
        if(!itemName.endsWith(" 이동권")) return false;

        // lore
        if(itemMeta.getLore() == null) return false;

        for(ItemStack template : warpTicketMap.values()) {
            if(item.getType() != template.getType()) continue;

            ItemMeta templateMeta = template.getItemMeta();
            if(!itemMeta.getLore().equals(warpTicketLore)) continue;
            if(itemMeta.hasCustomModelData() != templateMeta.hasCustomModelData()) continue;

            if(itemMeta.hasCustomModelData()) {
                if(itemMeta.getCustomModelData() == templateMeta.getCustomModelData()) return true;

            } else {
                return true;
            }
        }

        return false;
    }

    public List<String> getTemplateNames() {
        return new ArrayList<>(warpTicketMap.keySet());
    }

    public void save() {
        FileConfiguration config = plugin.getConfig();
        config.set(WARP_TICKETS_PREFIX, null);

        for(Map.Entry<String, ItemStack> entry : warpTicketMap.entrySet()) {
            try {
                String name = entry.getKey();
                ItemStack template = entry.getValue();

                String path = WARP_TICKETS_PREFIX + "." + name;
                config.set(path, itemStackToBase64(template));

            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().info("이동권 아이템 저장 실패");
            }
        }

        plugin.saveConfig();
    }

    private String itemStackToBase64(ItemStack item) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

        dataOutput.writeObject(item);
        dataOutput.close();
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    private ItemStack itemFromBase64(String base64) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(base64);
        BukkitObjectInputStream inputStream = new BukkitObjectInputStream(new ByteArrayInputStream(data));
        ItemStack item = (ItemStack) inputStream.readObject();
        inputStream.close();
        return item;
    }
}
