package net.superscary.dimensionalstorage.registries;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.superscary.dimensionalstorage.core.DimensionalStorage;
import net.superscary.dimensionalstorage.item.AntimatterItem;

public class DSDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, DimensionalStorage.MODID);

    /** Per-stack inventory for storage-enabled items. */
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> STORAGE_CONTENTS =
            DATA_COMPONENT_TYPES.register("storage_contents",
                    () -> DataComponentType.<ItemContainerContents>builder()
                            .persistent(ItemContainerContents.CODEC)
                            .networkSynchronized(ItemContainerContents.STREAM_CODEC)
                            .cacheEncoding()
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<AntimatterItem.Stability>> ANTIMATTER_STABILITY =
            DATA_COMPONENT_TYPES.register("antimatter_stability",
                    () -> DataComponentType.<AntimatterItem.Stability>builder()
                            .persistent(AntimatterItem.Stability.CODEC)
                            .networkSynchronized(AntimatterItem.Stability.STREAM_CODEC)
                            .build());

    private DSDataComponents() {}

}
