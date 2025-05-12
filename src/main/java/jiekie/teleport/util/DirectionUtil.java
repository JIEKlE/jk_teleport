package jiekie.teleport.util;

public class DirectionUtil {
    public static String getDirection(float yaw, float pitch) {
        if (pitch > 60) return "아래쪽";
        if (pitch < -60) return "위쪽";

        yaw = (yaw % 360 + 360) % 360; // 0~360 정규화

        if (yaw >= 337.5 || yaw < 22.5) return "남쪽";
        if (yaw < 67.5) return "남서쪽";
        if (yaw < 112.5) return "서쪽";
        if (yaw < 157.5) return "북서쪽";
        if (yaw < 202.5) return "북쪽";
        if (yaw < 247.5) return "북동쪽";
        if (yaw < 292.5) return "동쪽";
        return "남동쪽";
    }
}
