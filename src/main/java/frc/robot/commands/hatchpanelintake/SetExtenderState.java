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
public class SetExtenderState extends InstantCommand {
    private ExtenderDirection direction;
    
    public SetExtenderState(ExtenderDirection direction) {
        requires(HatchLatcher.getInstance());
        
        this.direction = direction;                
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() {
        HatchLatcher.getInstance().setExtenderState(direction);
    }    
}