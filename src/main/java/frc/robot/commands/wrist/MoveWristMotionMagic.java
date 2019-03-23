package frc.robot.commands.wrist;

import java.util.function.Supplier;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.RobotMap.RobotType;
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
public class MoveWristMotionMagic extends Command {
   private double position;
   private Supplier<Integer> setpointLambda;

   public static final double KF;
   public static final double KP;
   public static final double KI;
   public static final double KD;
   public static final int IZONE;
   public static final int ACCELERATION;
   public static final int CRUISE_VELOCITY;

   static {
      if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
         KF = 2.79;
         KP = 1.1;
         KI = 0.0025;
         KD = 75;
         IZONE = 150;
         ACCELERATION = (int) (315 * 1.20);
         CRUISE_VELOCITY = (int) (300 * 1.28);
      } else {
         KF = 2.79;
         KP = 1.1;
         KI = 0.0025;
         KD = 75;
         IZONE = 150;
         ACCELERATION = (int) (315 * 1.20);
         CRUISE_VELOCITY = (int) (300 * 1.28);
      }
   }

   public MoveWristMotionMagic(double angle) {
      requires(Wrist.getInstance());
      this.position = Wrist.getInstance().convertDegreesToEncoder(angle);
   }

   public MoveWristMotionMagic(Supplier<Integer> setpointLambda) {
      super(0);
      this.setpointLambda = setpointLambda;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void initialize() {
      if (setpointLambda != null) {
         this.position = Wrist.getInstance().convertDegreesToEncoder(setpointLambda.get());
      }

      System.out.println("entering wrist motion " + position);
      Robot.log("MoveElevatorMotionMagic initialized with desired position, " + position + ".");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void execute() {
      Wrist.getInstance().setWrist(ControlMode.MotionMagic, position);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected boolean isFinished() {
      return Math
            .abs(position - Wrist.getInstance().getMasterTalon().getSelectedSensorPosition()) <= Wrist.ALLOWABLE_ERROR;
   }

   @Override
   public void end() {
      System.out.println("wrist motion magic end");

      Robot.log("MoveWristMotionMagic ended.");
   }

   @Override
   public void interrupted() {
      end();
   }
}