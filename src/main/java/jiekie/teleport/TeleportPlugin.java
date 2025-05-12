package jiekie.teleport;

import jiekie.teleport.api.TeleportAPI;
import jiekie.teleport.command.TeleportCommand;
import jiekie.teleport.command.WarpTicketCommand;
import jiekie.teleport.completer.TeleportTabCompleter;
import jiekie.teleport.completer.WarpTicketTabCompleter;
import jiekie.teleport.event.GuiEvent;
import jiekie.teleport.event.PlayerEvent;
import jiekie.teleport.manager.LocationManager;
import jiekie.teleport.manager.WarpTicketManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TeleportPlugin extends JavaPlugin {
    private LocationManager locationManager;
    private WarpTicketManager warpTicketManager;

    @Override
    public void onEnable() {
        // config
        saveDefaultConfig();
        reloadConfig();
        
        locationManager = new LocationManager(this);
        locationManager.loadLocations();
        warpTicketManager = new WarpTicketManager(this);
        warpTicketManager.load();

        //  event
        getServer().getPluginManager().registerEvents(new PlayerEvent(this), this);
        getServer().getPluginManager().registerEvents(new GuiEvent(this), this);

        // command
        getCommand("텔레포트").setExecutor(new TeleportCommand(this));
        getCommand("이동권").setExecutor(new WarpTicketCommand(this));
        
        // tab completer
        getCommand("텔레포트").setTabCompleter(new TeleportTabCompleter(this));
        getCommand("이동권").setTabCompleter(new WarpTicketTabCompleter(this));

        // api
        TeleportAPI.initialize(locationManager);

        getLogger().info("텔레포트 플러그인 by Jiekie");
        getLogger().info("Copyright © 2025 Jiekie. All rights reserved.");
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    public WarpTicketManager getWarpTicketManager() {
        return warpTicketManager;
    }

    @Override
    public void onDisable() {
        // config 저장
        locationManager.saveLocations();
        warpTicketManager.save();
    }
}
