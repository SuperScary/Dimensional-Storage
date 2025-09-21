package net.superscary.dimensionalstorage.registries.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;
import net.superscary.dimensionalstorage.core.DimensionalStorage;

import java.io.IOException;

public class DSShaders {

    public static ShaderInstance DARK_MATTER;

    public static void onRegisterShaders(RegisterShadersEvent event) {
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(),
                            DimensionalStorage.getResource("dark_matter"),
                            DefaultVertexFormat.POSITION_COLOR),
                    shader -> DARK_MATTER = shader);
        } catch (IOException ignored) {}

    }

}
