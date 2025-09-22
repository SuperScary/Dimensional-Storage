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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface IStorage {

    default Optional<IItemHandler> createItemHandler(ItemStack stack) {
        return Optional.empty();
    }

    default Optional<IItemHandler> createBlockHandler(Level level, BlockPos pos, BlockState state,
                                                      @Nullable BlockEntity be, @Nullable Direction side) {
        return Optional.empty();
    }

    static @Nullable IItemHandler getItemHandler(ItemStack stack) {
        return stack.getCapability(Capabilities.ItemHandler.ITEM);
    }

    /**
     * Returns true if the entire stack can go into the given slot (no remainder).
     */
    static boolean canInsertAll(@NotNull IItemHandler handler, int slot, @NotNull ItemStack toInsert) {
        if (toInsert.isEmpty()) return false;
        if (slot < 0 || slot >= handler.getSlots()) return false;
        if (!handler.isItemValid(slot, toInsert)) return false;

        int limit = Math.min(handler.getSlotLimit(slot), toInsert.getMaxStackSize());
        if (limit <= 0) return false;

        // SIMULATE the insert; this honors any custom handler logic.
        ItemStack remainder = handler.insertItem(slot, toInsert.copy(), true);
        return remainder.isEmpty();
    }

    /**
     * Returns true if at least one item from the stack could be inserted into the slot.
     */
    static boolean canInsertAny(@NotNull IItemHandler handler, int slot, @NotNull ItemStack toInsert) {
        if (toInsert.isEmpty()) return false;
        if (slot < 0 || slot >= handler.getSlots()) return false;
        if (!handler.isItemValid(slot, toInsert)) return false;

        ItemStack remainder = handler.insertItem(slot, new ItemStack(toInsert.getItem(), toInsert.getCount()), true);
        return remainder.isEmpty();
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
