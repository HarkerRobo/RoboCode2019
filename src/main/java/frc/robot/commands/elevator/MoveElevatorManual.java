package frc.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import frc.robot.OI;
import frc.robot.RobotMap.Global;
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

    @Override
    public void initialize() {
        Elevator.getInstance().getMaster().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Global.PID_PRIMARY);
    }

    @Override
    public void execute() {
        OI oi = OI.getInstance();
        double speed = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getRightY(), OI.DRIVER_DEADBAND);
        Elevator.getInstance().getMaster().set(ControlMode.PercentOutput, speed);
        if(Math.abs(speed) > oi.DRIVER_DEADBAND)
        {
            boolean isDown = speed < 0;
            int position = Elevator.getInstance().getMaster().getSelectedSensorPosition(Global.PID_PRIMARY);
            boolean reverseBeyondLimit = position
                                         <= Elevator.REVERSE_SOFT_LIMIT;
            double currSpeed = Elevator.getInstance().getMaster().getSelectedSensorVelocity(Global.PID_PRIMARY);
            double distFromSoft = position - Elevator.REVERSE_SOFT_LIMIT;
            double outputFactor = 1.0;
            if(isDown && reverseBeyondLimit && Math.abs(currSpeed) / Elevator.MAX_SPEED < Elevator.SLOW_DOWN_PERCENT)
            {
                outputFactor = MathUtil.map(distFromSoft, 0, Elevator.REVERSE_SOFT_LIMIT, 1, 0);
            }
            else if(isDown && reverseBeyondLimit && Math.abs(currSpeed) / Elevator.MAX_SPEED >= Elevator.SLOW_DOWN_PERCENT) {
                outputFactor = MathUtil.map(distFromSoft, 0, Elevator.REVERSE_SOFT_LIMIT, 1, -0.5);
            }
            speed *= outputFactor;
            Elevator.getInstance().moveElevatorVelocity(speed);
        }
        else {
            Elevator.getInstance().moveElevatorVelocity(0);
        }
        
    }
}