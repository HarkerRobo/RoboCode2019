package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;

import frc.robot.OI;
import frc.robot.subsystems.Drivetrain;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;

/**
 * Drives based on driver gamepad joystick input.
 * 
 * @author Chirag Kaushik
 * @since 1/7/19
 */
public class DriveWithVelocityManual extends IndefiniteCommand {  
    
    public DriveWithVelocityManual() {
        requires(Drivetrain.getInstance());
    }

    @Override
    public void initialize() {
        Drivetrain.getInstance().setBoth(ControlMode.PercentOutput, 0.0);
    }

    @Override
    public void execute() {
        double leftDriverX = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftX(), OI.DRIVER_DEADBAND);
        double leftDriverY = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftY(), OI.DRIVER_DEADBAND);

        Drivetrain.getInstance().arcadeDrivePercentOutput(leftDriverY, leftDriverX);
    }       
    
    @Override
    public void end() {
        Drivetrain.getInstance().setBoth(ControlMode.Disabled, 0);
    }

    @Override
    public void interrupted() {
        Drivetrain.getInstance().setBoth(ControlMode.Disabled, 0.0);
    }
}
