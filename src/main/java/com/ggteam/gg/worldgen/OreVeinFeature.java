package com.ggteam.gg.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.HashSet;
import java.util.Set;

/**
 * A large, rare ore vein like vanilla's iron and copper veins: a long, winding tube of filler
 * rock (tuff for iron) with ore scattered through it and the odd raw ore block.
 *
 * Vanilla builds its veins into the terrain noise and only supports iron and copper, so this
 * feature recreates the look with the same proportions (see {@link OreVeinConfiguration}):
 * 70% of the vein's blocks are replaced, 10-30% of those become ore, 2% of the ore is a raw ore
 * block, and the rest is filler.
 */
public class OreVeinFeature extends Feature<OreVeinConfiguration> {
    /**
     * How far (in blocks) the vein's path may wander sideways from where it starts. Features may
     * only change blocks in their own chunk and the chunks right next to it, so this keeps the
     * whole vein (path + thickness) inside that area.
     */
    private static final int MAX_SIDEWAYS_REACH = 11;
    /** How far the vein's path may wander up or down from where it starts. */
    private static final int MAX_VERTICAL_REACH = 14;
    /** How sharply the path can turn per block (radians). */
    private static final double MAX_TURN_PER_STEP = 0.5;
    /** How steeply the path can climb or dive (radians). */
    private static final double MAX_SLOPE = 0.8;

    public OreVeinFeature(Codec<OreVeinConfiguration> codec) {
        super(codec);
    }

    /**
     * Places one vein. Called by the game for every placement of the feature (see the placed
     * feature JSON for how rare and how deep that is).
     *
     * @return true if at least one block was changed
     */
    @Override
    public boolean place(FeaturePlaceContext<OreVeinConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        OreVeinConfiguration config = context.config();
        BlockPos start = context.origin();

        // Walk a winding path, starting in a random direction.
        double x = start.getX() + 0.5;
        double y = start.getY() + 0.5;
        double z = start.getZ() + 0.5;
        double heading = random.nextDouble() * Math.PI * 2; // direction on the horizontal plane
        double slope = (random.nextDouble() - 0.5) * MAX_SLOPE; // up/down angle

        Set<BlockPos> alreadyDecided = new HashSet<>();
        boolean changedAnything = false;
        for (int step = 0; step < config.length(); step++) {
            heading += (random.nextDouble() - 0.5) * 2 * MAX_TURN_PER_STEP;
            slope = Mth.clamp(slope + (random.nextDouble() - 0.5) * 0.4, -MAX_SLOPE, MAX_SLOPE);
            x += Math.cos(heading) * Math.cos(slope);
            y += Math.sin(slope);
            z += Math.sin(heading) * Math.cos(slope);

            // Turn back when reaching the edge of the allowed area.
            if (Math.abs(x - start.getX()) > MAX_SIDEWAYS_REACH || Math.abs(z - start.getZ()) > MAX_SIDEWAYS_REACH) {
                heading += Math.PI;
                x = Mth.clamp(x, start.getX() - MAX_SIDEWAYS_REACH, start.getX() + MAX_SIDEWAYS_REACH);
                z = Mth.clamp(z, start.getZ() - MAX_SIDEWAYS_REACH, start.getZ() + MAX_SIDEWAYS_REACH);
            }
            if (Math.abs(y - start.getY()) > MAX_VERTICAL_REACH) {
                slope = -slope;
                y = Mth.clamp(y, start.getY() - MAX_VERTICAL_REACH, start.getY() + MAX_VERTICAL_REACH);
            }

            // Thickest in the middle of the vein, thinning out toward both ends.
            double progress = config.length() > 1 ? step / (double) (config.length() - 1) : 0.5;
            double radius = Mth.lerp(Math.sin(progress * Math.PI), config.minRadius(), config.maxRadius());
            changedAnything |= fillBall(level, random, config, x, y, z, radius, alreadyDecided);
        }
        return changedAnything;
    }

    /**
     * Turns the stone inside one ball of the vein into vein blocks.
     *
     * @param alreadyDecided blocks earlier balls already handled (balls overlap along the path)
     * @return true if at least one block was changed
     */
    private boolean fillBall(WorldGenLevel level, RandomSource random, OreVeinConfiguration config,
                             double centerX, double centerY, double centerZ, double radius, Set<BlockPos> alreadyDecided) {
        boolean changedAnything = false;
        int reach = Mth.ceil(radius);
        for (int dx = -reach; dx <= reach; dx++) {
            for (int dy = -reach; dy <= reach; dy++) {
                for (int dz = -reach; dz <= reach; dz++) {
                    if (dx * dx + dy * dy + dz * dz > radius * radius) {
                        continue;
                    }
                    BlockPos position = BlockPos.containing(centerX + dx, centerY + dy, centerZ + dz);
                    if (!alreadyDecided.add(position)) {
                        continue;
                    }
                    BlockState newState = chooseVeinBlock(level.getBlockState(position), random, config);
                    if (newState != null) {
                        level.setBlock(position, newState, Block.UPDATE_CLIENTS);
                        changedAnything = true;
                    }
                }
            }
        }
        return changedAnything;
    }

    /**
     * Decides what one block inside the vein becomes.
     *
     * @param current the block that's there now
     * @return the new block, or null to leave it alone (air, water, other ores and part of the stone are kept)
     */
    private BlockState chooseVeinBlock(BlockState current, RandomSource random, OreVeinConfiguration config) {
        boolean isDeepslate = current.is(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        boolean isStone = current.is(BlockTags.STONE_ORE_REPLACEABLES);
        if (!isDeepslate && !isStone) {
            return null; // never fill caves or water, or overwrite other ores
        }
        if (random.nextFloat() > config.solidness()) {
            return null; // like vanilla, part of the vein stays plain stone
        }
        if (random.nextFloat() < config.oreChance()) {
            if (random.nextFloat() < config.rawOreBlockChance()) {
                return config.rawOreBlock();
            }
            return isDeepslate ? config.deepslateOre() : config.stoneOre();
        }
        return config.filler();
    }
}
