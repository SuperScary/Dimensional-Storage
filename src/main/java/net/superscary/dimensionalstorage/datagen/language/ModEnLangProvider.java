package net.superscary.dimensionalstorage.datagen.language;

import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.superscary.dimensionalstorage.core.DimensionalStorage;
import net.superscary.dimensionalstorage.datagen.data.SoundProvider;
import net.superscary.dimensionalstorage.registries.DSBlocks;
import net.superscary.dimensionalstorage.registries.DSItems;
import net.superscary.dimensionalstorage.util.IDataProvider;

public class ModEnLangProvider extends LanguageProvider implements IDataProvider {

    public ModEnLangProvider(DataGenerator generator) {
        super(generator.getPackOutput(), DimensionalStorage.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addManualStrings();

        // ITEMS
        for (var item : DSItems.getItems()) {
            add(item.asItem(), item.getEnglishName());
        }

        // BLOCKS
        for (var block : DSBlocks.getBlocks()) {
            add(block.block(), block.getEnglishName());
        }
    }

    protected void addManualStrings () {
        add("itemGroup." + DimensionalStorage.MODID, DimensionalStorage.NAME);
        add("item.dimensionalstorage.dark_matter.stable", "§aStable");
        add("item.dimensionalstorage.dark_matter.unstable", "§o§cUnstable");
        add("item.dimensionalstorage.dark_matter.inventory", "%sx: %s");
        add("item.dimensionalstorage.dark_matter_magnet.active", "§aActive");
        add("item.dimensionalstorage.dark_matter_magnet.inactive", "§o§cInactive");

        // Sounds translations
        for (var map : SoundProvider.TRANSLATIONS) {
            for (var entry : map.entrySet()) {
                add(entry.getKey(), entry.getValue());
            }
        }
    }
}
