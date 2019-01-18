package frc.robot.commands.arm;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.Arm;

/**
 * Toggles the arm's position to the state it currently is not.
 * 
 * @since 1/11/19
 */
public class ToggleArmPosition extends InstantCommand {

    public ToggleArmPosition() {
        requires(Arm.getInstance());
    }

    @Override
    public void execute() {
        DoubleSolenoid.Value newValue = Arm.getInstance().getState() == DoubleSolenoid.Value.kForward ? DoubleSolenoid.Value.kReverse : DoubleSolenoid.Value.kForward;
        Arm.getInstance().setState(newValue);
    }
}