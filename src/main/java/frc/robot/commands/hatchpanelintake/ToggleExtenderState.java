
package frc.robot.commands.hatchpanelintake;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystems.HatchLatcher;
import frc.robot.subsystems.HatchLatcher.ExtenderDirection;
import frc.robot.util.PneumaticsUtil;

/**
 * Toggles extender direction.
 * 
 * @author Angela Jia
 * @author Shahzeb Lakhani
 * @since 1/29/19
 */
public class ToggleExtenderState extends InstantCommand {

    public ToggleExtenderState(){
        requires(HatchLatcher.getInstance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(){
        Robot.log("Extender moved " + (HatchLatcher.getInstance().getExtenderState() == ExtenderDirection.OUT ? "in" : "out") + ".");
        HatchLatcher.getInstance().setExtenderState(ExtenderDirection.convertDirection(
            PneumaticsUtil.switchSolenoidValue(HatchLatcher.getInstance().getExtenderState()
            .getValue())));
    }
}