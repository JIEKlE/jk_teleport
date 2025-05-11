package jiekie.api;

import jiekie.manager.LocationManager;
import org.bukkit.Location;

public class TeleportAPI {
    private static TeleportAPI instance;
    private final LocationManager locationManager;

    private TeleportAPI(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public static void initialize(LocationManager locationManager) {
        if(instance == null)
            instance = new TeleportAPI(locationManager);
    }

    public static TeleportAPI getInstance() {
        return instance;
    }

    public void setLocation(String name, Location location) {
        locationManager.setLocation(name, location);
    }
}
