package frc.robot.commands.drivetrain;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class ToggleLimelightLEDMode extends InstantCommand {
    private static final int LED_OFF = 1;
    private static final int LED_ON = 3;

    public void initialize() {
        int currentLEDValue = NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").getNumber(0.0).intValue();
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(currentLEDValue == LED_OFF ? LED_ON : LED_OFF);
    }
}