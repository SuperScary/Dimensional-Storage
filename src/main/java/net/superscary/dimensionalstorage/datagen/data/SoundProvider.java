package net.superscary.dimensionalstorage.datagen.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.superscary.dimensionalstorage.core.DimensionalStorage;
import net.superscary.dimensionalstorage.registries.DSSounds;
import net.superscary.dimensionalstorage.util.IDataProvider;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SoundProvider extends SoundDefinitionsProvider implements IDataProvider {

    public static final ArrayList<Map<String, String>> TRANSLATIONS = new ArrayList<>();

    public SoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, DimensionalStorage.MODID, helper);
    }

    @Override
    public @NotNull String getName() {
        return DimensionalStorage.NAME + " Sound Definitions";
    }

    @Override
    public void registerSounds() {
        add(DSSounds.VOID, make("void", "Void"));
        add(DSSounds.DARK_MATTER_THROW, make("dark_matter_throw", "Dark Matter Throw"));
    }

    private SoundDefinition make(String name, String translation) {
        Map<String, String> translationMap = new LinkedHashMap<>();
        translationMap.put("sound." + DimensionalStorage.MODID + "." + name, translation);
        TRANSLATIONS.add(translationMap);

        return SoundDefinition.definition()
                .with(sound(DimensionalStorage.MODID + ":" + name, SoundDefinition.SoundType.SOUND)
                        .volume(0.75f).pitch(1.0f).weight(1).stream(true))
                .subtitle("sound." + DimensionalStorage.MODID + "." + name).replace(true);
    }

}
