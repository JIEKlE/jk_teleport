package jiekie.event;

import jiekie.TeleportPlugin;
import jiekie.util.GuiUtil;
import jiekie.util.SoundUtil;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiEvent implements Listener {
    private final TeleportPlugin plugin;

    public GuiEvent(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        // 나침반 클릭 이벤트
        onCompassClick(e);
    }

    /* 나침반 클릭 이벤트 */
    public void onCompassClick(InventoryClickEvent e) {
        HumanEntity entity = e.getWhoClicked();
        if(!(entity instanceof Player player)) return;

        String title = e.getView().getTitle();
        if(!title.equals(GuiUtil.CHEST_NAME)) return;

        // 기존 이벤트 작동 취소
        e.setCancelled(true);

        ItemStack clickedItem = e.getCurrentItem();
        if(clickedItem == null || clickedItem.getType().isAir()) return;
        if(!clickedItem.hasItemMeta()) return;

        ItemMeta meta = clickedItem.getItemMeta();
        String name = meta.getDisplayName();

        Location location = plugin.getLocationManager().getLocation(name);
        player.teleport(location);
        SoundUtil.playTeleport(player);
    }
}
