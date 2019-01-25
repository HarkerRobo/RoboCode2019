package frc.robot.util;

import frc.robot.RobotMap.CAN_IDs;
import harkerrobolib.wrappers.HSPigeon;

/**
 * Calculates the position the robot has travelled given acceleration values.
 * 
 * @since 1/22/19
 * 
 * @author Jatin Kohli
 * @author Chirag Kaushik
 */
public class AccelerationIntegrator {
    private static AccelerationIntegrator instance;

    private static HSPigeon pigeon;

    private double previousAcceleration;
    private double previousVelocity;
    private long previousTime;  
    
    private double velocity;
    private double position;

    private short[] accelData;

    private static final int X_INDEX = 0; //Left and Right
    private static final int Y_INDEX = 1; //Forward and Backward
    private static final int Z_INDEX = 2; //Up and Down
    
    private AccelerationIntegrator() {   

        pigeon = new HSPigeon(CAN_IDs.PIGEON);

        previousAcceleration = 0;
        previousVelocity = 0;
        previousTime = System.currentTimeMillis();

        velocity = 0;
        position = 0;

        accelData = new short[3];
    }

    /**
     * Updates the position of the Robot by doubly integrating the Robot's velocity
     */
    public void updatePosition() {
        long dt = System.currentTimeMillis() - previousTime;
        pigeon.getBiasedAccelerometer(accelData); //Fills array with data

        velocity += 1/2.0 * (dt) * (previousAcceleration + accelData[Y_INDEX]);
        position += 1/2.0 * (dt) * (velocity + previousVelocity);
        
        previousVelocity = velocity;
        previousAcceleration = accelData[Y_INDEX];
        previousTime = System.currentTimeMillis();
    }

    public double getPosition() { 
        return position;
    }

    public static AccelerationIntegrator getInstance() {
        if(instance == null) {
            instance = new AccelerationIntegrator();
        }
        return instance;        
    }    
}