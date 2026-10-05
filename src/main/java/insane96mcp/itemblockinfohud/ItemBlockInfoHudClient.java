package insane96mcp.itemblockinfohud;

import insane96mcp.itemblockinfohud.feature.HudInfos;
import insane96mcp.itemblockinfohud.network.ServerPresencePayload;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = ItemBlockInfoHud.MOD_ID, dist = Dist.CLIENT)
public class ItemBlockInfoHudClient {
    public ItemBlockInfoHudClient(IEventBus modEventBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        modEventBus.addListener(HudInfos::registerGuiLayers);
        NeoForge.EVENT_BUS.addListener(ItemBlockInfoHudClient::onLoggingIn);
    }

    private static void onLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
        if (event.getPlayer().connection.hasChannel(ServerPresencePayload.TYPE))
            return;
        event.getPlayer().displayClientMessage(Component.translatable(ItemBlockInfoHud.lang("warning.missing_on_server")).withStyle(ChatFormatting.YELLOW), false);
    }
}
