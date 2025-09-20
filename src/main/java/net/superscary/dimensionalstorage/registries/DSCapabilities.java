package net.superscary.dimensionalstorage.registries;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.superscary.dimensionalstorage.block.BlockDefinition;
import net.superscary.dimensionalstorage.item.ItemDefinition;
import net.superscary.dimensionalstorage.storage.IStorage;

public class DSCapabilities {

    public static void register(RegisterCapabilitiesEvent event) {
        // Get items that implement IStorage
        var storageItems = DSItems.getItems().stream()
                .map(ItemDefinition::asItem)
                .filter(item -> item instanceof IStorage)
                .toArray(ItemLike[]::new);
        
        // Get blocks that implement IStorage
        var storageBlocks = DSBlocks.getBlocks().stream()
                .map(BlockDefinition::block)
                .filter(block -> block instanceof IStorage)
                .toArray(Block[]::new);
        
        // Only register if we have items/blocks to register
        if (storageItems.length > 0) {
            IStorage.registerItemHandlers(event, storageItems);
        }
        
        if (storageBlocks.length > 0) {
            IStorage.registerBlockHandlers(event, storageBlocks);
        }
    }

}
