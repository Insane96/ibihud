package insane96mcp.itemblockinfohud;

import com.mojang.logging.LogUtils;
import insane96mcp.insanelib.setup.ILModConfig;
import insane96mcp.itemblockinfohud.data.generator.IBIHBlockTagsProvider;
import insane96mcp.itemblockinfohud.data.generator.IBIHItemTagsProvider;
import insane96mcp.itemblockinfohud.network.ServerPresencePayload;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

@Mod(ItemBlockInfoHud.MOD_ID)
public class ItemBlockInfoHud {
    public static final String MOD_ID = "itemblockinfohud";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ILModConfig CLIENT_CONFIG;

    public ItemBlockInfoHud(IEventBus modEventBus, ModContainer modContainer) {
        CLIENT_CONFIG = new ILModConfig(id("main"), "Single Module", ModConfig.Type.CLIENT, modEventBus, ItemBlockInfoHud.class.getClassLoader());
        modContainer.registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG.spec);

        modEventBus.addListener(ItemBlockInfoHud::gatherData);
        modEventBus.addListener(ServerPresencePayload::register);
    }

    public static void gatherData(GatherDataEvent.Client event) {
        event.createProvider(IBIHBlockTagsProvider::new);
        event.createProvider(IBIHItemTagsProvider::new);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static String lang(String path) {
        return MOD_ID + "." + path;
    }
}
