package insane96mcp.itemblockinfohud;

import com.mojang.logging.LogUtils;
import insane96mcp.insanelib.setup.ILModConfig;
import insane96mcp.itemblockinfohud.data.generator.IBIHBlockTagsProvider;
import insane96mcp.itemblockinfohud.data.generator.IBIHItemTagsProvider;
import insane96mcp.itemblockinfohud.network.ServerPresencePayload;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;

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

    public static void gatherData(GatherDataEvent event) {
        PackOutput output = event.getGenerator().getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        IBIHBlockTagsProvider blockTagsProvider = new IBIHBlockTagsProvider(output, lookupProvider, MOD_ID, existingFileHelper);
        event.getGenerator().addProvider(
                event.includeServer(),
                blockTagsProvider
        );
        event.getGenerator().addProvider(
                event.includeServer(),
                new IBIHItemTagsProvider(output, lookupProvider, blockTagsProvider.contentsGetter(), MOD_ID, existingFileHelper)
        );
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
