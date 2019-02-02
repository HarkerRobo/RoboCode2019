package frc.robot.commands.hatchpanelintake;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.HatchLatcher;

/**
 * Toggles extender direction.
 * 
 * @author Angela Jia
 * @author Shahzeb Lakhani
 * @since 1/29/19
 */
public class ToggleExtenderManual extends InstantCommand {

    public ToggleExtenderManual(){
        requires(HatchLatcher.getInstance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(){
        HatchLatcher.getInstance().setExtenderState(HatchLatcher.getInstance().getExtenderState());
    }
}