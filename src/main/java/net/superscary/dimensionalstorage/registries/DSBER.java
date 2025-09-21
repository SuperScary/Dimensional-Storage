package net.superscary.dimensionalstorage.registries;

import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.superscary.dimensionalstorage.blockentity.ber.DarkMatterBlockEntityRenderer;

public class DSBER {

    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(DSBlockEntities.DARK_MATTER_BE.get(), DarkMatterBlockEntityRenderer::new);
    }

}
