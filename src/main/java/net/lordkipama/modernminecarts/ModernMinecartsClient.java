package net.lordkipama.modernminecarts;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.lordkipama.modernminecarts.block.ModBlocks;
import net.lordkipama.modernminecarts.client.FurnaceMinecartScreen;
import net.lordkipama.modernminecarts.network.MinecartRiderInputClient;
import net.lordkipama.modernminecarts.screen.ModScreenHandlers;
import net.lordkipama.modernminecarts.network.MinecartRiderInput;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;

@Environment(EnvType.CLIENT)
public class ModernMinecartsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.FURNACE_MINECART, FurnaceMinecartScreen::new);
        ClientPlayNetworking.registerGlobalReceiver(net.lordkipama.modernminecarts.SyncChainedMinecartPacket.ID, net.lordkipama.modernminecarts.SyncChainedMinecartPacket::handle);
        ClientTickEvents.END_CLIENT_TICK.register(MinecartRiderInputClient::tick);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.COPPER_RAIL);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.EXPOSED_COPPER_RAIL);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.WEATHERED_COPPER_RAIL);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.OXIDIZED_COPPER_RAIL);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.WAXED_COPPER_RAIL);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.WAXED_EXPOSED_COPPER_RAIL);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.WAXED_WEATHERED_COPPER_RAIL);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.WAXED_OXIDIZED_COPPER_RAIL);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.RAIL_CROSSING);

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.RAIL_JUMP);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.POWERED_DETECTOR_RAIL);
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), ModBlocks.DIRECTED_POWERED_RAIL);
    }
}
