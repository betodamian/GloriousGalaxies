package com.ggteam.gg;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

/**
 * Main entry point of the Glorious Galaxies mod.
 * NeoForge creates one instance of this class when the game starts.
 */
@Mod(GloriousGalaxies.MOD_ID)
public class GloriousGalaxies {
    /** Unique mod identifier. Must match "mod_id" in gradle.properties. */
    public static final String MOD_ID = "gg";

    /** Logger used for all of the mod's console/log messages. */
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Called by NeoForge on startup.
     *
     * @param modEventBus  event bus for registering the mod's content (blocks, items, etc.)
     * @param modContainer container holding this mod's info and config registration
     */
    public GloriousGalaxies(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Glorious Galaxies loaded");
    }
}
