package com.ferreusveritas.dynamictrees.systems.genfeatures;

import com.ferreusveritas.dynamictrees.api.IPostGenFeature;
import com.ferreusveritas.dynamictrees.data.DTBlockTags;
import com.ferreusveritas.dynamictrees.systems.genfeatures.config.ConfiguredGenFeature;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.SafeChunkBounds;
import com.ferreusveritas.dynamictrees.util.SimpleVoxmap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class UndergrowthGenFeature extends GenFeature implements IPostGenFeature {

    public UndergrowthGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
    }

    @Override
    public boolean postGeneration(ConfiguredGenFeature<?> configuredGenFeature, IWorld world, BlockPos rootPos, Species species, Biome biome, int radius, List<BlockPos> endPoints, SafeChunkBounds safeBounds, BlockState initialDirtState, Float seasonValue, Float seasonFruitProductionFactor) {
        final boolean worldGen = safeBounds != SafeChunkBounds.ANY;

        if (!worldGen || radius <= 2) {
            return false;
        }

        final Vector3d vTree = new Vector3d(rootPos.getX(), rootPos.getY(), rootPos.getZ()).add(0.5, 0.5, 0.5);

        for (int i = 0; i < 2; i++) {

            int rad = MathHelper.clamp(world.getRandom().nextInt(radius - 2) + 2, 2, radius - 1);
            Vector3d v = vTree.add(new Vector3d(1, 0, 0).scale(rad).yRot((float) (world.getRandom().nextFloat() * Math.PI * 2)));
            BlockPos vPos = new BlockPos(v);

            if (!safeBounds.inBounds(vPos, true)) {
                continue;
            }

            final BlockPos groundPos = CoordUtils.findWorldSurface(world, vPos, true);
            final BlockState soilBlockState = world.getBlockState(groundPos);

            BlockPos pos = groundPos.above();
            if (species.isAcceptableSoil(world, groundPos, soilBlockState)) {
                final int type = world.getRandom().nextInt(2);
                world.setBlock(pos, (type == 0 ? Blocks.OAK_LOG : Blocks.JUNGLE_LOG).defaultBlockState(), 2);
                pos = pos.above(world.getRandom().nextInt(3));

                final BlockState leavesState = (type == 0 ? Blocks.OAK_LEAVES : Blocks.JUNGLE_LEAVES).defaultBlockState().setValue(LeavesBlock.PERSISTENT, true);

                final SimpleVoxmap leafMap = species.getLeavesProperties().getCellKit().getLeafCluster();
                final BlockPos.Mutable leafPos = new BlockPos.Mutable();
                for (BlockPos.Mutable dPos : leafMap.getAllNonZero()) {
                    leafPos.set(pos.getX() + dPos.getX(), pos.getY() + dPos.getY(), pos.getZ() + dPos.getZ());

                    if (safeBounds.inBounds(leafPos, true) && (CoordUtils.coordHashCode(leafPos, 0) % 5) != 0 &&
                            (world.getBlockState(leafPos).canBeReplacedByLeaves(world, leafPos) ||
                                    world.getBlockState(leafPos).is(DTBlockTags.FOLIAGE))) {
                        world.setBlock(leafPos, leavesState, 2);
                    }
                }
            }
        }

        return true;
    }

}
