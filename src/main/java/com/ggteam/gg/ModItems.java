package com.ggteam.gg;

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

    /** Bauxite: raw aluminum ore item, dropped by bauxite ore. Registry id: gg:bauxite */
    public static final DeferredItem<Item> BAUXITE = ITEMS.registerSimpleItem("bauxite");

    /**
     * Aluminum: metal obtained by smelting bauxite in a furnace or blast furnace
     * (see the recipes in data/gg/recipe). Registry id: gg:aluminum
     */
    public static final DeferredItem<Item> ALUMINUM = ITEMS.registerSimpleItem("aluminum");

    /**
     * Hooks the item registry into the mod event bus.
     *
     * @param modEventBus the mod's event bus, provided by NeoForge on startup
     */
    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
