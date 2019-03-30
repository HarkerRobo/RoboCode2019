package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;

import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.RobotMap.Global;
import frc.robot.RobotMap.RobotType;
import frc.robot.subsystems.Drivetrain;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;

/**
 * Drives with velocity manually.
 * 
 * @author Chirag Kaushik
 * @author Angela Jia
 * @since February 2, 2019
 */
public class DriveWithVelocityManual extends IndefiniteCommand {
   public static final double LEFT_KP;
   public static final double LEFT_KI;
   public static final double LEFT_KD;
   public static final double LEFT_KF;

   public static final double RIGHT_KP;
   public static final double RIGHT_KI;
   public static final double RIGHT_KD;
   public static final double RIGHT_KF;

   static {
      if (RobotMap.ROBOT_TYPE == RobotType.COMP) {
         LEFT_KP = 0.4;
         LEFT_KI = 0.0;
         LEFT_KD = 0.0;
         LEFT_KF = 0.18;

         RIGHT_KP = 0.4;
         RIGHT_KI = 0.0;
         RIGHT_KD = 0.0;
         RIGHT_KF = 0.18;
      } else {
         LEFT_KP = 0.4;
         LEFT_KI = 0.0;
         LEFT_KD = 0.0;
         LEFT_KF = 0.18;

         RIGHT_KP = 0.4;
         RIGHT_KI = 0.0;
         RIGHT_KD = 0.0;
         RIGHT_KF = 0.18;
      }
   }

   public DriveWithVelocityManual() {
      requires(Drivetrain.getInstance());
      Robot.log("DriveWithVelocityManual constructed.");
   }

   public void execute() {
      double turn = OI.getInstance().getDriveStraightMode() ? 0
            : (MathUtil.mapJoystickOutput(OI.currentDriveMode.getTurnFunction().get(), OI.DRIVER_DEADBAND));
      double speed = MathUtil.mapJoystickOutput(OI.currentDriveMode.getSpeedFunction().get(), OI.DRIVER_DEADBAND);

      if (Drivetrain.getInstance().getRightMaster().getSensorCollection()
            .getPulseWidthPosition() == Global.DISCONNECTED_PULSE_WIDTH_POSITION
            || Drivetrain.getInstance().getLeftMaster().getSensorCollection()
                  .getPulseWidthPosition() == Global.DISCONNECTED_PULSE_WIDTH_POSITION) { 
         Drivetrain.getInstance().arcadeDrivePercentOutput(speed, Math.pow(turn, 2) * Math.signum(turn));
      } else {
         Drivetrain.getInstance().arcadeDriveVelocity(speed, Math.pow(turn, 2) * Math.signum(turn));
      }
   }

   public void end() {
      Drivetrain.getInstance().getLeftMaster().set(ControlMode.Disabled, 0);
      Drivetrain.getInstance().getRightMaster().set(ControlMode.Disabled, 0);
   }
}