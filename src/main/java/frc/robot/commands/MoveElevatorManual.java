package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import frc.robot.OI;
import frc.robot.subsystems.Elevator;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;

/**
 * Moves elevator based on driver right joystick input.
 * 
 * @author  Angela Jia
 * @version 1/
 */
public class MoveElevatorManual extends IndefiniteCommand {

    public MoveElevatorManual() {
        requires(Elevator.getInstance());
    }

    @Override
    public void initialize() {
        //Elevator.getInstance().getMaster().confi
    }

    @Override
    public void execute() {
        OI oi = OI.getInstance();
        double speed = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getRightY(), OI.DRIVER_DEADBAND);
        Elevator.getInstance().getMaster().set(ControlMode.PercentOutput, speed);
        if(Math.abs(speed) > oi.DRIVER_DEADBAND)
        {
            boolean isDown = speed < 0;
            //boolean 
        }
        
    }
}