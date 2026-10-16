package insane96mcp.itemblockinfohud.data.generator;

import insane96mcp.itemblockinfohud.ItemBlockInfoHud;
import insane96mcp.itemblockinfohud.data.HUDInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class IBIHItemTagsProvider extends ItemTagsProvider {
    public static final TagKey<Item> SEARCHABLE_CONTAINERS = create("searchable_containers");

    public IBIHItemTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider, ItemBlockInfoHud.MOD_ID);
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
