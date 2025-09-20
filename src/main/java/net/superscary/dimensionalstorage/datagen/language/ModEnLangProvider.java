package net.superscary.dimensionalstorage.datagen.language;

import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.superscary.dimensionalstorage.core.DimensionalStorage;
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
        add("item.dimensionalstorage.antimatter.stable", "§aStable");
        add("item.dimensionalstorage.antimatter.unstable", "§o§cUnstable");
    }
}
