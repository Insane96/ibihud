package insane96mcp.itemblockinfohud.feature;

import insane96mcp.insanelib.core.feature.Feature;
import insane96mcp.insanelib.core.feature.LoadFeature;
import insane96mcp.itemblockinfohud.ItemBlockInfoHud;
import insane96mcp.itemblockinfohud.data.HUDInfo;
import insane96mcp.itemblockinfohud.data.generator.IBIHItemTagsProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;

@LoadFeature(name = "HUD Infos", canBeDisabled = false)
public class IBIHFeature extends Feature {
    @Override
    public void loadConfigOptions() {
        super.loadConfigOptions();
        HUDInfo.freezeRegistry();
        HUDInfo.getRegistry().forEach(hudInfo ->
                hudInfo.enabled = HUDInfo.defineConfig(getBuilder(), hudInfo));
    }

    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ItemBlockInfoHud.id("hud_infos"), (guiGraphics, deltaTracker) -> {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null || mc.getDebugOverlay().showDebugScreen() || !Feature.isEnabled(IBIHFeature.class) || mc.options.hideGui)
                return;

            ArrayList<String> toDraw = new ArrayList<>();
            HitResult hitResult = Minecraft.getInstance().hitResult;
            HUDInfo.getRegistry().forEach(hudInfo -> {
                if (!hudInfo.enabled.get()
                        || !shouldRender(player, hitResult, hudInfo.itemTag, hudInfo.blockTag))
                    return;
                toDraw.add(hudInfo.renderer.apply(player).getString());
            });

            int y = 2;
            for (String s : toDraw) {
                guiGraphics.drawString(mc.font, s, 2, y, 0xFFFFFF);
                y += mc.font.lineHeight + 1;
            }
        });
    }

    public static boolean shouldRender(Player player, @Nullable HitResult hitResult, TagKey<Item> itemTag, TagKey<Block> blockTagKey) {
        return player.getInventory().contains(itemTag)
                || hasContainerWith(player, itemTag)
                || isLookingAtItemFrameWith(hitResult, itemTag)
                || isLookingAtBlock(hitResult, player.level(), blockTagKey);
    }

    private static boolean hasContainerWith(Player player, TagKey<Item> itemTag) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(IBIHItemTagsProvider.SEARCHABLE_CONTAINERS) && containerContains(stack, itemTag))
                return true;
        }
        return false;
    }

    private static boolean containerContains(ItemStack container, TagKey<Item> itemTag) {
        NonNullList<ItemStack> list = NonNullList.create();
        container.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(list);
        list.addAll(container.getOrDefault(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY).itemCopyStream().toList());
        return list.stream().anyMatch(s -> s.is(itemTag));
    }

    public static boolean isLookingAtItemFrameWith(@Nullable HitResult hitResult, TagKey<Item> itemTag) {
        return hitResult != null && hitResult.getType() == HitResult.Type.ENTITY && ((EntityHitResult) hitResult).getEntity() instanceof ItemFrame itemFrame && itemFrame.getItem().is(itemTag);
    }

    public static boolean isLookingAtBlock(HitResult hitResult, Level level, TagKey<Block> blockTag) {
        return hitResult != null && hitResult.getType() == HitResult.Type.BLOCK && level.getBlockState(((BlockHitResult) hitResult).getBlockPos()).is(blockTag);
    }
}
