package net.superscary.dimensionalstorage.item;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.items.IItemHandler;
import net.superscary.dimensionalstorage.item.base.BaseItem;
import net.superscary.dimensionalstorage.registries.DSDataComponents;
import net.superscary.dimensionalstorage.storage.IStorage;
import net.superscary.dimensionalstorage.storage.StorageItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class DarkMatterItem extends BaseItem implements IStorage {

    public DarkMatterItem(Properties properties) {
        super(properties.stacksTo(1).rarity(Rarity.EPIC)
                .component(DSDataComponents.DARKMATTER_STABILITY.get(), Stability.UNSTABLE));
    }

    @Override
    public boolean onEntityItemUpdate(@NotNull ItemStack stack, ItemEntity source) {
        final Level level = source.level();
        if (level.isClientSide || !source.isAlive()) return false;

        var handler = IStorage.getItemHandler(stack);

        if (handler == null) return false;

        final double RADIUS = 6.0;            // how far to pull (in blocks)
        final double PULL_STRENGTH = 0.35;    // per-tick acceleration toward the antimatter
        final double MAX_SPEED = 0.5;        // cap speed so things don’t yeet around
        final double KILL_DIST_SQR = 0.04;    // <= 0.2 blocks → vaporize

        final Vec3 center = source.position();
        final AABB box = source.getBoundingBox().inflate(RADIUS);

        for (ItemEntity it : level.getEntitiesOfClass(ItemEntity.class, box, e -> e != source && e.isAlive())) {
            if (it.getItem().getItem() instanceof DarkMatterItem) continue;

            Vec3 toCenter = center.subtract(it.position());
            double distSqr = toCenter.lengthSqr();

            if (distSqr <= KILL_DIST_SQR) {
                // level.playSound(null, it.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5f, 1.2f);
                if (handler.insertItem(0, it.getItem(), true).isEmpty() || handler.insertItem(0, it.getItem(), true).is(it.getItem().getItem())) {
                    handler.insertItem(0, it.getItem(), false);
                }
                it.discard();
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

        if (!this.components().has(DSDataComponents.DARKMATTER_STABILITY.get())) return InteractionResultHolder.pass(inHand);

        if (player.isShiftKeyDown() && !FMLEnvironment.production) {
            if (!level.isClientSide()) {
                if (this.getStability(inHand) == Stability.UNSTABLE) {
                    this.setStability(Stability.STABLE, inHand);
                } else {
                    this.setStability(Stability.UNSTABLE, inHand);
                }
                return InteractionResultHolder.success(inHand);
            }
        }

        if (this.getStability(inHand) == Stability.UNSTABLE) return InteractionResultHolder.pass(inHand);
        var handler = IStorage.getItemHandler(inHand);
        if (handler == null) return InteractionResultHolder.pass(inHand);

        // nothing to throw
        if (handler.getStackInSlot(0).isEmpty()) {
            return InteractionResultHolder.pass(inHand);
        }

        if (!level.isClientSide) {
            ItemStack toThrow = handler.getStackInSlot(0);
            if (toThrow.isEmpty()) {
                return InteractionResultHolder.pass(inHand);
            }
            toThrow = handler.extractItem(0, 1, false).copy();

            ItemEntity itemEnt = getItemEntity(level, player, toThrow);
            itemEnt.hasImpulse = true;

            level.addFreshEntity(itemEnt);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.SNOWBALL_THROW, SoundSource.PLAYERS, 0.8f,
                    0.02f);

            player.getCooldowns().addCooldown(this, 6); // 0.3s
        }

        return InteractionResultHolder.sidedSuccess(inHand, level.isClientSide);
    }

    private static @NotNull ItemEntity getItemEntity(@NotNull Level level, @NotNull Player player, ItemStack toThrow) {
        Vec3 look = player.getLookAngle();
        Vec3 eye = player.getEyePosition();
        double spawnOffset = 0.5; // half a block forward
        double spawnX = eye.x + look.x * spawnOffset;
        double spawnY = eye.y - 0.1; // slightly below eye
        double spawnZ = eye.z + look.z * spawnOffset;

        ItemEntity itemEnt = new ItemEntity(level, spawnX, spawnY, spawnZ, toThrow);

        // Throwing physics
        double power = 0.6;
        double inaccuracy = 0.02;
        RandomSource rng = level.getRandom();

        // Forward velocity and tiny random spray
        double vx = look.x * power + (rng.nextDouble() - 0.5) * inaccuracy;
        double vy = look.y * power + (rng.nextDouble() - 0.5) * inaccuracy + 0.05; // slightly upwards
        double vz = look.z * power + (rng.nextDouble() - 0.5) * inaccuracy;

        itemEnt.setDeltaMovement(vx, vy, vz);
        itemEnt.setPickUpDelay(10);
        itemEnt.setThrower(player);
        return itemEnt;
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
        tooltipComponents.add(Component.translatable("item.dimensionalstorage.darkmatter." + this.getStability(stack).name().toLowerCase()));
    }

    @Override
    public Optional<IItemHandler> createItemHandler(ItemStack stack) {
        return Optional.of(new StorageItemHandler(stack, 1));
    }

    public void setStability(Stability stability, ItemStack stack) {
        stack.set(DSDataComponents.DARKMATTER_STABILITY.get(), stability);
    }

    public Stability getStability(ItemStack stack) {
        Stability s = stack.get(DSDataComponents.DARKMATTER_STABILITY.get());
        if (s == null) return Stability.UNSTABLE;
        return s;
    }

    public enum Stability implements StringRepresentable {
        STABLE,
        UNSTABLE,
        FAILING;

        @Override
        public @NotNull String getSerializedName() {
            return this.name().toLowerCase();
        }

        public static final Codec<Stability> CODEC = StringRepresentable.fromEnum(Stability::values);

        public static final StreamCodec<ByteBuf, Stability> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);
    }
}
