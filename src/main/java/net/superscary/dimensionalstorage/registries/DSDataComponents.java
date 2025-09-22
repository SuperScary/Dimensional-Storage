package net.superscary.dimensionalstorage.registries;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.superscary.dimensionalstorage.core.DimensionalStorage;
import net.superscary.dimensionalstorage.item.DarkMatterItem;
import net.superscary.dimensionalstorage.item.DarkMatterMagnetItem;

public class DSDataComponents {

    public static final DeferredRegister<DataComponentType<?>> REGISTRY =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, DimensionalStorage.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> STORAGE_CONTENTS =
            REGISTRY.register("storage_contents",
                    () -> DataComponentType.<ItemContainerContents>builder()
                            .persistent(ItemContainerContents.CODEC)
                            .networkSynchronized(ItemContainerContents.STREAM_CODEC)
                            .cacheEncoding()
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DarkMatterItem.Stability>> DARKMATTER_STABILITY =
            REGISTRY.register("darkmatter_stability",
                    () -> DataComponentType.<DarkMatterItem.Stability>builder()
                            .persistent(DarkMatterItem.Stability.CODEC)
                            .networkSynchronized(DarkMatterItem.Stability.STREAM_CODEC)
                            .build());

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<DarkMatterMagnetItem.Active>> DARKMATTER_MAGNET_ACTIVE =
            REGISTRY.register("darkmatter_magnet_active",
                    () -> DataComponentType.<DarkMatterMagnetItem.Active>builder()
                            .persistent(DarkMatterMagnetItem.Active.CODEC)
                            .networkSynchronized(DarkMatterMagnetItem.Active.STREAM_CODEC)
                            .build());

    private DSDataComponents() {}

}
