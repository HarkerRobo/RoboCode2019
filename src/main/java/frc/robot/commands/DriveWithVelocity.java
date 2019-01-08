package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import frc.robot.subsystems.Drivetrain;
import frc.robot.OI;
import frc.robot.RobotMap;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;

/**
 * 
 * @version 1/7/19
 */
public class DriveWithVelocity extends IndefiniteCommand {     
    @Override
    public void initialize() {
        Drivetrain.getInstance().setBoth(ControlMode.PercentOutput, 0.0);
    }

    @Override
    public void execute() {
        double leftDriverX = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftX(), RobotMap.DEADBAND);
        double leftDriverY = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftY(), RobotMap.DEADBAND);

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
