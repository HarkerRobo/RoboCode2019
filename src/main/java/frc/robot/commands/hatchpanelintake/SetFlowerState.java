package frc.robot.commands.hatchpanelintake;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.subsystems.HatchLatcher;
import frc.robot.subsystems.HatchLatcher.FlowerDirection;

/**
 * Set the flower direction.
 * 
 * @author Shahzeb Lakhani
 * @since 1/31/19
 */
public class SetFlowerState extends InstantCommand {
   private FlowerDirection direction;

   public SetFlowerState(FlowerDirection direction) {
      requires(HatchLatcher.getInstance());
      this.direction = direction;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void initialize() {
      Robot.log("Flower " + (direction == FlowerDirection.OPEN ? "opened" : "closecd") + ".");
      HatchLatcher.getInstance().setFlowerState(direction);
   }
}