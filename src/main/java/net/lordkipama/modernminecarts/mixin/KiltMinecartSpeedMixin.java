package net.lordkipama.modernminecarts.mixin;

import net.lordkipama.modernminecarts.block.ModBlocks;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractMinecartEntity.class, priority = 900)
public abstract class KiltMinecartSpeedMixin {
    @Inject(
            method = "getCurrentCartSpeedCapOnRail",
            at = @At("RETURN"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private void modernminecarts$useConfiguredCartSpeedCap(CallbackInfoReturnable<Float> cir) {
        AbstractMinecartEntity cart = (AbstractMinecartEntity) (Object) this;
        if (modernminecarts$getRailState(cart) != null) {
            cir.setReturnValue((float) ((MinecartInvoker) cart).invokeGetMaxSpeed());
        }
    }

    @Inject(
            method = "getMaxSpeedWithRail",
            at = @At("RETURN"),
            cancellable = true,
            remap = false,
            require = 0
    )
    private void modernminecarts$useConfiguredRailSpeedWithKilt(CallbackInfoReturnable<Double> cir) {
        AbstractMinecartEntity cart = (AbstractMinecartEntity) (Object) this;
        BlockState railState = modernminecarts$getRailState(cart);
        if (railState != null && !railState.isOf(ModBlocks.RAIL_CROSSING)) {
            cir.setReturnValue(((MinecartInvoker) cart).invokeGetMaxSpeed());
        }
    }

    @Unique
    private static @Nullable BlockState modernminecarts$getRailState(AbstractMinecartEntity cart) {
        BlockPos railPos = BlockPos.ofFloored(cart.getX(), cart.getY(), cart.getZ());
        BlockState railState = cart.getWorld().getBlockState(railPos);
        if (!AbstractRailBlock.isRail(railState)) {
            railPos = railPos.down();
            railState = cart.getWorld().getBlockState(railPos);
        }
        return AbstractRailBlock.isRail(railState) ? railState : null;
    }
}
