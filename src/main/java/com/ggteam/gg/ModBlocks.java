package com.ggteam.gg;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry of every block added by Glorious Galaxies.
 * To add a new block: declare it below (it gets a matching block item automatically), then add
 * its blockstate, block model, item model, texture, lang entry, loot table and tool tags.
 */
public class ModBlocks {
    /** Holds all of this mod's blocks. Registered to the game in {@link #register}. */
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(GloriousGalaxies.MOD_ID);

    /**
     * Bauxite Ore: same properties as vanilla iron ore (hardness 3, needs a stone pickaxe or better,
     * drops no experience). Drops bauxite; see data/gg/loot_table/blocks/bauxite_ore.json.
     * Generates in jungles and warm/lukewarm oceans; see data/gg/worldgen.
     * Registry id: gg:bauxite_ore
     */
    public static final DeferredBlock<DropExperienceBlock> BAUXITE_ORE = registerBlockWithItem("bauxite_ore",
            () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 3.0F)));

    /**
     * Deepslate Bauxite Ore: same properties as vanilla deepslate iron ore (harder than the
     * stone version, deepslate sounds). Replaces deepslate underground. Registry id: gg:deepslate_bauxite_ore
     */
    public static final DeferredBlock<DropExperienceBlock> DEEPSLATE_BAUXITE_ORE = registerBlockWithItem("deepslate_bauxite_ore",
            () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(4.5F, 3.0F)
                    .sound(SoundType.DEEPSLATE)));

    /**
     * Registers a block together with the item that places it (so it can be held and put in the inventory).
     *
     * @param name          registry name, e.g. "bauxite_ore"
     * @param blockSupplier creates the block when the game registers it
     * @return the registered block
     */
    private static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name, Supplier<T> blockSupplier) {
        DeferredBlock<T> block = BLOCKS.register(name, blockSupplier);
        ModItems.ITEMS.registerSimpleBlockItem(name, block);
        return block;
    }

    /**
     * Hooks the block registry into the mod event bus.
     *
     * @param modEventBus the mod's event bus, provided by NeoForge on startup
     */
    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
