package net.lordkipama.modernminecarts.testmod;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.lordkipama.modernminecarts.ModernMinecartsConfig;
import net.lordkipama.modernminecarts.block.Custom.DirectedPoweredRailBlock;
import net.lordkipama.modernminecarts.block.ModBlocks;
import net.lordkipama.modernminecarts.logic.MinecartTuning;
import net.lordkipama.modernminecarts.testmod.mixin.AbstractMinecartInvoker;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.test.GameTest;
import net.minecraft.test.TestContext;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;

public final class RailSpeedGameTests implements FabricGameTest {
    private static final BlockPos RAIL_POS = new BlockPos(2, 1, 2);
    private static final double EPSILON = 1.0E-6D;

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void normalRailsUseCopperRailSpeed(TestContext context) {
        assertRailSpeed(context, Blocks.RAIL.getDefaultState(), ModernMinecartsConfig.copperSpeed());
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void ascendingRailsUseConfiguredSpeedCap(TestContext context) {
        BlockState ascendingRail = Blocks.RAIL.getDefaultState()
                .with(((AbstractRailBlock) Blocks.RAIL).getShapeProperty(), RailShape.ASCENDING_EAST);
        assertRailSpeed(
                context,
                ascendingRail,
                Math.min(ModernMinecartsConfig.copperSpeed(), ModernMinecartsConfig.maxAscendingSpeed())
        );
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void poweredRailsKeepTheirConfiguredSpeed(TestContext context) {
        BlockState poweredRail = Blocks.POWERED_RAIL.getDefaultState()
                .with(PoweredRailBlock.POWERED, true);
        assertRailSpeed(context, poweredRail, ModernMinecartsConfig.poweredRailSpeed());
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void directedPoweredRailUsesAcceleratedSpeed(TestContext context) {
        BlockPos railPos = context.getAbsolutePos(RAIL_POS);
        context.getWorld().setBlockState(railPos.north(), Blocks.REDSTONE_BLOCK.getDefaultState());
        assertRailSpeed(
                context,
                ModBlocks.DIRECTED_POWERED_RAIL.getDefaultState().with(PoweredRailBlock.POWERED, true),
                ModernMinecartsConfig.copperSpeed()
        );
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void directedPoweredRailActivatesFromRedstone(TestContext context) {
        ServerWorld world = context.getWorld();
        BlockPos railPos = context.getAbsolutePos(RAIL_POS);
        world.setBlockState(railPos.down(), Blocks.STONE.getDefaultState());
        world.setBlockState(railPos.north(), Blocks.REDSTONE_BLOCK.getDefaultState());
        world.setBlockState(railPos, ModBlocks.DIRECTED_POWERED_RAIL.getDefaultState());

        context.waitAndRun(1L, () -> {
            BlockState actualState = world.getBlockState(railPos);
            context.assertTrue(
                    actualState.get(PoweredRailBlock.POWERED),
                    "Directed Powered Rail should activate when powered by redstone"
            );
            context.complete();
        });
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void directedPoweredRailLaunchesStationaryMinecartForward(TestContext context) {
        assertDirectedLaunch(context, false, 0.0D, true, 1.0D);
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void directedPoweredRailLaunchesStationaryMinecartBackward(TestContext context) {
        assertDirectedLaunch(context, true, 0.0D, true, -1.0D);
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void directedPoweredRailReversesOpposingMinecart(TestContext context) {
        assertDirectedLaunch(context, false, -0.2D, true, 1.0D);
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void unpoweredDirectedRailDoesNotLaunchStationaryMinecart(TestContext context) {
        assertDirectedLaunch(context, false, 0.0D, false, 0.0D);
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void directedRailRotationPreservesTravelDirection(TestContext context) {
        BlockState railState = ModBlocks.DIRECTED_POWERED_RAIL.getDefaultState()
                .with(PoweredRailBlock.SHAPE, RailShape.EAST_WEST)
                .with(DirectedPoweredRailBlock.REVERSED, true);
        BlockState rotatedState = ModBlocks.DIRECTED_POWERED_RAIL.rotate(
                railState,
                BlockRotation.CLOCKWISE_90
        );
        context.assertTrue(
                rotatedState.get(DirectedPoweredRailBlock.REVERSED),
                "Rotating a westbound rail clockwise should preserve its transformed travel direction"
        );
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void ascendingDirectedPoweredRailUsesConfiguredSpeedCap(TestContext context) {
        double expectedSpeed = Math.min(
                ModernMinecartsConfig.copperSpeed(),
                ModernMinecartsConfig.maxAscendingSpeed()
        );
        double actualSpeed = MinecartTuning.directedPoweredRailSpeed(true);
        context.assertTrue(
                Math.abs(expectedSpeed - actualSpeed) <= EPSILON,
                "Expected ascending rail speed " + expectedSpeed + " but got " + actualSpeed
        );
        context.complete();
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void copperRailsKeepTheirOxidationSpeed(TestContext context) {
        assertRailSpeed(context, ModBlocks.WEATHERED_COPPER_RAIL.getDefaultState(),
                ModernMinecartsConfig.weatheredCopperSpeed());
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void crossingKeepsVanillaMinecartSpeed(TestContext context) {
        assertRailSpeed(context, ModBlocks.RAIL_CROSSING.getDefaultState(),
                0.4D);
    }

    private static void assertDirectedLaunch(
            TestContext context,
            boolean reversed,
            double initialZVelocity,
            boolean powered,
            double expectedZSign
    ) {
        ServerWorld world = context.getWorld();
        BlockPos railPos = context.getAbsolutePos(RAIL_POS);
        world.setBlockState(railPos.down(), Blocks.STONE.getDefaultState());
        if (powered) {
            world.setBlockState(railPos.north(), Blocks.REDSTONE_BLOCK.getDefaultState());
        }
        BlockState railState = ModBlocks.DIRECTED_POWERED_RAIL.getDefaultState()
                .with(DirectedPoweredRailBlock.REVERSED, reversed)
                .with(PoweredRailBlock.POWERED, powered);
        world.setBlockState(railPos, railState);

        AbstractMinecartEntity cart = EntityType.MINECART.create(world);
        context.assertTrue(cart != null, "Minecart entity should be created");
        cart.setPosition(railPos.getX() + 0.5D, railPos.getY() + 0.1D, railPos.getZ() + 0.5D);
        cart.setVelocity(0.0D, 0.0D, initialZVelocity);
        world.spawnEntity(cart);

        context.waitAndRun(1L, () -> {
            double velocity = cart.getVelocity().z;
            if (expectedZSign == 0.0D) {
                context.assertTrue(Math.abs(velocity) <= EPSILON,
                        "Unpowered Directed Powered Rail should not accelerate a stationary minecart");
            } else {
                context.assertTrue(
                        velocity * expectedZSign > 0.0D,
                        "Minecart should move in the rail's configured direction, got z velocity " + velocity
                );
                if (initialZVelocity == 0.0D) {
                    context.assertTrue(
                            Math.abs(velocity) <= 0.08D,
                            "Stationary minecart should receive one powered-rail acceleration impulse"
                    );
                }
            }
            cart.discard();
            context.complete();
        });
    }

    private static void assertRailSpeed(TestContext context, BlockState railState, double expectedSpeed) {
        ServerWorld world = context.getWorld();
        BlockPos railPos = context.getAbsolutePos(RAIL_POS);
        world.setBlockState(railPos.down(), Blocks.STONE.getDefaultState());
        world.setBlockState(railPos, railState);

        AbstractMinecartEntity cart = EntityType.MINECART.create(world);
        context.assertTrue(cart != null, "Minecart entity should be created");
        cart.setPosition(railPos.getX() + 0.5D, railPos.getY() + 0.1D, railPos.getZ() + 0.5D);
        world.spawnEntity(cart);

        context.waitAndRun(1L, () -> {
            context.assertTrue(cart.isOnRail(), "Minecart should be positioned on the test rail");
            double actualSpeed = ((AbstractMinecartInvoker) cart).modernminecarts$invokeGetMaxSpeed();
            context.assertTrue(
                    Math.abs(expectedSpeed - actualSpeed) <= EPSILON,
                    "Expected rail speed " + expectedSpeed + " but got " + actualSpeed
            );

            cart.discard();
            context.complete();
        });
    }
}
