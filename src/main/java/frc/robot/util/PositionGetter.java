package frc.robot.util;

import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Drivetrain;
/**
 * Calculates the current position of the robot.
 * 
 * @since 1/22/19
 * 
 * @author Jatin Kohli
 */
public class PositionGetter
{
    private double velocity = 0;
    private double position = 0;

    private double prevVelocity = 0;
    private double prevPosition = 0;

    private double prevTime = System.currentTimeMillis();

    public void updatePosition()
    {
        double dt = System.currentTimeMillis() - prevTime;
        velocity += (1.0/2) * dt * (Drivetrain.getInstance().getLeftMaster().getSelectedSensorPosition(Global.PID_PRIMARY));
    }

    public double getPosition()
    {
        return position;
    }
}