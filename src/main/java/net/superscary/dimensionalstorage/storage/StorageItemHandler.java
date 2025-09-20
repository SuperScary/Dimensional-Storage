package net.superscary.dimensionalstorage.storage;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.items.IItemHandler;
import net.superscary.dimensionalstorage.registries.DSDataComponents;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class StorageItemHandler implements IItemHandler {

    private final ItemStack owner;
    private final int size;
    private final int slotLimit;

    public StorageItemHandler(ItemStack owner, int size) {
        this(owner, size, 64);
    }

    public StorageItemHandler(ItemStack owner, int size, int slotLimit) {
        this.owner = owner;
        this.size = Math.max(1, size);
        this.slotLimit = Math.max(1, slotLimit);
        ensureSize();
    }

    private ItemContainerContents contents() {
        ItemContainerContents c = owner.get(DSDataComponents.STORAGE_CONTENTS.get());
        return c != null ? c : ItemContainerContents.EMPTY;
    }

    private NonNullList<ItemStack> copyToList() {
        NonNullList<ItemStack> list = NonNullList.withSize(size, ItemStack.EMPTY);
        contents().copyInto(list);
        return list;
    }

    private void writeBack(List<ItemStack> list) {
        owner.set(DSDataComponents.STORAGE_CONTENTS.get(), ItemContainerContents.fromItems(list));
    }

    private void ensureSize() {
        NonNullList<ItemStack> list = copyToList();
        // copyToList already sized; writing back ensures component exists
        writeBack(list);
    }

    @Override
    public int getSlots() {
        return size;
    }

    @Override
    public @NotNull ItemStack getStackInSlot(int slot) {
        return copyToList().get(slot);
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) return ItemStack.EMPTY;

        NonNullList<ItemStack> list = copyToList();
        ItemStack existing = list.get(slot);

        int limit = Math.min(getSlotLimit(slot), stack.getMaxStackSize());
        if (!existing.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(existing, stack)) {
                return stack; // not stackable
            }
            limit -= existing.getCount();
        }
        if (limit <= 0) return stack;

        boolean overflow = stack.getCount() > limit;

        if (!simulate) {
            if (existing.isEmpty()) {
                list.set(slot, overflow ? stack.copyWithCount(limit) : stack.copy());
            } else {
                existing.grow(overflow ? limit : stack.getCount());
            }
            writeBack(list);
        }

        return overflow ? stack.copyWithCount(stack.getCount() - limit) : ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount <= 0) return ItemStack.EMPTY;

        NonNullList<ItemStack> list = copyToList();
        ItemStack existing = list.get(slot);
        if (existing.isEmpty()) return ItemStack.EMPTY;

        int toExtract = Math.min(amount, existing.getMaxStackSize());
        ItemStack out;
        if (existing.getCount() <= toExtract) {
            out = existing.copy();
            if (!simulate) list.set(slot, ItemStack.EMPTY);
        } else {
            out = existing.copyWithCount(toExtract);
            if (!simulate) list.set(slot, existing.copyWithCount(existing.getCount() - toExtract));
        }

        if (!simulate) writeBack(list);
        return out;
    }

    @Override
    public int getSlotLimit(int slot) {
        return slotLimit;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true;
    }
}
