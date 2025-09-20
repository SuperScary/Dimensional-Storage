package net.superscary.dimensionalstorage.datagen.models;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.superscary.dimensionalstorage.block.BlockDefinition;
import net.superscary.dimensionalstorage.block.base.DecorativeBlock;
import net.superscary.dimensionalstorage.block.base.OreBlock;
import net.superscary.dimensionalstorage.core.DimensionalStorage;
import net.superscary.dimensionalstorage.registries.DSBlocks;

import java.util.List;
import java.util.function.BiConsumer;

public class BlockModelProvider extends ModBlockStateProvider {

    public static final ResourceLocation MACHINE_BOTTOM = DimensionalStorage.getResource("block/machine_states/machine_bottom");
    public static final ResourceLocation MACHINE_TOP = DimensionalStorage.getResource("block/machine_states/machine_top");
    public static final ResourceLocation MACHINE_SIDE = DimensionalStorage.getResource("block/machine_states/machine_side");

    public BlockModelProvider (PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, DimensionalStorage.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels () {
        for (var block : DSBlocks.getBlocks()) {
            if (block.block() instanceof OreBlock || block.block() instanceof DecorativeBlock) {
                blockWithItem(block);
            }
        }
    }

    private void blockWithItem (BlockDefinition<?> block) {
        err(List.of(block.id()));
        simpleBlockWithItem(block.block(), cubeAll(block.block()));
    }

    private void machine (BlockDefinition<?> block, String name) {
        var on = modLoc("block/" + name + "/" + name + "_on");
        var off = modLoc("block/" + name + "/" + name + "_off");

        err(List.of(on, off));

        BlockModelBuilder modelOn = models().cube("block/" + block.id().getPath() + "/" + block.id().getPath() + "_on", MACHINE_BOTTOM, MACHINE_TOP, on, MACHINE_SIDE, MACHINE_SIDE, MACHINE_SIDE).texture("particle", MACHINE_SIDE);
        BlockModelBuilder modelOff = models().cube("block/" + block.id().getPath() + "/" + block.id().getPath() + "_off", MACHINE_BOTTOM, MACHINE_TOP, off, MACHINE_SIDE, MACHINE_SIDE, MACHINE_SIDE).texture("particle", MACHINE_SIDE);
        directionBlock(block.block(), (state, builder) -> builder.modelFile(state.getValue(BlockStateProperties.POWERED) ? modelOn : modelOff));
    }

    private VariantBlockStateBuilder directionBlock (Block block, BiConsumer<BlockState, ConfiguredModel.Builder<?>> model) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        builder.forAllStates(state -> {
            ConfiguredModel.Builder<?> bld = ConfiguredModel.builder();
            model.accept(state, bld);
            applyRotationBld(bld, state.getValue(BlockStateProperties.FACING));
            return bld.build();
        });
        return builder;
    }

    private void applyRotationBld (ConfiguredModel.Builder<?> builder, Direction direction) {
        switch (direction) {
            case DOWN -> builder.rotationX(90);
            case UP -> builder.rotationX(-90);
            case NORTH -> {
            }
            case SOUTH -> builder.rotationY(180);
            case WEST -> builder.rotationY(270);
            case EAST -> builder.rotationY(90);
        }
    }

    /**
     * Ignores missing textures so we can still build data without the texture present.
     * @param list a list of ResourceLocations that we know will exist but currently don't.
     */
    private void err (List<ResourceLocation> list) {
        for (var res : list) {
            existingFileHelper.trackGenerated(res, PackType.CLIENT_RESOURCES, ".png", "textures");

        }
    }

}
