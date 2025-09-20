package net.superscary.dimensionalstorage.datagen.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.superscary.dimensionalstorage.core.DimensionalStorage;
import net.superscary.dimensionalstorage.util.IDataProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.superscary.dimensionalstorage.registries.DSBlocks.*;

public class ModBlockTagGenerator extends BlockTagsProvider implements IDataProvider {

    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, DimensionalStorage.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(DRAKIUM_ORE.block())
                .add(DEEPSLATE_DRAKIUM_ORE.block())
                .add(END_STONE_DRAKIUM_ORE.block())
                .add(NETHER_DRAKIUM_ORE.block())
                .add(DRAKIUM_BLOCK.block())
                .add(DRAKIUM_BLOCK_RAW.block());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(DRAKIUM_ORE.block())
                .add(DEEPSLATE_DRAKIUM_ORE.block())
                .add(END_STONE_DRAKIUM_ORE.block())
                .add(NETHER_DRAKIUM_ORE.block())
                .add(DRAKIUM_BLOCK.block())
                .add(DRAKIUM_BLOCK_RAW.block());

        this.tag(Tags.Blocks.ORES)
                .add(DRAKIUM_ORE.block())
                .add(END_STONE_DRAKIUM_ORE.block())
                .add(DEEPSLATE_DRAKIUM_ORE.block())
                .add(NETHER_DRAKIUM_ORE.block());
    }
}
