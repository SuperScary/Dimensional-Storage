package net.superscary.dimensionalstorage.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.superscary.dimensionalstorage.blockentity.DarkMatterBlockEntity;
import net.superscary.dimensionalstorage.core.DimensionalStorage;

import java.util.function.Supplier;

public class DSBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, DimensionalStorage.MODID);

    public static final Supplier<BlockEntityType<DarkMatterBlockEntity>> DARK_MATTER_BE =
            REGISTRY.register("dark_matter_be", () -> BlockEntityType.Builder.of(
                    DarkMatterBlockEntity::new, DSBlocks.DARK_MATTER_BLOCK.block()).build(null));

}
