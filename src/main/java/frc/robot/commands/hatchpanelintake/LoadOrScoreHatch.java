package frc.robot.commands.hatchpanelintake;

import edu.wpi.first.wpilibj.command.WaitCommand;
import frc.robot.subsystems.HatchLatcher;
import frc.robot.subsystems.HatchLatcher.ExtenderDirection;
import frc.robot.subsystems.HatchLatcher.FlowerDirection;
import harkerrobolib.auto.SequentialCommandGroup;

/**
 * Chooses between loading or scoring a hatch.
 * 
 * @author Finn Frankis
 * @author Chirag Kaushik
 * 
 * @since 2/2/19
 */
public class LoadOrScoreHatch extends SequentialCommandGroup {
   public enum ScoreState {
      LOAD(FlowerDirection.OPEN), SCORE(FlowerDirection.CLOSED);
      private FlowerDirection direction;

      private ScoreState(FlowerDirection direction) {
         this.direction = direction;
      }

      public FlowerDirection getFlowerDirection() {
         return direction;
      }
   }

   public LoadOrScoreHatch(ScoreState scoreState) {
      super(new SetExtenderState(ExtenderDirection.OUT),
            HatchLatcher.getInstance().getExtenderState() == ExtenderDirection.IN
                  ? new WaitCommand(HatchLatcher.EXTENDER_EXTEND_TIME)
                  : new WaitCommand(0.0),
            new SetFlowerManual(scoreState.getFlowerDirection()));
   }
}