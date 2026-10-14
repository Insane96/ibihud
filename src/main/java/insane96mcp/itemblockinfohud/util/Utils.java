package insane96mcp.itemblockinfohud.util;

import insane96mcp.itemblockinfohud.ItemBlockInfoHud;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
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

    private static final String[] MOON_PHASES = {"full_moon", "waning_gibbous", "last_quarter", "waning_crescent", "new_moon", "waxing_crescent", "first_quarter", "waxing_gibbous"};

    public static String getMoonPhaseTranslatable(int moonPhase) {
        return ItemBlockInfoHud.lang("moon_phase." + MOON_PHASES[Math.floorMod(moonPhase, MOON_PHASES.length)]);
    }

    public static String ticksToTimeString(long ticks) {
        int hours = (int) ((ticks + 6000) % 24000 / 1000);
        String minutes = String.format("%02d", ticks % 1000 * 60 / 1000);
        return hours + ":" + minutes;
    }

    public static String getWeatherTranslatable(Level level, BlockPos pos) {
        Biome.Precipitation precipitation = level.getBiome(pos).value().getPrecipitationAt(pos);
        if (!level.isRaining())
            return ItemBlockInfoHud.lang("weather.clear");
        else if (level.isThundering())
            return ItemBlockInfoHud.lang("weather.thunder");
        else if (precipitation == Biome.Precipitation.NONE)
            return ItemBlockInfoHud.lang("weather.cloudy");
        else if (precipitation == Biome.Precipitation.SNOW)
            return ItemBlockInfoHud.lang("weather.snow");
        return ItemBlockInfoHud.lang("weather.rain");
    }
}
