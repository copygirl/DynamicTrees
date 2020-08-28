package com.ferreusveritas.dynamictrees.api;

import com.ferreusveritas.dynamictrees.blocks.BlockRooty;
import net.minecraft.block.Block;

import java.util.*;

public class RootyBlockHelper {

    private static Map<Block, BlockRooty> rootyBlocksMap = new HashMap<>();
    private static LinkedList<Block> blackList = new LinkedList<>();

    /** THIS MUST BE CALLED BEFORE addToRootyBlocksMap
     *
     * @param blockToExcempt this is the block that does not need a custom rooty dirt
     * @param defaultTo blockToExcempt will get defaultTo's rooty dirt instead.
     * @return
     */
    public static boolean excemptBlock(Block blockToExcempt, Block defaultTo){
        if (rootyBlocksMap.containsKey(blockToExcempt))
            return false; //block was already registered
        if (!rootyBlocksMap.containsKey(defaultTo)){
            addToRootyBlocksMap(defaultTo);
        }
        rootyBlocksMap.put(blockToExcempt, rootyBlocksMap.get(defaultTo));
        return true;
    }

    /**
     * @param primitiveSoil normal block that trees can be planted on
     * @param rootyBlock rooty dirt that should represent the primitive dirt
     * @return
     */
    public static boolean addToRootyBlocksMap (Block primitiveSoil, BlockRooty rootyBlock){
        if (rootyBlocksMap.containsKey(primitiveSoil))
            return false;
        rootyBlocksMap.put(primitiveSoil, rootyBlock);
        return true;
    }

    public static boolean addToRootyBlocksMap (Block primitiveSoil){
        return addToRootyBlocksMap(primitiveSoil, new BlockRooty(primitiveSoil));
    }

    public static Map<Block, BlockRooty> getRootyBlocksMap() {
        return rootyBlocksMap;
    }

    public static Collection<BlockRooty> getListForRegistry(){
        LinkedList<BlockRooty> listNotRepeated = new LinkedList<>();
        for (BlockRooty rooty : rootyBlocksMap.values()){
            if (!listNotRepeated.contains(rooty)){
                listNotRepeated.add(rooty);
            }
        }
        return listNotRepeated;
    }

}
