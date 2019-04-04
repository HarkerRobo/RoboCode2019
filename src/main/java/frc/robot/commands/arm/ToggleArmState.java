package frc.robot.commands.arm;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmDirection;

/**
 * Toggles the arm's position to the state it currently is not.
 * 
 * @author Angela Jia
 * @author Chirag Kaushik
 * @since 1/11/19
 */
public class ToggleArmState extends InstantCommand {

   public ToggleArmState() {
      requires(Arm.getInstance());
   }

   public void initialize() {
      Robot.log("Arm moved " + (Arm.getInstance().getDirection() == ArmDirection.DOWN ? "up" : "down") + ".");
   }

   @Override
   public void execute() {
      DoubleSolenoid.Value newValue = Arm.getInstance().getState() == DoubleSolenoid.Value.kForward
           ? DoubleSolenoid.Value.kReverse
           : DoubleSolenoid.Value.kForward;
      Arm.getInstance().setState(newValue);
   }
}