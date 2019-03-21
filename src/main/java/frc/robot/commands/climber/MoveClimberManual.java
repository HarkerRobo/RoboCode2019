package frc.robot.commands.climber;

import frc.robot.OI;
import frc.robot.OI.TriggerMode;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Climber.ClimbDirection;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;

/**
 * Moves climber in a specified direction until it hits a limit switch.
 * 
 * @author Finn Frankis
 * @author Angela Jia
 * 
 * @since 3/15/19
 */
public class MoveClimberManual extends IndefiniteCommand {
   
   private ClimbDirection climbDirection;

   public MoveClimberManual(ClimbDirection climberDirection) {
      requires(Climber.getInstance());
   }

   @Override
   protected void execute() {

      double leftTriggerOutput = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftTrigger(), OI.DRIVER_DEADBAND_TRIGGER);
      double rightTriggerOutput = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getRightTrigger(), OI.DRIVER_DEADBAND_TRIGGER);
         
      if (OI.getInstance().getCurrentTriggerMode() == TriggerMode.CLIMB) {
         Climber.getInstance().setClimberOutput(climbDirection, leftTriggerOutput, rightTriggerOutput);
      } else {
         Climber.getInstance().setClimberOutput(ClimbDirection.UP, 0);
      }
   }
}