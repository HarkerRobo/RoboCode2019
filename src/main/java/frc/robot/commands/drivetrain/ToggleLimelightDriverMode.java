package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.util.Limelight;

/**
 * Toggles the Limelight view mode between one for low-exposure vision processing and one for high-exposure human view. 
 */
public class ToggleLimelightDriverMode extends InstantCommand
{
    /**
     * {@inheritDoc}
     */
    public void initialize()
    {
        Limelight.getInstance().toggleCamMode();
    }
}