package net.lordkipama.modernminecarts.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;

public final class MinecartRiderInputClient {
    private static boolean wasRidingMinecart;

    private MinecartRiderInputClient() {
    }

    public static void tick(MinecraftClient client) {
        if (client.player == null || client.getNetworkHandler() == null) {
            wasRidingMinecart = false;
            return;
        }

        if (client.player.getVehicle() instanceof AbstractMinecartEntity) {
            float sideways = client.player.input.movementSideways;
            float forward = client.player.input.movementForward;
            MinecartRiderInput.setClientInput(client.player.getUuid(), sideways, forward, client.player.getYaw());
            if (ClientPlayNetworking.canSend(MinecartRiderInput.ID)) {
                ClientPlayNetworking.send(
                        MinecartRiderInput.ID,
                        MinecartRiderInput.createPacket(sideways, forward)
                );
            }
            wasRidingMinecart = true;
        } else if (wasRidingMinecart) {
            MinecartRiderInput.setClientInput(client.player.getUuid(), 0.0F, 0.0F, client.player.getYaw());
            if (ClientPlayNetworking.canSend(MinecartRiderInput.ID)) {
                ClientPlayNetworking.send(
                        MinecartRiderInput.ID,
                        MinecartRiderInput.createPacket(0.0F, 0.0F)
                );
            }
            wasRidingMinecart = false;
        }
    }
}
