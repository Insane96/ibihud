package insane96mcp.itemblockinfohud.data.generator;

import insane96mcp.itemblockinfohud.ItemBlockInfoHud;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class IBIHBlockTagsProvider extends BlockTagsProvider {
    public static final TagKey<Block> CARDINAL_DIRECTION = create("cardinal_direction");
    public static final TagKey<Block> DEPTH = create("depth");
    public static final TagKey<Block> TIME = create("time");
    public static final TagKey<Block> BIOME = create("biome");

    public IBIHBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(CARDINAL_DIRECTION);
        tag(DEPTH);
        tag(TIME)
                .addOptional(ResourceLocation.parse("supplementaries:clock_block"));
        tag(BIOME);
    }

    public static TagKey<Block> create(String tagName) {
        return TagKey.create(Registries.BLOCK, ItemBlockInfoHud.id(tagName));
    }
}
