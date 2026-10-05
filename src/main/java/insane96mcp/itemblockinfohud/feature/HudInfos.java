package insane96mcp.itemblockinfohud.feature;

import insane96mcp.insanelib.core.feature.Feature;
import insane96mcp.insanelib.core.feature.LoadFeature;
import insane96mcp.insanelib.core.feature.config.Config;
import insane96mcp.itemblockinfohud.ItemBlockInfoHud;
import insane96mcp.itemblockinfohud.data.generator.IBIHBlockTagsProvider;
import insane96mcp.itemblockinfohud.data.generator.IBIHItemTagsProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@LoadFeature(name = "HUD Infos",
        description = "Adds various infos on top left of the screen",
        canBeDisabled = false)
public class HudInfos extends Feature {
    @Config(description = "If true, items in the inventory and blocks looked at in the itemblockinfohud:cardinal_direction tags will display the cardinal direction.")
    public static Boolean cardinalDirection = true;
    @Config(description = "If true, items in the inventory and blocks looked at in the itemblockinfohud:depth tags will display the current Y level")
    public static Boolean depth = true;
    @Config(description = "If true, items in the inventory and blocks looked at in the itemblockinfohud:time tags will display the time of day")
    public static Boolean time = true;
    @Config(description = "If true, items in the inventory and blocks looked at in the itemblockinfohud:biome tags will display the current biome")
    public static Boolean biome = true;

    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ItemBlockInfoHud.id("hud_infos"), (guiGraphics, deltaTracker) -> {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null || mc.getDebugOverlay().showDebugScreen() || !Feature.isEnabled(HudInfos.class) || mc.options.hideGui)
                return;

            ArrayList<String> toDraw = new ArrayList<>();
            tryRenderCardinalDirection(player, toDraw);
            tryRenderDepth(player, toDraw);
            tryRenderBiome(player, toDraw);
            tryRenderTime(player, toDraw);

            int y = 2;
            for (String s : toDraw) {
                guiGraphics.drawString(mc.font, s, 2, y, 0xFFFFFF);
                y += mc.font.lineHeight + 1;
            }
        });
    }

    public static void tryRenderCardinalDirection(Player player, List<String> toDraw) {
        if (!cardinalDirection
                || !shouldRender(player, Minecraft.getInstance().hitResult, IBIHItemTagsProvider.CARDINAL_DIRECTION, IBIHBlockTagsProvider.CARDINAL_DIRECTION))
            return;

        renderCardinalDirection(player, toDraw);
    }

    public static void renderCardinalDirection(Player player, List<String> toDraw) {
        float direction = Mth.wrapDegrees(player.getYHeadRot());
        String d = getDirectionTranslatable(direction);
        toDraw.add(Component.translatable(d).getString());
    }

    public static void tryRenderDepth(Player player, List<String> toDraw) {
        if (!depth
                || !shouldRender(player, Minecraft.getInstance().hitResult, IBIHItemTagsProvider.DEPTH, IBIHBlockTagsProvider.DEPTH))
            return;

        renderDepth(player, toDraw);
    }

    public static void renderDepth(Player player, List<String> toDraw) {
        toDraw.add(Component.translatable("hud_info.depth", player.getBlockY()).getString());
    }

    public static void tryRenderBiome(Player player, List<String> toDraw) {
        if (!biome
                || !shouldRender(player, Minecraft.getInstance().hitResult, IBIHItemTagsProvider.BIOME, IBIHBlockTagsProvider.BIOME))
            return;

        renderBiome(player, toDraw);
    }

    public static void renderBiome(Player player, List<String> toDraw) {
        Holder<Biome> biome = player.level().getBiome(player.blockPosition());
        String name = biome.unwrapKey().get().location().toString();
        name = name.replace(':', '.');
        toDraw.add(Component.translatable("biome." + name).getString());
    }

    public static void tryRenderTime(Player player, List<String> toDraw) {
        if (!time
                || !shouldRender(player, Minecraft.getInstance().hitResult, IBIHItemTagsProvider.TIME, IBIHBlockTagsProvider.TIME))
            return;

        renderTime(player, toDraw);
    }

    public static void renderTime(Player player, List<String> toDraw) {
        long dayTime = player.level().getDayTime();
        toDraw.add(Component.translatable("hud_info.time", ticksToTimeString(dayTime), player.level().getGameTime() / 24000).getString());
    }

    public static String ticksToTimeString(long ticks) {
        int hours = (int) ((ticks + 6000) % 24000 / 1000);
        String minutes = String.format("%02d", ticks % 1000 / 20);
        return hours + ":" + minutes;
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

    private static @NotNull String getDirectionTranslatable(float direction) {
        String d = "";
        if (direction > -22.5 && direction <= 22.5)
            d = "hud_info.cardinal_direction.south";
        else if (direction > 22.5 && direction <= 67.5)
            d = "hud_info.cardinal_direction.south_west";
        else if (direction > 67.5 && direction <= 112.5)
            d = "hud_info.cardinal_direction.west";
        else if (direction > 112.5 && direction <= 157.5)
            d = "hud_info.cardinal_direction.north_west";
        else if (direction > 157.5 || direction <= -157.5)
            d = "hud_info.cardinal_direction.north";
        else if (direction > -157.5 && direction <= -112.5)
            d = "hud_info.cardinal_direction.north_east";
        else if (direction > -112.5 && direction <= -67.5)
            d = "hud_info.cardinal_direction.east";
        else if (direction > -67.5 && direction <= -22.5)
            d = "hud_info.cardinal_direction.south_east";
        return d;
    }
}
