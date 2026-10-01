package com.ggteam.gg.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

/**
 * Settings for one kind of large ore vein (see {@link OreVeinFeature}).
 * Filled in from a configured feature JSON, e.g. data/gg/worldgen/configured_feature/bauxite_vein.json.
 *
 * @param stoneOre          ore placed where the vein replaces stone (or granite, diorite, andesite)
 * @param deepslateOre      ore placed where the vein replaces deepslate or tuff
 * @param rawOreBlock       rare block of raw ore mixed into the vein (like raw iron blocks in iron veins)
 * @param filler            rock the rest of the vein is made of (vanilla iron veins use tuff)
 * @param length            how many blocks long the vein's winding path is
 * @param minRadius         vein thickness at its two ends
 * @param maxRadius         vein thickness in its middle
 * @param solidness         share of the vein's blocks that are replaced (vanilla: 0.7, the rest stays plain stone)
 * @param oreChance         share of the replaced blocks that become ore instead of filler (vanilla: 0.1 to 0.3)
 * @param rawOreBlockChance share of the ore that becomes a raw ore block instead (vanilla: 0.02)
 */
public record OreVeinConfiguration(
        BlockState stoneOre,
        BlockState deepslateOre,
        BlockState rawOreBlock,
        BlockState filler,
        int length,
        float minRadius,
        float maxRadius,
        float solidness,
        float oreChance,
        float rawOreBlockChance
) implements FeatureConfiguration {

    /** Reads and writes these settings as JSON (the names below are the JSON field names). */
    public static final Codec<OreVeinConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("stone_ore").forGetter(OreVeinConfiguration::stoneOre),
            BlockState.CODEC.fieldOf("deepslate_ore").forGetter(OreVeinConfiguration::deepslateOre),
            BlockState.CODEC.fieldOf("raw_ore_block").forGetter(OreVeinConfiguration::rawOreBlock),
            BlockState.CODEC.fieldOf("filler").forGetter(OreVeinConfiguration::filler),
            Codec.intRange(1, 96).fieldOf("length").forGetter(OreVeinConfiguration::length),
            Codec.floatRange(0.5F, 4.0F).fieldOf("min_radius").forGetter(OreVeinConfiguration::minRadius),
            Codec.floatRange(0.5F, 4.0F).fieldOf("max_radius").forGetter(OreVeinConfiguration::maxRadius),
            Codec.floatRange(0.0F, 1.0F).fieldOf("solidness").forGetter(OreVeinConfiguration::solidness),
            Codec.floatRange(0.0F, 1.0F).fieldOf("ore_chance").forGetter(OreVeinConfiguration::oreChance),
            Codec.floatRange(0.0F, 1.0F).fieldOf("raw_ore_block_chance").forGetter(OreVeinConfiguration::rawOreBlockChance)
    ).apply(instance, OreVeinConfiguration::new));
}
