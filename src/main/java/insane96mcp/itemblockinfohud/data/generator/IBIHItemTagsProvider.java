package insane96mcp.itemblockinfohud.data.generator;

import insane96mcp.itemblockinfohud.ItemBlockInfoHud;
import insane96mcp.itemblockinfohud.data.HUDInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class IBIHItemTagsProvider extends ItemTagsProvider {
    public static final TagKey<Item> SEARCHABLE_CONTAINERS = create("searchable_containers");

    public IBIHItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture, CompletableFuture<TagLookup<Block>> tagLookupCompletableFuture, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, completableFuture, tagLookupCompletableFuture, modId, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        HUDInfo.getRegistry().forEach(hudInfo -> hudInfo.itemTagConsumer.accept(tag(hudInfo.itemTag)));

        tag(SEARCHABLE_CONTAINERS)
                .add(Items.BUNDLE);
    }

    public static TagKey<Item> create(String tagName) {
        return TagKey.create(Registries.ITEM, ItemBlockInfoHud.id(tagName));
    }
}
