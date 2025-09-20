package net.superscary.dimensionalstorage.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.superscary.dimensionalstorage.core.DimensionalStorage;
import net.superscary.dimensionalstorage.datagen.language.ModEnLangProvider;
import net.superscary.dimensionalstorage.datagen.loot.ModLootTableProvider;
import net.superscary.dimensionalstorage.datagen.models.BlockModelProvider;
import net.superscary.dimensionalstorage.datagen.models.ModItemModelProvider;
import net.superscary.dimensionalstorage.datagen.recipes.CraftingRecipes;
import net.superscary.dimensionalstorage.datagen.recipes.SmeltingRecipes;
import net.superscary.dimensionalstorage.datagen.tags.ModBlockTagGenerator;
import net.superscary.dimensionalstorage.datagen.tags.ModItemTagGenerator;
import net.superscary.dimensionalstorage.datagen.world.WorldGenProvider;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

@EventBusSubscriber(modid = DimensionalStorage.MODID)
public class DataGenerators {

    @SubscribeEvent
    public static void gather (GatherDataEvent event) {
        var generator = event.getGenerator();
        var registries = event.getLookupProvider();
        var pack = generator.getVanillaPack(true);
        var existingFileHelper = event.getExistingFileHelper();
        var localization = new ModEnLangProvider(generator);

        // WORLD GENERATION
        pack.addProvider(output -> new WorldGenProvider(output, registries));

        // SOUNDS
        //pack.addProvider(packOutput -> new SoundProvider(packOutput, existingFileHelper));

        // LOOT TABLE
        pack.addProvider(bindRegistries(ModLootTableProvider::new, registries));

        // POI
        //pack.addProvider(packOutput -> new FMPoiTagGenerator(packOutput, registries, existingFileHelper));

        // TAGS
        var blockTagsProvider = pack.addProvider(pOutput -> new ModBlockTagGenerator(pOutput, registries, existingFileHelper));
        pack.addProvider(pOutput -> new ModItemTagGenerator(pOutput, registries, blockTagsProvider.contentsGetter(), existingFileHelper));
        //pack.addProvider(packOutput -> new FMFluidTagGenerator(packOutput, registries, existingFileHelper));

        // MODELS & STATES
        pack.addProvider(pOutput -> new BlockModelProvider(pOutput, existingFileHelper));
        pack.addProvider(pOutput -> new ModItemModelProvider(pOutput, existingFileHelper));

        // RECIPES
        pack.addProvider(bindRegistries(CraftingRecipes::new, registries));
        pack.addProvider(bindRegistries(SmeltingRecipes::new, registries));

        // LOCALIZATION MUST RUN LAST
        pack.addProvider(output -> localization);
    }

    private static <T extends DataProvider> DataProvider.Factory<T> bindRegistries (BiFunction<PackOutput, CompletableFuture<HolderLookup.Provider>, T> factory, CompletableFuture<HolderLookup.Provider> factories) {
        return pOutput -> factory.apply(pOutput, factories);
    }

}
