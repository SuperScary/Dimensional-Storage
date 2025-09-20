package net.superscary.dimensionalstorage.world.modifiers;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.superscary.dimensionalstorage.core.DimensionalStorage;

import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> DRAKIUM_ORE_KEY = registerKey("drakium_ore");
    public static final ResourceKey<PlacedFeature> DRAKIUM_END_ORE_KEY = registerKey("drakium_ore_end");
    public static final ResourceKey<PlacedFeature> DRAKIUM_NETHER_ORE_KEY = registerKey("drakium_ore_nether");

    public static void bootstrap (BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, DRAKIUM_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DRAKIUM_ORE_KEY),
                ModOrePlacement.rareOrePlacement(16, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));

        register(context, DRAKIUM_END_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DRAKIUM_END_ORE_KEY),
                ModOrePlacement.commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));

        register(context, DRAKIUM_NETHER_ORE_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DRAKIUM_NETHER_ORE_KEY),
                ModOrePlacement.commonOrePlacement(16, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));
    }

    public static ResourceKey<PlacedFeature> registerKey (String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, DimensionalStorage.getResource(name));
    }

    private static void register (BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration, List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

}