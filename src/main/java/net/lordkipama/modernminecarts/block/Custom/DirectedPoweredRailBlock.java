package net.lordkipama.modernminecarts.block.Custom;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DirectedPoweredRailBlock extends PoweredRailBlock {
    public static final BooleanProperty REVERSED = BooleanProperty.of("reversed");

    public DirectedPoweredRailBlock(AbstractBlock.Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(REVERSED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(REVERSED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState state = super.getPlacementState(context);
        if (state == null) {
            return null;
        }
        Direction placementDirection = context.getHorizontalPlayerFacing();
        RailShape shape = state.get(SHAPE);
        Direction positiveDirection = getPositiveDirection(shape);
        if (placementDirection.getAxis() != positiveDirection.getAxis()) {
            shape = placementDirection.getAxis() == Direction.Axis.X
                    ? RailShape.EAST_WEST
                    : RailShape.NORTH_SOUTH;
            state = state.with(SHAPE, shape);
        }
        return state.with(REVERSED, placementDirection != getPositiveDirection(shape));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        Direction travelDirection = getTravelDirection(state);
        BlockState rotated = super.rotate(state, rotation);
        Direction rotatedTravelDirection = rotation.rotate(travelDirection);
        return rotated.with(REVERSED, rotatedTravelDirection != getPositiveDirection(rotated.get(SHAPE)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        Direction travelDirection = getTravelDirection(state);
        BlockState mirrored = super.mirror(state, mirror);
        Direction mirroredTravelDirection = mirror.apply(travelDirection);
        return mirrored.with(REVERSED, mirroredTravelDirection != getPositiveDirection(mirrored.get(SHAPE)));
    }

    @Override
    public ActionResult onUse(
            BlockState state,
            World world,
            BlockPos pos,
            PlayerEntity player,
            Hand hand,
            BlockHitResult hit
    ) {
        if (!player.getStackInHand(hand).isEmpty()) {
            return ActionResult.PASS;
        }
        if (!world.isClient) {
            world.setBlockState(pos, state.cycle(REVERSED), Block.NOTIFY_ALL);
        }
        return ActionResult.SUCCESS;
    }

    public static Vec3d getTravelVector(BlockState state) {
        Direction positiveDirection = getPositiveDirection(state.get(SHAPE));
        return state.get(REVERSED)
                ? new Vec3d(-positiveDirection.getOffsetX(), 0.0D, -positiveDirection.getOffsetZ())
                : new Vec3d(positiveDirection.getOffsetX(), 0.0D, positiveDirection.getOffsetZ());
    }

    private static Direction getTravelDirection(BlockState state) {
        Direction positiveDirection = getPositiveDirection(state.get(SHAPE));
        return state.get(REVERSED) ? positiveDirection.getOpposite() : positiveDirection;
    }

    private static Direction getPositiveDirection(RailShape shape) {
        return switch (shape) {
            case NORTH_SOUTH, ASCENDING_NORTH, ASCENDING_SOUTH -> Direction.SOUTH;
            case EAST_WEST, ASCENDING_EAST, ASCENDING_WEST -> Direction.EAST;
            default -> throw new IllegalArgumentException("Unsupported directed rail shape: " + shape);
        };
    }
}
