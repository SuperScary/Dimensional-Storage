package net.superscary.dimensionalstorage.registries;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.superscary.dimensionalstorage.core.DimensionalStorage;
import net.superscary.dimensionalstorage.item.DarkMatterItem;

public class DSDataComponents {

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, DimensionalStorage.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> STORAGE_CONTENTS =
            DATA_COMPONENT_TYPES.register("storage_contents",
                    () -> DataComponentType.<ItemContainerContents>builder()
                            .persistent(ItemContainerContents.CODEC)
                            .networkSynchronized(ItemContainerContents.STREAM_CODEC)
                            .cacheEncoding()
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DarkMatterItem.Stability>> DARKMATTER_STABILITY =
            DATA_COMPONENT_TYPES.register("darkmatter_stability",
                    () -> DataComponentType.<DarkMatterItem.Stability>builder()
                            .persistent(DarkMatterItem.Stability.CODEC)
                            .networkSynchronized(DarkMatterItem.Stability.STREAM_CODEC)
                            .build());

    private DSDataComponents() {}

}
