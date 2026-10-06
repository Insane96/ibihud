package insane96mcp.itemblockinfohud.util;

import insane96mcp.itemblockinfohud.ItemBlockInfoHud;
import org.jetbrains.annotations.NotNull;

public class Utils {
    public static @NotNull String getDirectionTranslatable(float direction) {
        String d = "";
        if (direction > -22.5 && direction <= 22.5)
            d = "cardinal_direction.south";
        else if (direction > 22.5 && direction <= 67.5)
            d = "cardinal_direction.south_west";
        else if (direction > 67.5 && direction <= 112.5)
            d = "cardinal_direction.west";
        else if (direction > 112.5 && direction <= 157.5)
            d = "cardinal_direction.north_west";
        else if (direction > 157.5 || direction <= -157.5)
            d = "cardinal_direction.north";
        else if (direction > -157.5 && direction <= -112.5)
            d = "cardinal_direction.north_east";
        else if (direction > -112.5 && direction <= -67.5)
            d = "cardinal_direction.east";
        else if (direction > -67.5 && direction <= -22.5)
            d = "cardinal_direction.south_east";
        return ItemBlockInfoHud.lang(d);
    }

    public static String ticksToTimeString(long ticks) {
        int hours = (int) ((ticks + 6000) % 24000 / 1000);
        String minutes = String.format("%02d", ticks % 1000 / 20);
        return hours + ":" + minutes;
    }
}
