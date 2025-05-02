package jiekie.model;

import org.bukkit.Location;

public class LocationData {
    private final String name;
    private Location location;
    private String templateName;
    private String englishPermission;
    private String koreanPermission;

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

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getEnglishPermission() {
        return englishPermission;
    }

    public void setEnglishPermission(String englishPermission) {
        this.englishPermission = englishPermission;
    }

    public String getKoreanPermission() {
        return koreanPermission;
    }

    public void setKoreanPermission(String koreanPermission) {
        this.koreanPermission = koreanPermission;
    }
}
