package jiekie.event;

import jiekie.TeleportPlugin;
import jiekie.util.GuiUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class PlayerEvent implements Listener {
    private final TeleportPlugin plugin;

    public PlayerEvent(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        // 나침반 우클릭
        onCompassRightClick(e);
    }

    /* 나침반 우클릭 */
    private void onCompassRightClick(PlayerInteractEvent e) {
        // 우클릭 반응
        if(!Objects.equals(e.getHand(), EquipmentSlot.HAND)) return;
        if(e.getAction() != Action.RIGHT_CLICK_AIR  && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = e.getPlayer();

        // 나침반 체크
        ItemStack item = e.getItem();
        if(item == null || item.getType() != Material.COMPASS) return;

        ItemMeta meta = item.getItemMeta();
        if(meta == null || !meta.hasDisplayName()) return;

        if(!meta.getDisplayName().equals(GuiUtil.COMPASS_NAME)) return;

        e.setCancelled(true);
        GuiUtil.openCompass(plugin, player);
    }


}
