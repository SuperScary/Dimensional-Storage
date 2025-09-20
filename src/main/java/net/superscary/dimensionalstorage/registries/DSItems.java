package net.superscary.dimensionalstorage.registries;

import com.google.common.base.Preconditions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.superscary.dimensionalstorage.DSTab;
import net.superscary.dimensionalstorage.core.DimensionalStorage;
import net.superscary.dimensionalstorage.item.AntimatterItem;
import net.superscary.dimensionalstorage.item.base.BaseItem;
import net.superscary.dimensionalstorage.item.ItemDefinition;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class DSItems {

    public static final DeferredRegister.Items REGISTRY = DeferredRegister.createItems(DimensionalStorage.MODID);

    private static final List<ItemDefinition<?>> ITEMS = new ArrayList<>();

    // DRAKIUM
    public static final ItemDefinition<BaseItem> RAW_DRAKIUM_ORE = item("Raw Drakium Ore", BaseItem::new);
    public static final ItemDefinition<BaseItem> DRAKIUM_INGOT = item("Drakium Ingot", BaseItem::new);
    public static final ItemDefinition<BaseItem> DRAKIUM_NUGGET = item("Drakium Nugget", BaseItem::new);

    public static final ItemDefinition<AntimatterItem> ANTIMATTER = item("Antimatter", AntimatterItem::new);

    public static final ItemDefinition<BaseItem> ELECTRIC_CIRCUIT_BASE = item("Electric Circuit Base", BaseItem::new);

    public static List<ItemDefinition<?>> getItems () {
        return Collections.unmodifiableList(ITEMS);
    }

    static <T extends Item> ItemDefinition<T> item (String name, Function<Item.Properties, T> factory) {
        String resourceFriendly = name.toLowerCase().replace(' ', '_');
        return item(name, DimensionalStorage.getResource(resourceFriendly), factory, Keys.MAIN);
    }

    static <T extends Item> ItemDefinition<T> item (final String name, String resourceName, Function<Item.Properties, T> factory) {
        return item(name, DimensionalStorage.getResource(resourceName), factory, Keys.MAIN);
    }

    static <T extends Item> ItemDefinition<T> item (String name, ResourceLocation id, Function<Item.Properties, T> factory, @Nullable ResourceKey<CreativeModeTab> group) {
        Preconditions.checkArgument(id.getNamespace().equals(DimensionalStorage.MODID), "Can only register items in " + DimensionalStorage.MODID);
        var definition = new ItemDefinition<>(name, REGISTRY.registerItem(id.getPath(), factory));

        if (Objects.equals(group, Keys.MAIN)) {
            DSTab.add(definition);
        } else if (group != null) {
            DSTab.addExternal(group, definition);
        }

        ITEMS.add(definition);
        return definition;
    }

}
