package net.superscary.dimensionalstorage.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLEnvironment;
import net.superscary.dimensionalstorage.item.base.BaseItem;
import net.superscary.dimensionalstorage.registries.DSDataComponents;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DarkMatterMagnetItem extends BaseItem {

    public DarkMatterMagnetItem(Properties properties) {
        super(properties.stacksTo(1)
                .component(DSDataComponents.DARKMATTER_MAGNET_ACTIVE.get(), Active.INACTIVE));
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (this.getActive(stack) == Active.INACTIVE) return;
        if (!(entity instanceof Player player)) return;
        if (level.isClientSide()) return;

        final double RADIUS = 6.0;            // how far to pull (in blocks)
        final double PULL_STRENGTH = 0.35;    // per-tick acceleration toward the antimatter
        final double MAX_SPEED = 0.5;        // cap speed so things don’t yeet around
        final double ABSORB_DIST_SQ = 0.04; // <= 0.2 blocks → add to inventory

        final Vec3 center = player.position();
        final AABB box = player.getBoundingBox().inflate(RADIUS);

        for (ItemEntity it : level.getEntitiesOfClass(ItemEntity.class, box, Entity::isAlive)) {
            if (it == null) continue;

            // (Optional) Don’t pull other magnets to prevent ping-pong
            if (it.getItem().getItem() instanceof DarkMatterMagnetItem) continue;

            Vec3 toCenter = center.subtract(it.position());
            double distSq = toCenter.lengthSqr();

            // Close enough? Try to add to player’s inventory.
            if (distSq <= ABSORB_DIST_SQ) {
                ItemStack entityStack = it.getItem();      // this is the live stack inside the ItemEntity
                int before = entityStack.getCount();

                boolean anyAdded = player.getInventory().add(entityStack);
                player.getInventory().setChanged();

                // If fully moved, the entity’s stack becomes empty → discard the entity
                if (entityStack.isEmpty()) {
                    it.discard();
                } else if (anyAdded && entityStack.getCount() != before) {
                    // Partially added: ItemEntity already holds the same (now smaller) stack,
                    // nothing else needed. (You could play a subtle sound if you like.)
                }

                continue;
            }

            // Apply pull force
            if (distSq > 1.0e-6) {
                Vec3 accel = toCenter.normalize().scale(PULL_STRENGTH);
                Vec3 vel = it.getDeltaMovement().add(accel);

                // Cap speed
                double sp = vel.length();
                if (sp > MAX_SPEED) vel = vel.scale(MAX_SPEED / sp);

                it.setDeltaMovement(vel);
                it.hasImpulse = true;
            }
        }
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return this.getActive(stack) == Active.ACTIVE;
    }

    @Override
    public boolean onEntityItemUpdate(@NotNull ItemStack stack, ItemEntity source) {
        final Level level = source.level();
        if (level.isClientSide || !source.isAlive()) return false;
        if (this.getActive(stack) == Active.INACTIVE) return false;

        final double RADIUS = 6.0;            // how far to pull (in blocks)
        final double PULL_STRENGTH = 0.35;    // per-tick acceleration toward the antimatter
        final double MAX_SPEED = 0.5;        // cap speed so things don’t yeet around
        final double KILL_DIST_SQR = 0.04;    // <= 0.2 blocks → vaporize

        final Vec3 center = source.position();
        final AABB box = source.getBoundingBox().inflate(RADIUS);

        for (ItemEntity it : level.getEntitiesOfClass(ItemEntity.class, box, e -> e != source && e.isAlive())) {
            if (it.getItem().getItem() instanceof DarkMatterMagnetItem) continue;

            Vec3 toCenter = center.subtract(it.position());
            double distSqr = toCenter.lengthSqr();

            if (distSqr <= KILL_DIST_SQR) {
                continue;
            }

            if (distSqr > 1.0e-6) {
                Vec3 accel = toCenter.normalize().scale(PULL_STRENGTH);
                Vec3 vel = it.getDeltaMovement().add(accel);

                double speed = vel.length();
                if (speed > MAX_SPEED) vel = vel.scale(MAX_SPEED / speed);

                it.setDeltaMovement(vel);
                it.hasImpulse = true;
            }
        }

        return false;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level,  @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack inHand = player.getItemInHand(usedHand);

        if (!this.components().has(DSDataComponents.DARKMATTER_MAGNET_ACTIVE.get())) return InteractionResultHolder.pass(inHand);

        if (player.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                if (this.getActive(inHand) == Active.INACTIVE) {
                    this.setActive(Active.ACTIVE, inHand);
                } else {
                    this.setActive(Active.INACTIVE, inHand);
                }
                return InteractionResultHolder.success(inHand);
            }
        }

        return InteractionResultHolder.sidedSuccess(inHand, level.isClientSide);
    }

    @Override
    public boolean canAttackBlock(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        return false;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("item.dimensionalstorage.dark_matter_magnet." + this.getActive(stack).name().toLowerCase()));
    }

    public void setActive(Active active, ItemStack stack) {
        stack.set(DSDataComponents.DARKMATTER_MAGNET_ACTIVE.get(), active);
    }

    public Active getActive(ItemStack stack) {
        Active s = stack.get(DSDataComponents.DARKMATTER_MAGNET_ACTIVE.get());
        if (s == null) return Active.INACTIVE;
        return s;
    }

    public enum Active implements StringRepresentable {
        ACTIVE,
        INACTIVE;

        @Override
        public @NotNull String getSerializedName() {
            return this.name().toLowerCase();
        }

        public static final Codec<Active> CODEC = StringRepresentable.fromEnum(Active::values);

        public static final StreamCodec<ByteBuf, Active> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
    }
}
