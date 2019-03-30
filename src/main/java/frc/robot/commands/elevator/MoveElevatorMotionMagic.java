package frc.robot.commands.elevator;

import java.util.function.Supplier;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.RobotMap.RobotType;
import frc.robot.subsystems.Elevator;

/**
 * Moves the elevator to a specified setpoint using motion magic.
 * 
 * @author Dawson Chen
 * @author Angela Jia
 * @since 1/14/19
 */
public class MoveElevatorMotionMagic extends Command {

   private int setpoint;

   /**
    * Elevator motion magic constants
    */
   public static final double KF;
   public static final double KP;
   public static final double KI;
   public static final double KD;
   public static final int IZONE;
   public static final int MOTION_MAGIC_ACCELERATION;
   public static final int CRUISE_VELOCITY;

   public static final boolean MOTION_MAGIC_SENSOR_PHASE;
   public static final int ALLOWABLE_ERROR;

   static {
      if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
         KF = 0.31;
         KP = 0.26;
         KI = 0.0015;
         KD = 5;
         IZONE = 500;
         MOTION_MAGIC_ACCELERATION = 10000;
         CRUISE_VELOCITY = 6000;

         MOTION_MAGIC_SENSOR_PHASE = false;
         ALLOWABLE_ERROR = 100;

      } else {
         KF = 0.31;
         KP = 0.26;
         KI = 0.0015;
         KD = 5;
         IZONE = 500;
         MOTION_MAGIC_ACCELERATION = 15000;
         CRUISE_VELOCITY = 11000;

         MOTION_MAGIC_SENSOR_PHASE = false;
         ALLOWABLE_ERROR = 100;
      }
   }

   private Supplier<Integer> setpointLambda;

   public MoveElevatorMotionMagic(int setpoint) {
      requires(Elevator.getInstance());
      this.setpoint = setpoint;
   }

   public MoveElevatorMotionMagic(Supplier<Integer> setpointLambda) {
      this(0);
      this.setpointLambda = setpointLambda;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected boolean isFinished() {
      return Math
            .abs(setpoint - Elevator.getInstance().getMasterTalon().getSelectedSensorPosition()) <= ALLOWABLE_ERROR;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void initialize() {
      if (setpointLambda != null) {
         this.setpoint = setpointLambda.get();
      }
      System.out.println("EL MOTION MAGIC " + setpoint);
      Robot.log("MoveElevatorMotionMagic initialized.");

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void execute() {
      Elevator.getInstance().setElevator(ControlMode.MotionMagic, setpoint);
      SmartDashboard.putNumber("el error", Elevator.getInstance().getMasterTalon().getClosedLoopError());

   }

   public void end() {
      System.out.println("command ended");
      Robot.log("MoveElevatorMotionMagic ended.");
   }
}
