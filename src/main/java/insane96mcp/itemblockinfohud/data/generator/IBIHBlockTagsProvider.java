package insane96mcp.itemblockinfohud.data.generator;

import insane96mcp.itemblockinfohud.ItemBlockInfoHud;
import insane96mcp.itemblockinfohud.data.HUDInfo;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class IBIHBlockTagsProvider extends BlockTagsProvider {
    public IBIHBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ItemBlockInfoHud.MOD_ID);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        HUDInfo.getRegistry().forEach(hudInfo -> hudInfo.blockTagConsumer.accept(tag(hudInfo.blockTag)));
    }

    public static TagKey<Block> create(String tagName) {
        return TagKey.create(Registries.BLOCK, ItemBlockInfoHud.id(tagName));
    }
}
