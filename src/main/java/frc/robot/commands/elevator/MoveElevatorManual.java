package frc.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.Robot.Side;
import frc.robot.RobotMap.Global;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Wrist;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.util.MathUtil;

/**
 * Moves elevator based on driver right joystick input.
 * 
 * @author Angela Jia
 * @since 1/10/19
 */
public class MoveElevatorManual extends IndefiniteCommand {
   private boolean isHolding;
   private boolean shouldClosedLoop;
   private double lastPos;

   public MoveElevatorManual() {
      requires(Elevator.getInstance());
      Robot.log("MoveElevatorManual constructed.");
      isHolding = false;
      shouldClosedLoop = false;
      lastPos = 0;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void initialize() {
      Elevator.getInstance().getMasterTalon().configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
            Global.PID_PRIMARY);
      Elevator.getInstance().setUpMotionMagic();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void execute() {
      double desiredSpeed = MathUtil.mapJoystickOutput(OI.getInstance().getOperatorGamepad().getLeftY(),
            OI.DRIVER_DEADBAND);
      if (Math.abs(desiredSpeed) > 0) {
         isHolding = false;
         shouldClosedLoop = true;

         Elevator.getInstance().setElevator(ControlMode.PercentOutput, desiredSpeed);
      } else {
         if (!isHolding) {
            lastPos = Elevator.getInstance().getCurrentPositionEncoder();
         }
         isHolding = true;
      }

      boolean isWristPullingUp = false;//Elevator.getInstance().isAbove(Elevator.RAIL_POSITION)
            // && Wrist.getInstance().getCurrentSide() == Side.FRONT;
      if (isHolding && shouldClosedLoop && !isWristPullingUp) {
         Elevator.getInstance().setElevator(ControlMode.MotionMagic, lastPos);
      } else if (isWristPullingUp) {
         Elevator.getInstance().setElevator(ControlMode.Disabled, 0.0);
      }
   }

   public void disableClosedLoop() {
      shouldClosedLoop = false;
   }

   public void setLastPosition(double lastPos) {
      this.lastPos = lastPos;
   }

   /**
    * Sets the last position to be the current elevator position.
    */
   public void setLastPosition() {
      this.setLastPosition(Elevator.getInstance().getCurrentPositionEncoder());
   }
}