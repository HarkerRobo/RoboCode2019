package frc.robot.commands.rollers;

import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Rollers.RollerDirection;
import harkerrobolib.commands.IndefiniteCommand;

/**
 * Allows manual control over the rollers for intake and outtake.
 * 
 * @author Chirag Kaushik
 * @author Shahzeb Lakhani
 * @author Dawson Chen
 * @since 1/10/19
 */
public class SpinRollersManual extends IndefiniteCommand {

   public SpinRollersManual() {
      requires(Rollers.getInstance());
      Robot.log("SpinRollersManual constructed.");
   }

   /**
    * The driver controller will take priority and spin the roller according to the
    * amount that the right and left triggers are pressed. {@inheritDoc}
    */
   @Override
   public void execute() {
      if(OI.getInstance().getRunRollersAndIntake()) {
         Rollers.getInstance().moveRollers(Rollers.DEFAULT_ROLLER_MAGNITUDE, RollerDirection.IN);
      } else {
         Rollers.getInstance().moveRollers(0, RollerDirection.IN);
      }
   }
}