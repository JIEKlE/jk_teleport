package jiekie.teleport.manager;

import jiekie.teleport.TeleportPlugin;
import jiekie.teleport.model.LocationData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

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

            String englishPermission = config.getString(path + ".english_permission", null);
            String koreanPermission = config.getString(path + ".korean_permission", null);
            setPermission(name, englishPermission, koreanPermission);
        }
    }

    public LocationData getLocationData(String name) {
        return locationMap.get(name);
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

    public void setPermission(String name, String englishPermission, String koreanPermission) {
        locationMap.get(name).setEnglishPermission(englishPermission);
        locationMap.get(name).setKoreanPermission(koreanPermission);
    }

    public boolean playerHasPermission(String name, Player player) {
        LocationData locationData = locationMap.get(name);
        String englishPermission = locationData.getEnglishPermission();
        if(englishPermission == null) return true;
        return player.hasPermission(englishPermission);
    }

    public String getPermissionName(String name) {
        LocationData locationData = locationMap.get(name);
        return locationData.getKoreanPermission() == null ? locationData.getEnglishPermission() : locationData.getKoreanPermission();
    }

    public void setTemplateNameForLocation(String name, String templateName) {
        locationMap.get(name).setTemplateName(templateName);
    }

    public void resetTemplate(String templateName) {
        for(LocationData locationData : locationMap.values()) {
            String locationTemplateName = locationData.getTemplateName();
            if(locationTemplateName == null || locationTemplateName.isBlank()) continue;
            if(locationTemplateName.equals(templateName))
                locationData.setTemplateName(null);
        }
    }

    public String getTemplateNameForLocation(String name) {
        return locationMap.get(name).getTemplateName();
    }

    public void removeLocation(String name) {
        locationMap.remove(name);
    }

    public List<String> getAllLocationNames() {
        return new ArrayList<>(locationMap.keySet());
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
            config.set(path + ".english_permission", locationData.getEnglishPermission());
            config.set(path + ".korean_permission", locationData.getKoreanPermission());
        }

        plugin.saveConfig();
    }
}
