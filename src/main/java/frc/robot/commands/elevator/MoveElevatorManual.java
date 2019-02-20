package frc.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import frc.robot.OI;
import frc.robot.subsystems.Elevator;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;

/**
 * Moves elevator based on driver right joystick input.
 * 
 * @author Angela Jia
 * @since 1/10/19
 */
public class MoveElevatorManual extends IndefiniteCommand {

    public MoveElevatorManual() {
        requires(Elevator.getInstance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        //Elevator.getInstance().getMasterTalon().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        double desiredSpeed = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getRightY(), OI.DRIVER_DEADBAND);
        Elevator.getInstance().setElevator(ControlMode.PercentOutput, desiredSpeed);
        // if(Math.abs(desiredSpeed) > 0)
        // {
        //     boolean isDown = desiredSpeed < 0;
        //     int position = Elevator.getInstance().getMaster().getSelectedSensorPosition(Global.PID_PRIMARY);
        //     boolean reverseBeyondLimit = position
        //                                  <= Elevator.REVERSE_SOFT_LIMIT;
        //     double currSpeed = Elevator.getInstance().getMaster().getSelectedSensorVelocity(Global.PID_PRIMARY);
        //     double distFromSoft = position - Elevator.REVERSE_SOFT_LIMIT;
        //     double outputFactor = 1.0;
        //     if(isDown && reverseBeyondLimit && Math.abs(currSpeed) / Elevator.MAX_SPEED < Elevator.SLOW_DOWN_PERCENT)
        //     {
        //         outputFactor = MathUtil.map(distFromSoft, 0, Elevator.REVERSE_SOFT_LIMIT, Elevator.MAX_OUTPUT_FACTOR, Elevator.MIN_LESS_OUTPUT_FACTOR);
        //     }
        //     else if(isDown && reverseBeyondLimit) {
        //         outputFactor = MathUtil.map(distFromSoft, 0, Elevator.REVERSE_SOFT_LIMIT, Elevator.MAX_OUTPUT_FACTOR, Elevator.MIN_MORE_OUTPUT_FACTOR);
        //     }
        //     desiredSpeed *= outputFactor;
        //     Elevator.getInstance().moveElevatorVelocity(desiredSpeed);
        // }
        // else {
        //     Elevator.getInstance().moveElevatorVelocity(0);
        // }
        
    }
    
}