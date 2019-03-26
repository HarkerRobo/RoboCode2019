package frc.robot.commands.wrist;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.OI.TriggerMode;
import frc.robot.Robot;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Wrist;
import frc.robot.subsystems.Wrist.WristDirection;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;

/**
 * Moves the wrist manually.
 * 
 * @author Finn Frankis
 * @author Angela Jia
 * 
 * @since 1/10/19
 */
public class MoveWristManual extends IndefiniteCommand {
   private boolean isHolding;
   private boolean shouldClosedLoop;
   private double lastPos;

   public MoveWristManual() {
      requires(Wrist.getInstance());
      Robot.log("MoveWristManual constructed.");
      isHolding = false;
      shouldClosedLoop = false;
      lastPos = 0;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void initialize() {
      Wrist.getInstance().getMasterTalon().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
            Global.PID_PRIMARY);
      Wrist.getInstance().setupMotionMagic();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void execute() {
      double leftDriverTrigger = OI.getInstance().getDriverGamepad().getLeftTrigger();
      double rightDriverTrigger = OI.getInstance().getDriverGamepad().getRightTrigger();
      double rightOperatorY = MathUtil.mapJoystickOutput(OI.getInstance().getDriverGamepad().getRightY(), OI.OPERATOR_DEADBAND_JOYSTICK);

      double magnitude = 0;
      WristDirection direction;
      // double currentPosition =
      // Wrist.getInstance().getMasterTalon().getSelectedSensorPosition(Global.PID_PRIMARY);
      // if (OI.getInstance().getCurrentTriggerMode() == TriggerMode.WRIST_MANUAL && (leftDriverTrigger > OI.DRIVER_DEADBAND_TRIGGER
      //       || rightDriverTrigger > OI.DRIVER_DEADBAND_TRIGGER)) { 
         if (Math.abs(rightOperatorY) > 0) {
         isHolding = false;
         shouldClosedLoop = true;


         // if (leftDriverTrigger > rightDriverTrigger) {
         //    magnitude = 0.35 * leftDriverTrigger;
         //    direction = WristDirection.TO_BACK;
         // } else {
         //    magnitude = 0.35 * rightDriverTrigger;
         //    direction = WristDirection.TO_FRONT;
         // }

         Wrist.getInstance().setWristPercentOutput(rightOperatorY, WristDirection.TO_BACK);
      } else {
         if (!isHolding) {
            lastPos = Wrist.getInstance().getCurrentAngleEncoder();
         }
         isHolding = true;
      }

      if (isHolding && shouldClosedLoop) {
         Wrist.getInstance().setWrist(ControlMode.MotionMagic, lastPos);
         SmartDashboard.putNumber("Wrist Error", Wrist.getInstance().getMasterTalon().getClosedLoopError());
      }
   }

   public void disableClosedLoop() {
      shouldClosedLoop = false;
   }

   public void setLastPosition(double lastPos) {
      this.lastPos = lastPos;
   }

   /**
    * Sets the last position to be the current wrist position.
    */
   public void setLastPosition() {
      this.setLastPosition(Wrist.getInstance().getCurrentAngleEncoder());
   }
}