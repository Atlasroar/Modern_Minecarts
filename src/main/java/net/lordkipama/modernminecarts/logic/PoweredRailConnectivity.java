package net.lordkipama.modernminecarts.logic;

import net.lordkipama.modernminecarts.mixin.PoweredRailBlockInvoker;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class PoweredRailConnectivity {
    private PoweredRailConnectivity() {
    }

    public static boolean isPoweredByOtherRails(
            World world,
            BlockPos pos,
            boolean direction,
            int distance,
            RailShape shape
    ) {
        if (distance >= 8) {
            return false;
        }

        BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof PoweredRailBlock railBlock)) {
            return false;
        }

        RailShape railShape = state.get(PoweredRailBlock.SHAPE);
        if (shape == RailShape.EAST_WEST
                && (railShape == RailShape.NORTH_SOUTH
                || railShape == RailShape.ASCENDING_NORTH
                || railShape == RailShape.ASCENDING_SOUTH)) {
            return false;
        }
        if (shape == RailShape.NORTH_SOUTH
                && (railShape == RailShape.EAST_WEST
                || railShape == RailShape.ASCENDING_EAST
                || railShape == RailShape.ASCENDING_WEST)) {
            return false;
        }

        if (!state.get(PoweredRailBlock.POWERED)) {
            return false;
        }
        if (world.isReceivingRedstonePower(pos)) {
            return true;
        }

        return ((PoweredRailBlockInvoker) railBlock).modernminecarts$invokeIsPoweredByOtherRails(
                world,
                pos,
                state,
                direction,
                distance + 1
        );
    }
}
