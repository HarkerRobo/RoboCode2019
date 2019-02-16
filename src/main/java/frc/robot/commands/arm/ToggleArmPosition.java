package frc.robot.commands.arm;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmDirection;

/**
 * Toggles the arm's position to the state it currently is not.
 * 
 * @author Angela Jia
 * @author Chirag Kaushik
 * @since 1/11/19
 */
public class ToggleArmPosition extends InstantCommand {

    public ToggleArmPosition() {
        requires(Arm.getInstance());
    }

    @Override
    public void execute() {
        ArmDirection newDir = Arm.getInstance().getStates() == ArmDirection.DOWN ? ArmDirection.UP : ArmDirection.DOWN;
        Arm.getInstance().setState(newDir);
    }
}