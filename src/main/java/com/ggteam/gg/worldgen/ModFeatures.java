package com.ggteam.gg.worldgen;

import com.ggteam.gg.GloriousGalaxies;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Custom world generation features. A feature is the code that places something in the world;
 * WHAT it places and HOW OFTEN is set by JSON files in data/gg/worldgen.
 */
public class ModFeatures {
    /** Holds all of this mod's features. Registered to the game in {@link #register}. */
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, GloriousGalaxies.MOD_ID);

    /**
     * Large ore veins like vanilla's iron veins. Used in JSON as "type": "gg:ore_vein".
     * Registry id: gg:ore_vein
     */
    public static final DeferredHolder<Feature<?>, OreVeinFeature> ORE_VEIN =
            FEATURES.register("ore_vein", () -> new OreVeinFeature(OreVeinConfiguration.CODEC));

    /**
     * Hooks the feature registry into the mod event bus.
     *
     * @param modEventBus the mod's event bus, provided by NeoForge on startup
     */
    public static void register(IEventBus modEventBus) {
        FEATURES.register(modEventBus);
    }
}
