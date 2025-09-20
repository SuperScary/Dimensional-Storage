package net.superscary.dimensionalstorage.datagen.models;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.superscary.dimensionalstorage.core.DimensionalStorage;
import net.superscary.dimensionalstorage.item.ItemDefinition;
import net.superscary.dimensionalstorage.registries.DSItems;
import net.superscary.dimensionalstorage.util.ICustomModel;
import net.superscary.dimensionalstorage.util.IDataProvider;

public class ModItemModelProvider extends ItemModelProvider implements IDataProvider {

    public ModItemModelProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, DimensionalStorage.MODID, existingFileHelper);
    }

    private static ResourceLocation makeId (String id) {
        return id.contains(":") ? ResourceLocation.parse(id) : DimensionalStorage.getResource(id);
    }

    @Override
    protected void registerModels () {
        for (var item : DSItems.getItems()) {
            if (!(item instanceof ICustomModel)) {
                handheldItem(item);
            }
        }
    }

    public ItemModelBuilder handheldItem (ItemDefinition<?> item) {
        return handheldItem(item.asItem());
    }

}
