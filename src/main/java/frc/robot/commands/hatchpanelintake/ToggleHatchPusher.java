package frc.robot.commands.hatchpanelintake;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.HatchPusher;

/**
 * Toggles the hatch panel intake direction
 * 
 * @author Chirag Kaushik
 * @since January 14, 2019
 */
public class ToggleHatchPusher extends InstantCommand {

    @Override
    public void initialize() {
        HatchPusher.getInstance().setSolenoidState(HatchPusher.getInstance().getSolenoidState() == DoubleSolenoid.Value.kForward ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward);
    }
}