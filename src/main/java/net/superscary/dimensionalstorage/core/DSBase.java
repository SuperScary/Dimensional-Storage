package net.superscary.dimensionalstorage.core;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import net.superscary.dimensionalstorage.DSTab;
import net.superscary.dimensionalstorage.registries.*;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

public abstract class DSBase implements DimensionalStorage {

    static DimensionalStorage INSTANCE;

    public DSBase(ModContainer container, IEventBus modEventBus) {
        if (INSTANCE != null) throw new IllegalStateException("Already initialized!");
        INSTANCE = this;

        register(modEventBus);

        modEventBus.addListener((RegisterEvent event) -> {
            if (event.getRegistryKey() == Registries.CREATIVE_MODE_TAB) DSTab.init(BuiltInRegistries.CREATIVE_MODE_TAB);
        });

        modEventBus.addListener(DSCapabilities::register);
        modEventBus.addListener(DSBER::register);
    }

    private void register(IEventBus modEventBus) {
        DSBlocks.REGISTRY.register(modEventBus);
        DSItems.REGISTRY.register(modEventBus);
        DSDataComponents.DATA_COMPONENT_TYPES.register(modEventBus);
        DSBlockEntities.REGISTRY.register(modEventBus);
    }

    @Override
    public Collection<ServerPlayer> getPlayers () {
        var server = getCurrentServer();

        if (server != null) {
            return server.getPlayerList().getPlayers();
        }

        return Collections.emptyList();
    }

    @Nullable
    @Override
    public MinecraftServer getCurrentServer () {
        return ServerLifecycleHooks.getCurrentServer();
    }

}
