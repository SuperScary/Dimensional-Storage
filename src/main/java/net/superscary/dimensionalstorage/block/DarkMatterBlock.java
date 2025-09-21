package net.superscary.dimensionalstorage.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.superscary.dimensionalstorage.block.base.BaseBlock;
import net.superscary.dimensionalstorage.blockentity.DarkMatterBlockEntity;
import org.jetbrains.annotations.Nullable;

public class DarkMatterBlock extends BaseBlock implements EntityBlock {

    public DarkMatterBlock() {
        super(BlockBehaviour.Properties.of()
                .noCollission()
                .strength(-1.0F, 3600000.0F)
                .explosionResistance(6000000.0F)
                .lightLevel(state -> 0));
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DarkMatterBlockEntity(blockPos, blockState);
    }
}
