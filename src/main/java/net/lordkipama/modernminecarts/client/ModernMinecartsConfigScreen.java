package net.lordkipama.modernminecarts.client;

import net.lordkipama.modernminecarts.ModernMinecarts;
import net.lordkipama.modernminecarts.ModernMinecartsConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.Locale;

public final class ModernMinecartsConfigScreen extends Screen {
    private static final double MIN_SPEED = 0.01D;
    private static final double MAX_SPEED = 1.6D;
    private static final int COLUMN_GAP = 8;
    private static final int COLUMN_WIDTH = 205;

    private final Screen parent;
    private final double[] speeds = new double[7];
    private final boolean[] features = new boolean[6];
    private Text saveError;

    public ModernMinecartsConfigScreen(Screen parent) {
        super(Text.translatable("gui.modernminecarts.config.title"));
        this.parent = parent;
        speeds[0] = ModernMinecartsConfig.copperSpeed();
        speeds[1] = ModernMinecartsConfig.exposedCopperSpeed();
        speeds[2] = ModernMinecartsConfig.weatheredCopperSpeed();
        speeds[3] = ModernMinecartsConfig.oxidizedCopperSpeed();
        speeds[4] = ModernMinecartsConfig.poweredRailSpeed();
        speeds[5] = ModernMinecartsConfig.maxAscendingSpeed();
        speeds[6] = ModernMinecartsConfig.furnaceMinecartSpeed();
        features[0] = ModernMinecartsConfig.enableFurnaceMinecartChunkloading();
        features[1] = ModernMinecartsConfig.enableMinecartChaining();
        features[2] = ModernMinecartsConfig.enableCopperRails();
        features[3] = ModernMinecartsConfig.enableRailCrossing();
        features[4] = ModernMinecartsConfig.enablePoweredDetectorRail();
        features[5] = ModernMinecartsConfig.enableRailJump();
    }

    @Override
    protected void init() {
        int left = (width - (COLUMN_WIDTH * 2 + COLUMN_GAP)) / 2;
        int right = left + COLUMN_WIDTH + COLUMN_GAP;

        addDrawableChild(new SpeedSlider(left, 45, 0, "gui.modernminecarts.config.copper_speed"));
        addDrawableChild(new SpeedSlider(right, 45, 1, "gui.modernminecarts.config.exposed_copper_speed"));
        addDrawableChild(new SpeedSlider(left, 65, 2, "gui.modernminecarts.config.weathered_copper_speed"));
        addDrawableChild(new SpeedSlider(right, 65, 3, "gui.modernminecarts.config.oxidized_copper_speed"));
        addDrawableChild(new SpeedSlider(left, 85, 4, "gui.modernminecarts.config.powered_rail_speed"));
        addDrawableChild(new SpeedSlider(right, 85, 5, "gui.modernminecarts.config.max_ascending_speed"));
        addDrawableChild(new SpeedSlider(left, 105, 6, "gui.modernminecarts.config.furnace_minecart_speed"));

        addFeatureButton(left, 141, 0, "gui.modernminecarts.config.furnace_chunkloading");
        addFeatureButton(right, 141, 1, "gui.modernminecarts.config.minecart_chaining");
        addFeatureButton(left, 161, 2, "gui.modernminecarts.config.copper_rails");
        addFeatureButton(right, 161, 3, "gui.modernminecarts.config.rail_crossing");
        addFeatureButton(left, 181, 4, "gui.modernminecarts.config.powered_detector_rail");
        addFeatureButton(right, 181, 5, "gui.modernminecarts.config.rail_jump");

        addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.done"),
                button -> saveAndClose()
        ).dimensions(width / 2 - 100, height - 28, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, 14, 0xFFFFFF);
        context.drawCenteredTextWithShadow(
                textRenderer,
                Text.translatable("gui.modernminecarts.config.speed_section"),
                width / 2,
                30,
                0xFFFFFF
        );
        context.drawCenteredTextWithShadow(
                textRenderer,
                Text.translatable("gui.modernminecarts.config.features_section"),
                width / 2,
                129,
                0xFFFFFF
        );
        context.drawCenteredTextWithShadow(
                textRenderer,
                Text.translatable("gui.modernminecarts.config.restart_note"),
                width / 2,
                202,
                0xAAAAAA
        );
        if (saveError != null) {
            context.drawCenteredTextWithShadow(textRenderer, saveError, width / 2, height - 48, 0xFF5555);
        }
        super.render(context, mouseX, mouseY, delta);
    }

    private void addFeatureButton(int x, int y, int index, String translationKey) {
        ButtonWidget button = ButtonWidget.builder(featureMessage(translationKey, features[index]), pressed -> {
            features[index] = !features[index];
            pressed.setMessage(featureMessage(translationKey, features[index]));
            saveError = null;
        }).dimensions(x, y, COLUMN_WIDTH, 20).build();
        addDrawableChild(button);
    }

    private Text featureMessage(String translationKey, boolean enabled) {
        return Text.translatable(
                "gui.modernminecarts.config.feature_value",
                Text.translatable(translationKey),
                Text.translatable(enabled ? "options.on" : "options.off")
        );
    }

    private void saveAndClose() {
        boolean saved = ModernMinecartsConfig.apply(
                speeds[0],
                speeds[1],
                speeds[2],
                speeds[3],
                speeds[4],
                speeds[5],
                speeds[6],
                features[0],
                features[1],
                features[2],
                features[3],
                features[4],
                features[5],
                ModernMinecarts.LOGGER
        );
        if (saved) {
            if (client != null) {
                client.setScreen(parent);
            }
        } else {
            saveError = Text.translatable("gui.modernminecarts.config.save_failed");
        }
    }

    private final class SpeedSlider extends SliderWidget {
        private final int index;
        private final String translationKey;

        private SpeedSlider(int x, int y, int index, String translationKey) {
            super(x, y, COLUMN_WIDTH, 20, Text.empty(), (speeds[index] - MIN_SPEED) / (MAX_SPEED - MIN_SPEED));
            this.index = index;
            this.translationKey = translationKey;
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            double speed = MIN_SPEED + value * (MAX_SPEED - MIN_SPEED);
            setMessage(Text.translatable(
                    "gui.modernminecarts.config.speed_value",
                    Text.translatable(translationKey),
                    String.format(Locale.ROOT, "%.2f", speed)
            ));
        }

        @Override
        protected void applyValue() {
            speeds[index] = MIN_SPEED + value * (MAX_SPEED - MIN_SPEED);
            saveError = null;
        }
    }
}
