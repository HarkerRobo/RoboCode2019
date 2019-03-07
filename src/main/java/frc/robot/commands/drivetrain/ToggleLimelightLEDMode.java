package frc.robot.commands.drivetrain;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.util.Limelight;

public class ToggleLimelightLEDMode extends InstantCommand {
    public void initialize() {
        int currentLEDValue = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").getNumber(0.0).intValue();
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(currentLEDValue == Limelight.LED_OFF ? Limelight.LED_ON : Limelight.LED_OFF);
    }
}