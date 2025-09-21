package net.superscary.dimensionalstorage.blockentity.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.superscary.dimensionalstorage.blockentity.DarkMatterBlockEntity;
import net.superscary.dimensionalstorage.registries.shader.DSShaders;
import net.superscary.dimensionalstorage.render.DSRenderTypes;

public class DarkMatterBlockEntityRenderer implements BlockEntityRenderer<DarkMatterBlockEntity> {

    public DarkMatterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(DarkMatterBlockEntity be, float partialTick, PoseStack pose,
                       MultiBufferSource buffers, int packedLight, int packedOverlay) {
        if (DSShaders.DARK_MATTER == null) return;

        // Draw an inner cube (black hole core)
        pose.pushPose();
        // Slightly shrink to avoid z-fighting with neighbors
        float inset = 0.0025f;
        AABB box = new AABB(inset, inset, inset, 1f - inset, 1f - inset, 1f - inset);

        VertexConsumer vc = buffers.getBuffer(DSRenderTypes.darkMatter());

        // full black tint, non-emissive (shader ignores lighting anyway)
        float r = 0f, g = 0f, b = 0f, a = 1f;

        // draw 6 faces (helpers omitted for brevity)
        face(pose, vc, box, Direction.NORTH, r, g, b, a);
        face(pose, vc, box, Direction.SOUTH, r, g, b, a);
        face(pose, vc, box, Direction.WEST, r, g, b, a);
        face(pose, vc, box, Direction.EAST, r, g, b, a);
        face(pose, vc, box, Direction.UP, r, g, b, a);
        face(pose, vc, box, Direction.DOWN, r, g, b, a);

        pose.popPose();
    }

    private static void vert(PoseStack.Pose m, VertexConsumer vc,
                             float x, float y, float z, float r, float g, float b, float a) {
        vc.addVertex(m.pose(), x, y, z).setColor(r, g, b, a);
    }

    private static void face(PoseStack pose, VertexConsumer vc, AABB b, Direction d,
                             float r, float g, float bl, float a) {
        var m = pose.last();
        float x1 = (float) b.minX; float y1 = (float) b.minY; float z1 = (float) b.minZ;
        float x2 = (float) b.maxX; float y2 = (float) b.maxY; float z2 = (float) b.maxZ;

        switch (d) {
            case NORTH -> { // z = z1
                vert(m, vc, x1, y1, z1, r,g,bl,a);
                vert(m, vc, x2, y1, z1, r,g,bl,a);
                vert(m, vc, x2, y2, z1, r,g,bl,a);
                vert(m, vc, x1, y2, z1, r,g,bl,a);
            }
            case SOUTH -> { // z = z2
                vert(m, vc, x2, y1, z2, r,g,bl,a);
                vert(m, vc, x1, y1, z2, r,g,bl,a);
                vert(m, vc, x1, y2, z2, r,g,bl,a);
                vert(m, vc, x2, y2, z2, r,g,bl,a);
            }
            case WEST -> { // x = x1
                vert(m, vc, x1, y1, z2, r,g,bl,a);
                vert(m, vc, x1, y1, z1, r,g,bl,a);
                vert(m, vc, x1, y2, z1, r,g,bl,a);
                vert(m, vc, x1, y2, z2, r,g,bl,a);
            }
            case EAST -> { // x = x2
                vert(m, vc, x2, y1, z1, r,g,bl,a);
                vert(m, vc, x2, y1, z2, r,g,bl,a);
                vert(m, vc, x2, y2, z2, r,g,bl,a);
                vert(m, vc, x2, y2, z1, r,g,bl,a);
            }
            case UP -> { // y = y2
                vert(m, vc, x1, y2, z1, r,g,bl,a);
                vert(m, vc, x2, y2, z1, r,g,bl,a);
                vert(m, vc, x2, y2, z2, r,g,bl,a);
                vert(m, vc, x1, y2, z2, r,g,bl,a);
            }
            case DOWN -> { // y = y1
                vert(m, vc, x1, y1, z2, r,g,bl,a);
                vert(m, vc, x2, y1, z2, r,g,bl,a);
                vert(m, vc, x2, y1, z1, r,g,bl,a);
                vert(m, vc, x1, y1, z1, r,g,bl,a);
            }
        }
    }

    @Override
    public boolean shouldRenderOffScreen(DarkMatterBlockEntity be) {
        return false;
    }

}
