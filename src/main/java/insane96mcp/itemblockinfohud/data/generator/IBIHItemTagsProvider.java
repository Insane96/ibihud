package insane96mcp.itemblockinfohud.data.generator;

import insane96mcp.itemblockinfohud.ItemBlockInfoHud;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class IBIHItemTagsProvider extends ItemTagsProvider {
    public static final TagKey<Item> CARDINAL_DIRECTION = create("cardinal_direction");
    public static final TagKey<Item> DEPTH = create("depth");
    public static final TagKey<Item> TIME = create("time");
    public static final TagKey<Item> BIOME = create("biome");

    public IBIHItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture, CompletableFuture<TagLookup<Block>> tagLookupCompletableFuture, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, completableFuture, tagLookupCompletableFuture, modId, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(CARDINAL_DIRECTION)
                .add(Items.COMPASS);
        tag(DEPTH)
                .addOptional(ResourceLocation.parse("caverns_and_chasms:depth_gauge"))
                .addOptional(ResourceLocation.parse("supplementaries:altimeter"));
        tag(TIME)
                .add(Items.CLOCK);
        tag(BIOME);
    }

    public static TagKey<Item> create(String tagName) {
        return TagKey.create(Registries.ITEM, ItemBlockInfoHud.id(tagName));
    }
}
