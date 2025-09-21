package net.superscary.dimensionalstorage.core;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.superscary.dimensionalstorage.registries.DSBER;
import net.superscary.dimensionalstorage.registries.shader.DSShaders;

public class DSClient extends DSBase {

    public DSClient(ModContainer container, IEventBus modEventBus) {
        super(container, modEventBus);

        modEventBus.addListener(DSShaders::onRegisterShaders);
    }

    @Override
    public Level getClientLevel() {
        return Minecraft.getInstance().level;
    }
}
