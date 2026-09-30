package net.lordkipama.modernminecarts.testmod;

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest;
import net.lordkipama.modernminecarts.ModernMinecartsConfig;
import net.lordkipama.modernminecarts.block.ModBlocks;
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
    public void copperRailsKeepTheirOxidationSpeed(TestContext context) {
        assertRailSpeed(context, ModBlocks.WEATHERED_COPPER_RAIL.getDefaultState(),
                ModernMinecartsConfig.weatheredCopperSpeed());
    }

    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    public void crossingKeepsVanillaMinecartSpeed(TestContext context) {
        assertRailSpeed(context, ModBlocks.RAIL_CROSSING.getDefaultState(),
                0.4D);
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
