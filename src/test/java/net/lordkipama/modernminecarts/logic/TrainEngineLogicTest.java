package net.lordkipama.modernminecarts.logic;

public final class TrainEngineLogicTest {
    public static void main(String[] args) {
        for (double cap : new double[]{0.01D, 0.1D, 0.2D, 0.4D, 0.8D, 1.6D}) {
            for (int children = 0; children <= MinecartTuning.MAX_TRAIN_LENGTH; children++) {
                for (int engines = 1; engines <= 4; engines++) {
                    double speed = TrainEngineLogic.calculateSpeedLimit(cap, children, engines);
                    if (speed <= 0 || speed > cap) {
                        throw new AssertionError("Speed " + speed + " exceeds cap " + cap
                                + " for " + children + " carts and " + engines + " engines");
                    }
                }
            }
        }
        assertSpeed(0.4D, TrainEngineLogic.calculateSpeedLimit(0.4D, 2, 1));
        assertSpeed(0.36D, TrainEngineLogic.calculateSpeedLimit(0.4D, 3, 1));
        assertSpeed(0.2D, TrainEngineLogic.calculateSpeedLimit(0.4D, 128, 1));
        assertSpeed(0.1D, TrainEngineLogic.calculateSpeedLimit(0.1D, 128, 1));
    }

    private static void assertSpeed(double expected, double actual) {
        if (Math.abs(expected - actual) > 1.0E-9D) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }
}
