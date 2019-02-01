package frc.robot.commands.hatchpanelintake;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.HatchLatcher;
import frc.robot.subsystems.HatchLatcher.FlowerDirection;
import frc.robot.util.PneumaticsUtil;

/**
 * Toggles flower direction.
 * 
 * @author Shahzeb Lakhani
 * @since 1/31/19
 */
public class ToggleFlowerManual extends InstantCommand{
    public ToggleFlowerManual() {
        requires(HatchLatcher.getInstance());       
    }

    public void initialize() {
        HatchLatcher.getInstance().setFlowerState(FlowerDirection.convertDirection(PneumaticsUtil.
                                                                    switchSolenoidValue(HatchLatcher.
                                                                     getInstance().getFlowerState())));
    }

          
}