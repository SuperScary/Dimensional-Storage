package net.superscary.dimensionalstorage.registries;

import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.superscary.dimensionalstorage.storage.IStorage;

public class DSCapabilities {

    public static void register(RegisterCapabilitiesEvent event) {
        IStorage.registerItemHandlers(event,
                DSItems.ANTIMATTER
        );
    }

}
