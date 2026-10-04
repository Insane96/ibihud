package insane96mcp.itemblockinfohud.data.generator;

import insane96mcp.itemblockinfohud.ItemBlockInfoHud;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class IBIHBlockTagsProvider extends BlockTagsProvider {
    public static final TagKey<Block> HUD_CARDINAL_DIRECTION = create("hud/cardinal_direction");
    public static final TagKey<Block> HUD_DEPTH = create("hud/depth");
    public static final TagKey<Block> HUD_TIME = create("hud/time");
    public static final TagKey<Block> HUD_BIOME = create("hud/biome");

    public IBIHBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(HUD_CARDINAL_DIRECTION);
        tag(HUD_DEPTH);
        tag(HUD_TIME);
        tag(HUD_BIOME);
    }

    public static TagKey<Block> create(String tagName) {
        return TagKey.create(Registries.BLOCK, ItemBlockInfoHud.id(tagName));
    }
}
