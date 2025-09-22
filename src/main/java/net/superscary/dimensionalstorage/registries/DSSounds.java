package net.superscary.dimensionalstorage.registries;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.superscary.dimensionalstorage.core.DimensionalStorage;

import java.util.function.Supplier;

public class DSSounds {

    public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, DimensionalStorage.MODID);

    public static final Supplier<SoundEvent> VOID = register("void");
    public static final Supplier<SoundEvent> DARK_MATTER_THROW = register("dark_matter_throw");

    private static Supplier<SoundEvent> register(String name) {
        var id = DimensionalStorage.getResource(name);
        return REGISTRY.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

}
