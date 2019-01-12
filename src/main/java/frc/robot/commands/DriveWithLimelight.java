package frc.robot.commands;

import frc.robot.util.Limelight;
import frc.robot.subsystems.Drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;


/**
 * Drives towards a target using the Limelight camera
 * 
 * @author Jatin Kohli
 * @author Chirag Kaushik
 * @version January 12, 2019
 */
public class DriveWithLimelight extends Command {
    private Limelight limelight;
    private final double SPEED_MULTIPLIER = 0.01;

    public DriveWithLimelight() {
        requires(Drivetrain.getInstance());
        limelight = limelight.getInstance();
    }

    @Override
    public void execute() {
        double outputLeft = 0.0;
        double outputRight = 0.0;

        double tx = limelight.getTx();
        double ta = limelight.getTa(); 
        if(limelight.isTargetVisible()) {
            outputLeft = tx
            outputRight = 
        }
        
        outputLeft *= SPEED_MULTIPLIER;
        outputRight *= SPEED_MULTIPLIER;        
        Drivetrain.getInstance().getLeftMaster().set(ControlMode.PercentOutput, outputLeft);
        Drivetrain.getInstance().getRightMaster().set(ControlMode.PercentOutput, outputRight);
    }

    @Override
    public void end() {
        Drivetrain.getInstance().setBoth(ControlMode.Disabled, 0);
    }

    @Override
    public void interrupted() {
        Drivetrain.getInstance().setBoth(ControlMode.Disabled, 0);
    }

    @Override
    public boolean isFinished() {
        return false;        
    }
}