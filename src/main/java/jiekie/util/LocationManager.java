package jiekie.util;

import jiekie.TeleportPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationManager {
    private final TeleportPlugin plugin;
    private final Map<String, Location> locationMap = new HashMap<>();

    public LocationManager(TeleportPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadLocations() {
        locationMap.clear();
        FileConfiguration config = plugin.getConfig();
        ConfigurationSection locations = config.getConfigurationSection("locations");

        if(locations == null) return;
        for(String name : locations.getKeys(false)) {
            String path = "locations." + name;
            String worldName = config.getString(path + ".world");
            World world = Bukkit.getWorld(worldName);
            int x = config.getInt(path + ".x");
            int y = config.getInt(path + ".y");
            int z = config.getInt(path + ".z");
            int yaw = config.getInt(path + ".yaw");
            int pitch = config.getInt(path + ".pitch");

            locationMap.put(name, new Location(world, x, y, z, yaw, pitch));
        }
    }

    public Location getLocation(String name) {
        return locationMap.get(name);
    }

    public boolean exists(String name) {
        return locationMap.containsKey(name);
    }

    public void setLocation(String name, Location location) {
        locationMap.put(name, location);
    }

    public void removeLocation(String name) {
        locationMap.remove(name);
    }

    public void saveLocations() {
        FileConfiguration config = plugin.getConfig();
        config.set("locations", null);

        for(Map.Entry<String, Location> entry : locationMap.entrySet()) {
            String name = entry.getKey();
            Location location = entry.getValue();

            String path = "locations." + name;
            config.set(path + ".world", location.getWorld().getName());
            config.set(path + ".x", location.getBlockX());
            config.set(path + ".y", location.getBlockY());
            config.set(path + ".z", location.getBlockZ());
            config.set(path + ".yaw", location.getYaw());
            config.set(path + ".pitch", location.getPitch());
        }

        plugin.saveConfig();
    }

    public List<String> getAllLocationNames() {
        return new ArrayList<>(locationMap.keySet());
    }
}
