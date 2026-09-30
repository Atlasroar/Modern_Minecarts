package net.lordkipama.modernminecarts.testmod.mixin;

import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractMinecartEntity.class)
public interface AbstractMinecartInvoker {
    @Invoker("getMaxSpeed")
    double modernminecarts$invokeGetMaxSpeed();
}
