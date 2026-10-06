package insane96mcp.itemblockinfohud.data;

import insane96mcp.itemblockinfohud.ItemBlockInfoHud;
import insane96mcp.itemblockinfohud.util.Utils;
import net.minecraft.core.Holder;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class HUDInfo {
    private static List<HUDInfo> REGISTRY = new ArrayList<>();

    public ResourceLocation id;
    public TagKey<Item> itemTag;
    public Consumer<IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item>> itemTagConsumer;
    public TagKey<Block> blockTag;
    public Consumer<IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block>> blockTagConsumer;
    public Function<Player, Component> renderer;
    public ModConfigSpec.BooleanValue enabled;

    private HUDInfo(ResourceLocation id, Consumer<IntrinsicHolderTagsProvider.IntrinsicTagAppender<Item>> items, Consumer<IntrinsicHolderTagsProvider.IntrinsicTagAppender<Block>> blocks, Function<Player, Component> renderer) {
        this.id = id;
        this.itemTag = ItemTags.create(id);
        this.itemTagConsumer = items;
        this.blockTag = BlockTags.create(id);
        this.blockTagConsumer = blocks;
        this.renderer = renderer;
    }

    public HUDInfo(ResourceLocation id, Function<Player, Component> renderer) {
        this(id, tag -> {}, tag -> {}, renderer);
    }

    //TODO Allow external mods to register custom HUDInfo. Right now it's frozen too early
    static {
        REGISTRY.add(
                new HUDInfo(ItemBlockInfoHud.id("cardinal_direction"),
                        tag -> tag.add(Items.COMPASS),
                        tag -> {},
                        player -> {
                            float direction = Mth.wrapDegrees(player.getYHeadRot());
                            String d = Utils.getDirectionTranslatable(direction);
                            return Component.translatable(d);
                        }));
        REGISTRY.add(
                new HUDInfo(ItemBlockInfoHud.id("depth"),
                        tag ->
                                tag.addOptional(ResourceLocation.parse("caverns_and_chasms:depth_gauge"))
                                .addOptional(ResourceLocation.parse("supplementaries:altimeter")),
                        tag -> {},
                        player -> Component.translatable(ItemBlockInfoHud.lang("depth"), player.getBlockY())));
        REGISTRY.add(
                new HUDInfo(ItemBlockInfoHud.id("time"),
                        tag -> tag.add(Items.CLOCK),
                        tag -> tag.addOptional(ResourceLocation.parse("supplementaries:clock_block")),
                        player -> Component.translatable(ItemBlockInfoHud.lang("time"), Utils.ticksToTimeString(player.level().getDayTime()), player.level().getGameTime() / 24000)));
        REGISTRY.add(
                new HUDInfo(ItemBlockInfoHud.id("biome"),
                        tag -> {},
                        tag -> {},
                        player -> {
                            Holder<Biome> biome = player.level().getBiome(player.blockPosition());
                            if (biome.unwrapKey().isEmpty())
                                return Component.translatable(ItemBlockInfoHud.lang("failed_to_get_biome"));
                            String name = biome.unwrapKey().get().location().toString();
                            name = name.replace(':', '.');
                            return Component.translatable("biome." + name);
                        }));
    }

    public static List<HUDInfo> getRegistry() {
        return REGISTRY;
    }

    public static void freezeRegistry() {
        REGISTRY = Collections.unmodifiableList(REGISTRY);
    }

    public static ModConfigSpec.BooleanValue defineConfig(ModConfigSpec.Builder builder, HUDInfo hudInfo) {
        return builder
                .comment("If true, items in the inventory and blocks looked at in the %s tags will display this info.".formatted(hudInfo.itemTag.location()))
                .define(hudInfo.id.getNamespace() + "." + hudInfo.id.getPath(), true);
    }
}
