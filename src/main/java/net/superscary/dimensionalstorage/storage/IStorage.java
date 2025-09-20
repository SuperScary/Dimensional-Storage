package net.superscary.dimensionalstorage.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface IStorage {

    default Optional<IItemHandler> createItemHandler(ItemStack stack) { return Optional.empty(); }

    default Optional<IItemHandler> createBlockHandler(Level level, BlockPos pos, BlockState state,
                                                      @Nullable BlockEntity be, @Nullable Direction side) {
        return Optional.empty();
    }

    static @Nullable IItemHandler getItemHandler(ItemStack stack) {
        return stack.getCapability(Capabilities.ItemHandler.ITEM);
    }

    static @Nullable IItemHandler getBlockHandler(Level level, BlockPos pos, @Nullable Direction side) {
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);
        return level.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, be, side);
    }

    static void registerItemHandlers(RegisterCapabilitiesEvent event, ItemLike... items) {
        event.registerItem(
                Capabilities.ItemHandler.ITEM,
                (stack, ctx) -> stack.getItem() instanceof IStorage s ? s.createItemHandler(stack).orElse(null) : null,
                items
        );
    }

    static void registerBlockHandlers(RegisterCapabilitiesEvent event, Block... blocks) {
        event.registerBlock(
                Capabilities.ItemHandler.BLOCK,
                (level, pos, state, be, side) -> state.getBlock() instanceof IStorage s
                        ? s.createBlockHandler(level, pos, state, be, side).orElse(null)
                        : null,
                blocks
        );
    }
}
