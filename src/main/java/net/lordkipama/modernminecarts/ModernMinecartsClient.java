package net.lordkipama.modernminecarts;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.lordkipama.modernminecarts.client.FurnaceMinecartScreen;
import net.lordkipama.modernminecarts.block.ModBlocks;
import net.lordkipama.modernminecarts.screen.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ModernMinecartsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HandledScreens.register(ModScreenHandlers.FURNACE_MINECART, FurnaceMinecartScreen::new);
        ClientPlayNetworking.registerGlobalReceiver(net.lordkipama.modernminecarts.SyncChainedMinecartPacket.ID, net.lordkipama.modernminecarts.SyncChainedMinecartPacket::handle);

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

        ResourceManagerHelper.registerBuiltinResourcePack(
                ModernMinecarts.id("modernminecarts3d"),
                FabricLoader.getInstance().getModContainer(ModernMinecarts.MOD_ID).orElseThrow(),
                Text.literal("Modern Minecarts 3D Copper Rails"),
                ResourcePackActivationType.NORMAL
        );
    }
}
