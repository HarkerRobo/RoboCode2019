package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.util.Limelight;

public class ToggleLimelightDriverMode extends InstantCommand
{
    public void initialize()
    {
        Limelight.getInstance().toggleCamMode();
    }
    
}