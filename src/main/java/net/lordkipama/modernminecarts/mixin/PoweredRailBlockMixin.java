package net.lordkipama.modernminecarts.mixin;

import net.lordkipama.modernminecarts.logic.PoweredRailConnectivity;
import net.minecraft.block.PoweredRailBlock;
import net.minecraft.block.enums.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PoweredRailBlock.class)
public abstract class PoweredRailBlockMixin {
    @Inject(
            method = "isPoweredByOtherRails(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ZILnet/minecraft/block/enums/RailShape;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void modernminecarts$sharePowerBetweenRailTypes(
            World world,
            BlockPos pos,
            boolean direction,
            int distance,
            RailShape shape,
            CallbackInfoReturnable<Boolean> cir
    ) {
        cir.setReturnValue(PoweredRailConnectivity.isPoweredByOtherRails(
                world,
                pos,
                direction,
                distance,
                shape
        ));
    }
}
