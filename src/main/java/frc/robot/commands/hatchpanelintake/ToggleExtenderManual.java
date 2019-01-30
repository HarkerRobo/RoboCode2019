package frc.robot.commands.hatchpanelintake;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.HatchLatcher;
import frc.robot.subsystems.HatchLatcher.ExtenderDirection;
import frc.robot.util.PneumaticsUtil;

/**
 * Toggles extender direction.
 * 
 * @author Angela Jia
 * @since 1/29/19
 */
public class ToggleExtenderManual extends InstantCommand {

    public ToggleExtenderManual(){
        requires(HatchLatcher.getInstance());
    }
    public void initialize(){
        HatchLatcher.getInstance().setExtenderState(ExtenderDirection.convertDirection(
                                                        PneumaticsUtil.switchSolenoidValue(
                                                            HatchLatcher.getInstance().getExtenderState())
                                                            )
                                                            );
    }
}