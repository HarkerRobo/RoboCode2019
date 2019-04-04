package frc.robot.commands.arm;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystems.Arm;
import frc.robot.subsystems.Arm.ArmDirection;

/**
 * Sets the arm to a given position using pneumatics.
 * 
 * @author Angela Jia
 * @author Chirag Kaushik
 * @since 1/11/19
 */
public class SetArmState extends InstantCommand {
   private ArmDirection direction;

   public SetArmState(ArmDirection direction) {
      requires(Arm.getInstance());
      this.direction = direction;

   }

   public void initialize() {
      Arm.getInstance().setState(direction.getState());
      Robot.log("Arm moved " + (direction == ArmDirection.DOWN ? "down" : "up") + ".");
   }
}