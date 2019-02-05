package frc.robot.util;

import edu.wpi.first.wpilibj.DigitalOutput;

/**
 * Controls the LED by communicating with the digital outputs.
 * 
 * @author Chirag Kaushik
 * @since February 2, 2019
 */
public class LEDController {
    private static LEDController instance;

    public static int NUM_BITS;

    private DigitalOutput[] digitalOutputs;

    private LEDController() {
        digitalOutputs = new DigitalOutput[NUM_BITS];
        for(int i = 0;i < NUM_BITS;i ++) {
            digitalOutputs[i] = new DigitalOutput(i);
        }
    }

    public void setPattern(int pattern) {
        for(int i = 0;i < NUM_BITS;i ++) {
            digitalOutputs[i].set((pattern & (1 << i)) > 0);
        }
    }

    public static void setNumBits(int bits) {
        NUM_BITS = bits;
    }
    
    public static LEDController getInstance() {
        if(instance == null)
            instance = new LEDController();
        return instance;
    }
}