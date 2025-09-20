package net.superscary.dimensionalstorage.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.superscary.dimensionalstorage.core.DimensionalStorage;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static net.superscary.dimensionalstorage.registries.DSBlocks.*;
import static net.superscary.dimensionalstorage.registries.DSItems.*;

public class SmeltingRecipes extends ModRecipeProvider {

    private static final int DEFAULT_SMELTING_TIME = 200;

    public SmeltingRecipes (PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, provider);
    }

    @Override
    public @NotNull String getName () {
        return DimensionalStorage.NAME + " Smelting Recipes";
    }

    @Override
    public void buildRecipes (@NotNull RecipeOutput consumer) {
        SimpleCookingRecipeBuilder
                .smelting(Ingredient.of(RAW_DRAKIUM_ORE), RecipeCategory.BREWING, DRAKIUM_INGOT, 0, DEFAULT_SMELTING_TIME)
                .unlockedBy("has_raw_drakium", has(RAW_DRAKIUM_ORE))
                .save(consumer, DimensionalStorage.getResource("smelting/drakium_ingot_from_raw_drakium_ore"));

        SimpleCookingRecipeBuilder
                .smelting(Ingredient.of(DRAKIUM_BLOCK_RAW), RecipeCategory.BREWING, DRAKIUM_BLOCK, 0, DEFAULT_SMELTING_TIME * 9)
                .unlockedBy("has_raw_drakium_block", has(DRAKIUM_BLOCK_RAW))
                .save(consumer, DimensionalStorage.getResource("smelting/drakium_block_from_raw_drakium_block"));
    }

}
