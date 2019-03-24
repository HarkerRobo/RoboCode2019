package frc.robot.commands.drivetrain;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;
import frc.robot.util.Limelight;

public class SetLimelightLEDMode extends InstantCommand {
   private LEDMode mode;

   public enum LEDMode {
      OFF(Limelight.LED_OFF), ON(Limelight.LED_ON);
      private int ledState;

      private LEDMode(int ledState) {
         this.ledState = ledState;
      }

      public int getLEDState() {
         return ledState;
      }
   }

   public SetLimelightLEDMode(LEDMode mode) {
      this.mode = mode;
      this.setRunWhenDisabled(true);
   }

   public void initialize() {
      Robot.log("Setting Limelight LEDs to " + mode.name());
      NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(mode.getLEDState());
   }
}