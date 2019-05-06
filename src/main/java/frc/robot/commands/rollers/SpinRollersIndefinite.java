package frc.robot.commands.rollers;

import java.util.function.Supplier;

import frc.robot.Robot;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Rollers.RollerDirection;
import harkerrobolib.commands.IndefiniteCommand;

/**
 * Spin the rollers at a certain magnitude and direction while button is
 * pressed.
 * 
 * @author Chirag Kaushik
 * @since 1/12/19
 */
public class SpinRollersIndefinite extends IndefiniteCommand {
   private RollerDirection direction;
   private Supplier<Double> magnitudeLambda;
   private boolean isStopped;

   public final static double magnitude2 = 0.49;
   public final static double magnitude1 = 0.5;

   private double prevTime = 0.0;

   public SpinRollersIndefinite(double magnitude, RollerDirection direction) {
      this(() -> magnitude, direction);
   }

   public SpinRollersIndefinite(Supplier<Double> magnitudeLambda, RollerDirection direction) {
      requires(Rollers.getInstance());
      this.direction = direction;
      this.magnitudeLambda = magnitudeLambda;
   }

   @Override
   public void initialize() {
      isStopped = false;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void execute() {
      prevTime = Robot.getTime();
      if(Rollers.getInstance().talon().getOutputCurrent() > Integer.MAX_VALUE)
         isStopped = true;
      if(!isStopped)
         Rollers.getInstance().moveRollers(magnitudeLambda.get(), direction);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void end() {
      Rollers.getInstance().stopRollers();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void interrupted() {
      Rollers.getInstance().stopRollers();
   }
}