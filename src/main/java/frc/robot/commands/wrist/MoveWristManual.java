package frc.robot.commands.wrist;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import frc.robot.OI;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristDirection;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;

/**
 * Moves the wrist manually.
 * @author Finn Frankis
 * 
 * @since 1/10/19
 */
public class MoveWristManual extends IndefiniteCommand {
    public MoveWristManual () {
        requires (Wrist.getInstance());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize () {
        Wrist.getInstance().getMasterTalon().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        double leftOperatorTrigger = OI.getInstance().getDriverGamepad().getLeftTrigger();
        double rightOperatorTrigger = OI.getInstance().getDriverGamepad().getRightTrigger();

        //double currentPosition = Wrist.getInstance().getMasterTalon().getSelectedSensorPosition(Global.PID_PRIMARY);
        
        if (leftOperatorTrigger > rightOperatorTrigger) {
            /*double distFromBack = Math.abs(currentPosition - Wrist.MAX_BACKWARD_POSITION);

            if (distFromBack <= Wrist.SLOW_DOWN_DISTANCE_FROM_ENDPOINT) {
                leftOperatorTrigger *= getOutputFactorFromEndpointDistance(distFromBack);
            }*/
            
            Wrist.getInstance().setWrist(leftOperatorTrigger, WristDirection.TO_BACK);
        }
        else {
            /*double distFromFront = Math.abs(currentPosition - Wrist.MAX_BACKWARD_POSITION);

            if (distFromFront <= Wrist.SLOW_DOWN_DISTANCE_FROM_ENDPOINT) {
                rightOperatorTrigger *= getOutputFactorFromEndpointDistance(distFromFront);*/
                Wrist.getInstance().setWrist(rightOperatorTrigger, WristDirection.TO_FRONT);
            }
    }

    /**
     * Gets the factor by which the output should be scaled down given the distance from an endpoint (assuming it has entered the slow-down zone).
     */
    private double getOutputFactorFromEndpointDistance (double dist) {
        return MathUtil.map(dist, 0, Wrist.SLOW_DOWN_DISTANCE_FROM_ENDPOINT, Wrist.MIN_PERCENT_OUTPUT, Wrist.MAX_PERCENT_OUTPUT); // when the distance from the endpoint is zero, wrist should be moving at minimum speed (stopped); when it is just entering the slow-down zone, wrist should be moving at maximum speed, with linear mapping in between
    }
}