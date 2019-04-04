package frc.robot.commands.climber;

import frc.robot.Robot;
import frc.robot.subsystems.Climber;
import harkerrobolib.commands.IndefiniteCommand;

/**
 * Moves the climber's talons at a specified output to initiate suction.
 * 
 * @author Angela Jia
 * @since 4/3/19
 */
public class MoveClimber extends IndefiniteCommand {

   private double magnitude;

   public MoveClimber(double magnitude) {
      requires(Climber.getInstance());
      this.magnitude = magnitude;
      Robot.log("MoveClimberSuction constructed with " + magnitude + "output.");
   }

   @Override
   public void execute() {
      Climber.getInstance().setClimberOutput(magnitude);
   }

   @Override
   public void end() {
      Climber.getInstance().setClimberOutput(0);
   }
}