package net.superscary.dimensionalstorage.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.superscary.dimensionalstorage.registries.DSBlockEntities;

public class DarkMatterBlockEntity extends BlockEntity {

    public DarkMatterBlockEntity(BlockPos pos, BlockState blockState) {
        super(DSBlockEntities.DARK_MATTER_BE.get(), pos, blockState);
    }

}
