package net.superscary.dimensionalstorage.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.superscary.dimensionalstorage.item.base.BaseItem;
import org.jetbrains.annotations.NotNull;

public class LightMatterItem extends BaseItem {

    public LightMatterItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public boolean onEntityItemUpdate(@NotNull ItemStack stack, ItemEntity source) {
        final Level level = source.level();
        if (level.isClientSide || !source.isAlive()) return false;

        // Tuning knobs
        final double RADIUS = 6.0;           // how far to push (in blocks)
        final double PUSH_STRENGTH = 0.35;   // per-tick acceleration away from the center
        final double MAX_SPEED = 0.50;       // cap for stability
        final double DEAD_ZONE_SQR = 0.01;   // if basically on top of us, treat as very close

        final Vec3 center = source.position();
        final AABB box = source.getBoundingBox().inflate(RADIUS);

        for (ItemEntity it : level.getEntitiesOfClass(ItemEntity.class, box, e -> e != source && e.isAlive())) {
            // don't push other LightMatter items (prevents ping-pong)
            if (it.getItem().getItem() instanceof LightMatterItem) continue;

            Vec3 offset = it.position().subtract(center); // <— away from center
            double distSqr = offset.lengthSqr();

            if (distSqr <= DEAD_ZONE_SQR) {
                Vec3 dir = it.getDeltaMovement().lengthSqr() > 1.0e-6
                        ? it.getDeltaMovement().normalize()
                        : new Vec3(1, 0.15, 0).normalize();
                Vec3 kick = dir.scale(PUSH_STRENGTH * 2.0);
                Vec3 vel = it.getDeltaMovement().add(kick);
                double sp = vel.length();
                if (sp > MAX_SPEED) vel = vel.scale(MAX_SPEED / sp);
                it.setDeltaMovement(vel);
                it.hasImpulse = true;
                continue;
            }

            if (distSqr > 1.0e-6) {
                double dist = Math.sqrt(distSqr);

                double falloff = Math.max(0.0, 1.0 - (dist / RADIUS));

                Vec3 accel = offset.normalize().scale(PUSH_STRENGTH * falloff);
                Vec3 vel = it.getDeltaMovement().add(accel);

                // Cap speed
                double sp = vel.length();
                if (sp > MAX_SPEED) vel = vel.scale(MAX_SPEED / sp);

                it.setDeltaMovement(vel);
                it.hasImpulse = true;
            }
        }

        return false;
    }


    @Override
    public boolean canAttackBlock(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        return false;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }

}
