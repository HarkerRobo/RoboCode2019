package frc.robot.commands.wrist;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Wrist;

/**
 * Moves the wrist to a given position.
 * 
 * @author Finn Frankis
 * @author Jatin Kohli
 * @author Chirag Kaushik
 * 
 * @since 1/12/19
 */
public class MoveWristPosition extends Command {
   private double position;

   public static final double KF = 0.0;
   public static final double KP = 1.1;
   public static final double KI = 0.0025;
   public static final double KD = 75;
   public static final int IZONE = 150;

   public MoveWristPosition(double angle) {
      requires(Wrist.getInstance());
      this.position = Wrist.getInstance().convertDegreesToEncoder(angle);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void initialize() {
      Wrist.getInstance().setupPositionPID();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void execute() {
      SmartDashboard.putNumber("Wrist Error", Wrist.getInstance().getMasterTalon().getClosedLoopError());
      Wrist.getInstance().setWrist(ControlMode.Position, position);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected boolean isFinished() {
      return false;// return
                  // Math.abs(Wrist.getInstance().getMasterTalon().getClosedLoopError(Wrist.POSITION_SLOT))
                  // < Wrist.ALLOWABLE_ERROR;
   }
}