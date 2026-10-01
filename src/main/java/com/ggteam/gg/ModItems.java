package com.ggteam.gg;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry of every item added by Glorious Galaxies.
 * To add a new item: declare it below, add a model json, a texture,
 * and a lang entry, then list it in the creative tabs it belongs to.
 * (Block items, like the ore blocks' items, are created in {@link ModBlocks}.)
 */
public class ModItems {
    /** Holds all of this mod's items. Registered to the game in {@link #register}. */
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(GloriousGalaxies.MOD_ID);

    // ----- Aluminum -----

    /** Bauxite: raw aluminum ore item, dropped by bauxite ore. Registry id: gg:bauxite */
    public static final DeferredItem<Item> BAUXITE = ITEMS.registerSimpleItem("bauxite");

    /**
     * Aluminum Ingot: metal obtained by smelting bauxite in a furnace or blast furnace
     * (see the recipes in data/gg/recipe). Registry id: gg:aluminum_ingot (was gg:aluminum)
     */
    public static final DeferredItem<Item> ALUMINUM_INGOT = ITEMS.registerSimpleItem("aluminum_ingot");

    // ----- Titanium -----

    /** Raw Titanium: dropped by titanium ore, smelts into a titanium ingot. Registry id: gg:raw_titanium */
    public static final DeferredItem<Item> RAW_TITANIUM = ITEMS.registerSimpleItem("raw_titanium");

    /** Titanium Ingot: metal obtained by smelting raw titanium. Registry id: gg:titanium_ingot */
    public static final DeferredItem<Item> TITANIUM_INGOT = ITEMS.registerSimpleItem("titanium_ingot");

    /**
     * Hooks the item registry into the mod event bus.
     *
     * @param modEventBus the mod's event bus, provided by NeoForge on startup
     */
    public static void register(IEventBus modEventBus) {
        // Aluminum was renamed to aluminum_ingot: worlds that still contain the old item get the new one.
        ITEMS.addAlias(modId("aluminum"), modId("aluminum_ingot"));
        ITEMS.register(modEventBus);
    }

    /** Builds an id in this mod's namespace, e.g. "aluminum" -> gg:aluminum */
    private static ResourceLocation modId(String path) {
        return ResourceLocation.fromNamespaceAndPath(GloriousGalaxies.MOD_ID, path);
    }
}
