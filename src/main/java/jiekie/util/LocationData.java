package jiekie.util;

import org.bukkit.Location;

public class LocationData {
    private String name;
    private Location location;
    private String templateName;

    public LocationData(String name, Location location, String templateName) {
        this.name = name;
        this.location = location;
        this.templateName = templateName;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
