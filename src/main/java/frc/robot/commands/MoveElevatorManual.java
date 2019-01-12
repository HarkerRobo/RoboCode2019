package frc.robot.commands;

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
 * @author  Angela Jia
 * @version 1/10/19
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
           // boolean reverseBeyondLimit = Elevator.getInstance().getVictorTwo().getSelectedSensorPosition();
            //boolean 
        }
        
    }
}