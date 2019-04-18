package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.RobotMap;
import frc.robot.RobotMap.RobotType;
import frc.robot.subsystems.Drivetrain;
import frc.robot.util.Limelight;
import frc.robot.util.PIDOutputGetter;
import frc.robot.util.PIDSourceCustomGet;
import harkerrobolib.util.MathUtil;

/**
 * Drives towards a target using the Limelight camera.
 * 
 * @author Jatin Kohli
 * @author Chirag Kaushik
 * @author Finn Frankis
 * @author Angela Jia
 * @since 1/12/19
 */
public class AlignWithLimelightDrive extends Command {

   public static final double TURN_KP, TURN_KI, TURN_KD, TURN_KF;
   public static final double FORWARD_KP, FORWARD_KI, FORWARD_KD, FORWARD_KF;

   static {
      if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
         TURN_KP = .03; // 0.09
         TURN_KI = 0;// 0.001;
         TURN_KD = 0.1;
         TURN_KF = 0;
         FORWARD_KF = 0;
         FORWARD_KP = 0.045;
         FORWARD_KI = 0;// 0.001;
         FORWARD_KD = 0.2;
      } else {
         TURN_KP = .03; // 0.09
         TURN_KI = 0;// 0.001;
         TURN_KD = 0.1;
         TURN_KF = 0;
         FORWARD_KF = 0;
         FORWARD_KP = 0.045;
         FORWARD_KI = 0;// 0.001;
         FORWARD_KD = 0.2;
      }
   }

   public static final double TURN_ALLOWABLE_ERROR = 0.054;
   public static final double FORWARD_ALLOWABLE_ERROR = 0.05;

   private PIDOutputGetter turnOutput;

   private PIDController turnController;
   private double txSetpoint;

   public AlignWithLimelightDrive(double txSetpoint) {
      requires(Drivetrain.getInstance());

      this.txSetpoint = txSetpoint;

      turnOutput = new PIDOutputGetter();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void initialize() {
      System.out.println("align with limelight");
      turnController = new PIDController(TURN_KP, TURN_KI, TURN_KD, TURN_KF,
            new PIDSourceCustomGet(() -> Limelight.getInstance().getTx(), PIDSourceType.kDisplacement), turnOutput);

      turnController.enable();

      turnController.setSetpoint(txSetpoint);
   }

   /**
    * {@inheritDoc}
    */
   public void execute() {
      double turnOutputVal = turnOutput.getOutput();

      SmartDashboard.putNumber("Turn Error", turnController.getError());
      SmartDashboard.putNumber("Left Vel", Drivetrain.getInstance().getLeftMaster().getSelectedSensorVelocity());
      SmartDashboard.putNumber("Right Vel", Drivetrain.getInstance().getRightMaster().getSelectedSensorVelocity());

      double leftDriverY = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftY(),
            OI.DRIVER_DEADBAND);

      Drivetrain.getInstance().getLeftMaster().set(ControlMode.PercentOutput,
            /* forwardOutputVal */ leftDriverY - turnOutputVal /*- angleOutputVal*/);
      Drivetrain.getInstance().getRightMaster().set(ControlMode.PercentOutput,
            /* forwardOutputVal + */ leftDriverY + turnOutputVal /* + angleOutputVal */);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void end() {
      Drivetrain.getInstance().setBoth(ControlMode.Disabled, 0);
      turnController.disable();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void interrupted() {
      end();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isFinished() {
      return false;
   }
}