package frc.robot.commands.hatchpanelintake;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.HatchLatcher;
import frc.robot.subsystems.HatchLatcher.ExtenderDirection;
/**
 * Sets the extender's state.
 * 
 * @author Shahzeb Lakhani
 * @since 1/29/19
 */
public class SetExtenderManual extends InstantCommand {
    private ExtenderDirection direction;
    
    public SetExtenderManual(ExtenderDirection direction) {
        requires(HatchLatcher.getInstance());
        
        this.direction = direction;                
    }

    /**
     * {@inheritDoc}
     */
    public void initialize() {
        HatchLatcher.getInstance().setExtenderState(direction);
    }    
}