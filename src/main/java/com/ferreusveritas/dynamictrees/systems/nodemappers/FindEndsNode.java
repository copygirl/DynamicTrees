package com.ferreusveritas.dynamictrees.systems.nodemappers;

import com.ferreusveritas.dynamictrees.api.network.INodeInspector;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Finds all branch end points.
 *
 * @author ferreusveritas
 */
public class FindEndsNode implements INodeInspector {

    private final List<BlockPos> endPoints;
    private BlockPos last;

    public FindEndsNode() { //Array is provided for you
        this(new ArrayList<>(32));
    }

    public FindEndsNode(List<BlockPos> ends) { //Or use your own
        this.endPoints = ends;
        last = BlockPos.ZERO;
    }

    @Override
    public boolean run(BlockState blockState, IWorld world, BlockPos pos, Direction fromDir) {
        return true;
    }

    @Override
    public boolean returnRun(BlockState blockState, IWorld world, BlockPos pos, Direction fromDir) {

        //Okay.. so.. a little explanation. If we are only one block away from the last block we returned from then we can't be on an end
        BlockPos dPos = pos.subtract(last);
        if (dPos.getX() * dPos.getX() + dPos.getY() * dPos.getY() + dPos.getZ() * dPos.getZ() != 1) {//This is actually the equation for distance squared. 1 squared is 1. Yay math.
            endPoints.add(pos);
        }

        last = pos;//We can only be in a branch on the return run

        return false;
    }

    public List<BlockPos> getEnds() {
        if (endPoints.isEmpty()) {//It's impossible for nothing to have been found.
            endPoints.add(last);//So just add the last node that was found.
        }
        return endPoints;
    }

}
