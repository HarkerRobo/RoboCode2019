package frc.robot.commands.drivetrain;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.util.Limelight;

/**
 * Toggles the Limelight view mode between one for low-exposure vision processing and one for high-exposure human view. 
 */
public class SetLimelightViewMode extends InstantCommand
{
    private ViewMode mode;
    public enum ViewMode {
        VISION (Limelight.VISION_MODE), DRIVER(Limelight.DRIVER_MODE);
        private int viewState;
        private ViewMode (int viewState) {
            this.viewState = viewState;
        }
        public int getViewState () {
            return viewState;
        }
    }
    
    public SetLimelightViewMode (ViewMode mode) {
        this.mode = mode;
    }
    /**
     * {@inheritDoc}
     */
    public void initialize()
    {
        Limelight.getInstance().table.getEntry(Limelight.MODE_KEY).setNumber(mode.getViewState());
    }
}