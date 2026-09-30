package net.lordkipama.modernminecarts.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PoweredRailBlock.class)
public interface PoweredRailBlockInvoker {
    @Invoker("isPoweredByOtherRails")
    boolean modernminecarts$invokeIsPoweredByOtherRails(
            World world,
            BlockPos pos,
            BlockState state,
            boolean direction,
            int distance
    );
}
