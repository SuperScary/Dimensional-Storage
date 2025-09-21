package net.superscary.dimensionalstorage.registries;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.superscary.dimensionalstorage.core.DimensionalStorage;

public class DSTags {

    public static class Items {

        public static final TagKey<Item> MATTER_ITEM = tag("matter_item");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(DimensionalStorage.getResource(name));
        }

    }

    public static class Blocks {

        public static final TagKey<Block> MATTER_BLOCK = tag("matter_block");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(DimensionalStorage.getResource(name));
        }
    }

}
