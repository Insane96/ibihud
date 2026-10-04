package insane96mcp.itemblockinfohud;

import insane96mcp.itemblockinfohud.feature.HudInfos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = ItemBlockInfoHud.MOD_ID, dist = Dist.CLIENT)
public class ItemBlockInfoHudClient {
    public ItemBlockInfoHudClient(IEventBus modEventBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        modEventBus.addListener(HudInfos::registerGuiLayers);
    }
}
