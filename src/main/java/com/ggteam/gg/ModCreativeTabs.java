package com.ggteam.gg;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Creative inventory tabs: the mod's own "Glorious Galaxies" tab,
 * plus adding our items to existing vanilla tabs.
 */
public class ModCreativeTabs {
    /** Holds all of this mod's creative tabs. */
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GloriousGalaxies.MOD_ID);

    /** The mod's own creative tab. Its name comes from lang key "itemGroup.gg.glorious_galaxies". */
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GLORIOUS_GALAXIES_TAB =
            CREATIVE_TABS.register("glorious_galaxies", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.gg.glorious_galaxies"))
                    .icon(() -> ModItems.BAUXITE.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        // Every item of the mod that should show up in our tab goes here.
                        output.accept(ModItems.BAUXITE.get());
                        output.accept(ModItems.ALUMINUM.get());
                    })
                    .build());

    /**
     * Hooks the tab registry and the vanilla-tab listener into the mod event bus.
     *
     * @param modEventBus the mod's event bus, provided by NeoForge on startup
     */
    public static void register(IEventBus modEventBus) {
        CREATIVE_TABS.register(modEventBus);
        modEventBus.addListener(ModCreativeTabs::addToVanillaTabs);
    }

    /**
     * Adds our items to vanilla creative tabs.
     * Vanilla has no "Minerals" tab, so mineral items go in "Ingredients" (coal, iron ingot, etc.).
     *
     * @param event fired once per tab while the game builds the tab contents
     */
    private static void addToVanillaTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.BAUXITE.get());
            event.accept(ModItems.ALUMINUM.get());
        }
    }
}
