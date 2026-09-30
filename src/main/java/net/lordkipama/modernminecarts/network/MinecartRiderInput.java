package net.lordkipama.modernminecarts.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.lordkipama.modernminecarts.ModernMinecarts;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class MinecartRiderInput {
    public static final Identifier ID = ModernMinecarts.id("minecart_rider_input");
    private static final long MAX_INPUT_AGE_TICKS = 5;
    private static final Map<UUID, ServerInput> SERVER_INPUTS = new HashMap<>();

    private static UUID clientPlayerId;
    private static Vec3d clientInput = Vec3d.ZERO;

    private MinecartRiderInput() {
    }

    public static void registerServerReceiver() {
        ServerPlayNetworking.registerGlobalReceiver(ID, (server, player, handler, buf, responseSender) -> {
            float sideways = clampInput(buf.readFloat());
            float forward = clampInput(buf.readFloat());
            server.execute(() -> {
                if (!(player.getVehicle() instanceof AbstractMinecartEntity)) {
                    SERVER_INPUTS.remove(player.getUuid());
                    return;
                }
                SERVER_INPUTS.put(player.getUuid(), new ServerInput(
                        sideways,
                        forward,
                        (ServerWorld) player.getWorld(),
                        player.getWorld().getTime()
                ));
            });
        });

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) ->
                SERVER_INPUTS.remove(handler.player.getUuid())
        );
    }

    public static void setClientInput(UUID playerId, float sideways, float forward, float yawDegrees) {
        clientPlayerId = playerId;
        clientInput = toWorldVector(sideways, forward, yawDegrees);
    }

    public static Vec3d getMoveIntent(PlayerEntity player) {
        if (player.getWorld().isClient()) {
            return player.getUuid().equals(clientPlayerId) ? clientInput : player.getVelocity();
        }

        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return player.getVelocity();
        }

        ServerInput input = SERVER_INPUTS.get(player.getUuid());
        if (input == null || input.world() != player.getWorld()) {
            return player.getVelocity();
        }

        long age = ((ServerWorld) player.getWorld()).getTime() - input.receivedAt();
        if (age < 0 || age > MAX_INPUT_AGE_TICKS) {
            SERVER_INPUTS.remove(player.getUuid());
            return player.getVelocity();
        }

        return toWorldVector(input.sideways(), input.forward(), serverPlayer.getYaw());
    }

    public static Vec3d toWorldVector(float sideways, float forward, float yawDegrees) {
        double lengthSquared = sideways * sideways + forward * forward;
        if (lengthSquared <= 0.0) {
            return Vec3d.ZERO;
        }

        double scale = lengthSquared > 1.0 ? 1.0 / Math.sqrt(lengthSquared) : 1.0;
        double strafe = sideways * scale;
        double advance = forward * scale;
        double yaw = Math.toRadians(yawDegrees);
        double sin = Math.sin(yaw);
        double cos = Math.cos(yaw);
        return new Vec3d(strafe * cos - advance * sin, 0.0, advance * cos + strafe * sin);
    }

    public static PacketByteBuf createPacket(float sideways, float forward) {
        PacketByteBuf buf = net.fabricmc.fabric.api.networking.v1.PacketByteBufs.create();
        buf.writeFloat(clampInput(sideways));
        buf.writeFloat(clampInput(forward));
        return buf;
    }

    private static float clampInput(float value) {
        if (!Float.isFinite(value)) {
            return 0.0F;
        }
        return Math.max(-1.0F, Math.min(1.0F, value));
    }

    private record ServerInput(float sideways, float forward, ServerWorld world, long receivedAt) {
    }
}
