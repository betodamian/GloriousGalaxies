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
                        // Every item and block of the mod that should show up in our tab goes here.
                        // Aluminum
                        output.accept(ModBlocks.BAUXITE_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_BAUXITE_ORE.get());
                        output.accept(ModBlocks.RAW_BAUXITE_BLOCK.get());
                        output.accept(ModItems.BAUXITE.get());
                        output.accept(ModItems.ALUMINUM_INGOT.get());
                        output.accept(ModBlocks.ALUMINUM_BLOCK.get());
                        // Titanium
                        output.accept(ModBlocks.TITANIUM_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_TITANIUM_ORE.get());
                        output.accept(ModBlocks.RAW_TITANIUM_BLOCK.get());
                        output.accept(ModItems.RAW_TITANIUM.get());
                        output.accept(ModItems.TITANIUM_INGOT.get());
                        output.accept(ModBlocks.TITANIUM_BLOCK.get());
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
     * Like vanilla: ores and raw ore blocks go in "Natural Blocks", metal blocks in "Building Blocks".
     *
     * @param event fired once per tab while the game builds the tab contents
     */
    private static void addToVanillaTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.BAUXITE.get());
            event.accept(ModItems.ALUMINUM_INGOT.get());
            event.accept(ModItems.RAW_TITANIUM.get());
            event.accept(ModItems.TITANIUM_INGOT.get());
        }
        if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS) {
            event.accept(ModBlocks.BAUXITE_ORE.get());
            event.accept(ModBlocks.DEEPSLATE_BAUXITE_ORE.get());
            event.accept(ModBlocks.RAW_BAUXITE_BLOCK.get());
            event.accept(ModBlocks.TITANIUM_ORE.get());
            event.accept(ModBlocks.DEEPSLATE_TITANIUM_ORE.get());
            event.accept(ModBlocks.RAW_TITANIUM_BLOCK.get());
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.ALUMINUM_BLOCK.get());
            event.accept(ModBlocks.TITANIUM_BLOCK.get());
        }
    }
}
