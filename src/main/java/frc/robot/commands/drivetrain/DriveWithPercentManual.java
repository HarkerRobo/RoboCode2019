package frc.robot.commands.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;

import frc.robot.OI;
import frc.robot.subsystems.Drivetrain;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;

/**
 * Drives based on driver gamepad joystick input.
 * 
 * @author Chirag Kaushik
 * @since 1/7/19
 */
public class DriveWithPercentManual extends IndefiniteCommand {

   public DriveWithPercentManual() {
      requires(Drivetrain.getInstance());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void initialize() {
      Drivetrain.getInstance().setBoth(ControlMode.PercentOutput, 0.0);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void execute() {
      double leftDriverX = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftX(),
            OI.DRIVER_DEADBAND);
      double leftDriverY = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getLeftY(),
            OI.DRIVER_DEADBAND);

      Drivetrain.getInstance().arcadeDrivePercentOutput(leftDriverY, leftDriverX);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void interrupted() {
      Drivetrain.getInstance().setBoth(ControlMode.Disabled, 0.0);
   }
}