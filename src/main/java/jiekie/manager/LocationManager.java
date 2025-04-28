package jiekie.manager;

import jiekie.TeleportPlugin;
import jiekie.model.LocationData;
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
    private final Map<String, LocationData> locationMap = new HashMap<>();

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
            String templateName = config.getString(path + ".template_name", null);

            Location location = new Location(world, x, y, z, yaw, pitch);
            LocationData locationData = new LocationData(name, location, templateName);
            locationMap.put(name, locationData);
        }
    }

    public Location getLocation(String name) {
        return locationMap.get(name).getLocation();
    }

    public boolean exists(String name) {
        return locationMap.containsKey(name);
    }

    public void setLocation(String name, Location location) {
        if(exists(name)) {
            locationMap.get(name).setLocation(location);

        } else {
            LocationData locationData = new LocationData(name, location, null);
            locationMap.put(name, locationData);
        }
    }

    public void setTemplateNameForLocation(String name, String templateName) {
        locationMap.get(name).setTemplateName(templateName);
    }

    public String getTemplateNameForLocation(String name) {
        return locationMap.get(name).getTemplateName();
    }

    public void removeLocation(String name) {
        locationMap.remove(name);
    }

    public void saveLocations() {
        FileConfiguration config = plugin.getConfig();
        config.set("locations", null);

        for(Map.Entry<String, LocationData> entry : locationMap.entrySet()) {
            String name = entry.getKey();
            LocationData locationData = entry.getValue();
            Location location = locationData.getLocation();

            String path = "locations." + name;
            config.set(path + ".world", location.getWorld().getName());
            config.set(path + ".x", location.getBlockX());
            config.set(path + ".y", location.getBlockY());
            config.set(path + ".z", location.getBlockZ());
            config.set(path + ".yaw", location.getYaw());
            config.set(path + ".pitch", location.getPitch());
            config.set(path + ".template_name", locationData.getTemplateName());
        }

        plugin.saveConfig();
    }

    public List<String> getAllLocationNames() {
        return new ArrayList<>(locationMap.keySet());
    }
}
