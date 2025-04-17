package jiekie;

import jiekie.command.TeleportCommand;
import jiekie.completer.TeleportTabCompleter;
import jiekie.event.GuiEvent;
import jiekie.event.PlayerEvent;
import jiekie.util.LocationManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class TeleportPlugin extends JavaPlugin {
    private LocationManager locationManager;

    @Override
    public void onEnable() {
        // config
        saveDefaultConfig();
        reloadConfig();
        
        locationManager = new LocationManager(this);
        locationManager.loadLocations();

        //  이벤트 등록
        getServer().getPluginManager().registerEvents(new PlayerEvent(this), this);
        getServer().getPluginManager().registerEvents(new GuiEvent(this), this);

        // 명령어 등록
        getCommand("텔레포트").setExecutor(new TeleportCommand(this));
        
        // 자동완성 등록
        getCommand("텔레포트").setTabCompleter(new TeleportTabCompleter(this));

        getLogger().info("텔레포트 플러그인 by Jiekie");
        getLogger().info("Copyright © 2025 Jiekie. All rights reserved.");
    }

    public LocationManager getLocationManager() {
        return locationManager;
    }

    @Override
    public void onDisable() {
        // config 저장
        locationManager.saveLocations();
    }
}
