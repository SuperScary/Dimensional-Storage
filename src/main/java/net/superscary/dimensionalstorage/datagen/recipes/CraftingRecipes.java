package net.superscary.dimensionalstorage.datagen.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.superscary.dimensionalstorage.core.DimensionalStorage;
import net.superscary.dimensionalstorage.item.DarkMatterMagnetItem;
import net.superscary.dimensionalstorage.registries.DSDataComponents;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static net.minecraft.world.level.block.Blocks.LAPIS_BLOCK;
import static net.minecraft.world.level.block.Blocks.REDSTONE_BLOCK;
import static net.superscary.dimensionalstorage.registries.DSBlocks.*;
import static net.superscary.dimensionalstorage.registries.DSItems.*;

public class CraftingRecipes extends ModRecipeProvider {

    public CraftingRecipes(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(packOutput, provider);
    }

    @Override
    public @NotNull String getName() {
        return DimensionalStorage.NAME + " Crafting Recipes";
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput consumer) {
        magnet(consumer);
        misc(consumer);
    }

    private void magnet(RecipeOutput consumer) {
        ItemStack dm = new ItemStack(DARK_MATTER);
        dm.set(DSDataComponents.DARKMATTER_MAGNET_ACTIVE.get(), DarkMatterMagnetItem.Active.ACTIVE);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, DARK_MATTER_MAGNET, 1)
                .pattern("IIR")
                .pattern("D  ")
                .pattern("IIL")
                .define('I', DRAKIUM_INGOT)
                .define('R', REDSTONE_BLOCK)
                .define('D', Ingredient.of(dm))
                .define('L', LAPIS_BLOCK)
                .unlockedBy("has_drakium_ingot", has(DRAKIUM_INGOT))
                .unlockedBy("has_drakium_dark_matter", has(DARK_MATTER))
                .unlockedBy("has_redstone_block", has(REDSTONE_BLOCK))
                .unlockedBy("has_lapis_block", has(LAPIS_BLOCK))
                .save(consumer, DimensionalStorage.getResource("crafting/magnet"));
    }

    protected void misc (RecipeOutput consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DRAKIUM_BLOCK, 1)
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', DRAKIUM_INGOT)
                .unlockedBy("has_drakium_ingot", has(DRAKIUM_INGOT))
                .save(consumer, DimensionalStorage.getResource("crafting/drakium_block_from_ingot"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DRAKIUM_BLOCK_RAW, 1)
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', RAW_DRAKIUM_ORE)
                .unlockedBy("has_raw_drakium", has(RAW_DRAKIUM_ORE))
                .save(consumer, DimensionalStorage.getResource("crafting/raw_drakium_block_from_raw_ore"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, DRAKIUM_INGOT, 1)
                .pattern("III")
                .pattern("III")
                .pattern("III")
                .define('I', DRAKIUM_NUGGET)
                .unlockedBy("has_drakium_nugget", has(DRAKIUM_NUGGET))
                .save(consumer, DimensionalStorage.getResource("crafting/drakium_ingot_from_nugget"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DRAKIUM_NUGGET, 9)
                .requires(DRAKIUM_INGOT)
                .unlockedBy("has_drakium_ingot", has(DRAKIUM_INGOT))
                .save(consumer, DimensionalStorage.getResource("crafting/drakium_nugget_from_ingot"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DRAKIUM_INGOT, 9)
                .requires(DRAKIUM_BLOCK)
                .unlockedBy("has_drakium_block", has(DRAKIUM_BLOCK))
                .save(consumer, DimensionalStorage.getResource("crafting/drakium_ingot_from_block"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, RAW_DRAKIUM_ORE, 9)
                .requires(DRAKIUM_BLOCK_RAW)
                .unlockedBy("has_drakium_block_raw", has(DRAKIUM_BLOCK_RAW))
                .save(consumer, DimensionalStorage.getResource("crafting/raw_drakium_from_raw_drakium_block"));
    }
}
