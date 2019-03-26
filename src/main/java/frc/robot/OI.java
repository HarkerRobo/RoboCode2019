package frc.robot;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.InstantCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.commands.arm.ToggleArmPosition;
import frc.robot.commands.drivetrain.AlignWithLimelightDrive;
import frc.robot.commands.drivetrain.SetLimelightLEDMode;
import frc.robot.commands.drivetrain.SetLimelightLEDMode.LEDMode;
import frc.robot.commands.drivetrain.SetLimelightViewMode;
import frc.robot.commands.drivetrain.SetLimelightViewMode.ViewMode;
import frc.robot.commands.drivetrain.ToggleLimelightLEDMode;
import frc.robot.commands.elevator.ZeroElevator;
import frc.robot.commands.groups.SetScoringPosition;
import frc.robot.commands.groups.SetScoringPosition.Location;
import frc.robot.commands.groups.StowHatchAndCargoIntake;
import frc.robot.commands.hatchpanelintake.ToggleExtenderState;
import frc.robot.commands.hatchpanelintake.ToggleFlowerState;
import frc.robot.commands.intake.SpinIntakeVelocity;
import frc.robot.commands.rollers.SpinRollersIndefinite;
import frc.robot.commands.wrist.ZeroWrist;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeDirection;
import frc.robot.subsystems.Rollers;
import frc.robot.subsystems.Rollers.RollerDirection;
import frc.robot.util.ConditionalCommand;
import frc.robot.util.CustomOperatorGamepad;
import frc.robot.util.RunIfNotEqualCommand;
import frc.robot.util.TriggerButton;
import frc.robot.util.TriggerButton.TriggerSide;
import harkerrobolib.auto.ParallelCommandGroup;
import harkerrobolib.auto.SequentialCommandGroup;
import harkerrobolib.commands.CallMethodCommand;
import harkerrobolib.wrappers.HSGamepad;
import harkerrobolib.wrappers.LogitechAnalogGamepad;
import harkerrobolib.wrappers.XboxGamepad;

/**
 * Class that manages input and output from gamepads.
 * 
 * @since 1/7/19
 */
public class OI {
   public enum Driver {
      CHRIS(0), PRANAV(1), ANGELA(0);

      private int id;

      private Driver(int id) {
         this.id = id;
      }

      public int getId() {
         return id;
      }
   }

   public enum TriggerMode {
      ALIGN(0), WRIST_MANUAL(1), CLIMB(2);

      private int value;

      private TriggerMode(int value) {
         this.value = value;
      }

      public int getValue() {
         return value;
      }
   }

   /*
   * Represents the possible modes for driving. ARCADE_YX corresponds to an arcade drive using Y for speed and X for turn (
   * on the same joystick); ARCADE_YY corresponds to an arcade drive using Y for speed and Y for turn (on two different
   * joysticks).
   */
   public enum DriveMode {
      ARCADE_YX(0, () -> OI.getInstance().getDriverGamepad().getLeftY(), () -> OI.getInstance().getDriverGamepad().getLeftX()), 
      ARCADE_YY(1, () -> OI.getInstance().getDriverGamepad().getLeftY(), () -> OI.getInstance().getDriverGamepad().getRightY());

      private int value;
      private Supplier<Double> speedFunction;
      private Supplier<Double> turnFunction;
      private DriveMode (int value, Supplier<Double> speedFunction, Supplier<Double> turnFunction) {
         this.value = value;

         this.speedFunction = speedFunction;
         this.turnFunction = turnFunction;
      }

      public int getValue() {
         return value;
      }

      public static DriveMode getMode (int value) {
         for (DriveMode mode : DriveMode.values()) {
            if (mode.getValue() == value) { return mode; }
         }
         return null;
      }

      public Supplier<Double> getSpeedFunction () { return speedFunction; }
      public Supplier<Double> getTurnFunction () { return turnFunction; }
   }

   private HSGamepad driverGamepad;
   private CustomOperatorGamepad customOperatorGamepad;
   private static OI instance;
   private static HSGamepad operatorGamepad;

   private TriggerMode currentTriggerMode;

   private static final int DRIVER_PORT = 0;
   private static final int OPERATOR_PORT = 1;

   public static final double DRIVER_DEADBAND = 0.15;
   public static final double DRIVER_DEADBAND_TRIGGER = 0.16;
   public static final double OPERATOR_DEADBAND_JOYSTICK = 0.1;
   public static final double OPERATOR_DEADBAND_TRIGGER = 0.1;

   public static final boolean HAS_TWO_CONTROLLERS = true;

   // DPad angles in degrees
   public static final int DPAD_UP_ANGLE = 0;
   public static final int DPAD_LEFT_ANGLE = 270;
   public static final int DPAD_RIGHT_ANGLE = 90;
   public static final int DPAD_DOWN_ANGLE = 180;

   private static Driver driver;

   private boolean cargoBayToggleMode;
   private boolean wristToggleMode;
   private boolean driveStraightMode;

   public static DriveMode currentDriveMode;

   private OI() {
      driver = Driver.CHRIS;
      driverGamepad = new XboxGamepad(DRIVER_PORT);
      operatorGamepad = new LogitechAnalogGamepad(OPERATOR_PORT);
      //customOperatorGamepad = new CustomOperatorGamepad(OPERATOR_PORT);
      cargoBayToggleMode = false;
      wristToggleMode = false;
      driveStraightMode = false;
      currentTriggerMode = TriggerMode.ALIGN;
      currentDriveMode = DriveMode.ARCADE_YY;
      initBindings();
   }

   public void initBindings() {
      driverGamepad.getButtonBumperRight().whenPressed(new InstantCommand() {
         @Override
         public void initialize() {
            cargoBayToggleMode = !cargoBayToggleMode;

            if (cargoBayToggleMode) {
               OI.getInstance().getDriverGamepad().setRumble(RumbleType.kLeftRumble, 0.4);
               OI.getInstance().getDriverGamepad().setRumble(RumbleType.kRightRumble, 0.4);
            } else {
               OI.getInstance().getDriverGamepad().setRumble(RumbleType.kLeftRumble, 0);
               OI.getInstance().getDriverGamepad().setRumble(RumbleType.kRightRumble, 0);
            }
            Robot.log(cargoBayToggleMode ? "Enabled cargo ship mode." : "Enabled rocket mode.");
         }
      });

      driverGamepad.getButtonBumperLeft().whenPressed(new ToggleArmPosition());
      driverGamepad.getButtonB().whenPressed(new ToggleFlowerState());
      driverGamepad.getButtonA().whenPressed(new ToggleExtenderState());
      driverGamepad.getButtonY()
            .whilePressed(new ParallelCommandGroup(
                  new SpinIntakeVelocity( IntakeDirection.IN, Intake.DEFAULT_INTAKE_VELOCITY),
                  new SpinRollersIndefinite(Rollers.getInstance()::getRecommendedRollersInput, RollerDirection.IN)));
      driverGamepad.getButtonX()
            .whilePressed(new SpinRollersIndefinite(Rollers.getInstance()::getRecommendedRollersOutput, RollerDirection.OUT));
      driverGamepad.getButtonStart().whenPressed(new InstantCommand() {
         public void initialize() {
            wristToggleMode = !wristToggleMode;
            Robot.log(wristToggleMode ? "Enabled wrist scoring mode." : "Enabled Limelight align mode.");
         }
      });
        
      // driverGamepad.getButtonSelect()
      //      .whenPressed(new CallMethodCommand(() -> currentTriggerMode = TriggerMode.CLIMB));
      driverGamepad.getButtonSelect()
         .whenPressed(new ToggleLimelightLEDMode());
      driverGamepad.getButtonStart().whenPressed(new CallMethodCommand (() -> {
          if (currentTriggerMode == TriggerMode.ALIGN || currentTriggerMode == TriggerMode.WRIST_MANUAL) {
            currentTriggerMode = (currentTriggerMode == TriggerMode.ALIGN ? TriggerMode.WRIST_MANUAL : TriggerMode.ALIGN);
          } else {
            currentTriggerMode = TriggerMode.ALIGN; // reset to default value
          }
      }));

      driverGamepad.getUpDPadButton().whenPressed(new StowHatchAndCargoIntake());

      // driverGamepad.getDownDPadButton().whenPressed(new ZeroForMatch());

      Trigger rightTrigger = new TriggerButton(driverGamepad, TriggerSide.RIGHT);
      rightTrigger.whileActive(new ConditionalCommand(() -> currentTriggerMode == TriggerMode.ALIGN,
            new SequentialCommandGroup(new CallMethodCommand(() -> Robot.log("Aligning with Limelight.")),
                  new SetLimelightLEDMode(LEDMode.ON), new SetLimelightViewMode(ViewMode.VISION),
                  new AlignWithLimelightDrive(0.0)))); // align to center
      rightTrigger.whenInactive(new ConditionalCommand(() -> currentTriggerMode == TriggerMode.ALIGN, new SequentialCommandGroup(new SetLimelightViewMode(ViewMode.DRIVER),
            new SetLimelightLEDMode(LEDMode.OFF))));
      Trigger leftTrigger = new TriggerButton(driverGamepad, TriggerSide.LEFT);
      leftTrigger.whenActive(new InstantCommand() {
         public void initialize() {
            if (currentTriggerMode == TriggerMode.ALIGN) {
               driveStraightMode = true;
            } 
            Robot.log("Enabled drive straight mode.");
         }
      });

      leftTrigger.whenInactive(new InstantCommand() {
         public void initialize() {
            if (currentTriggerMode == TriggerMode.ALIGN) {
               driveStraightMode = false;
            }
            Robot.log("Disabled drive straight mode.");
         }
      });

      // customOperatorGamepad.getForwardOneButton().whenPressed(new ConditionalCommand(() -> cargoBayToggleMode,
      //       new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.CARGO_SHIP_FRONT),
      //             () -> Robot.getSetScoringCommand()),
      //       new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.F1), () -> Robot.getSetScoringCommand())));
      // customOperatorGamepad.getBackwardOneButton().whenPressed(new ConditionalCommand(() -> cargoBayToggleMode,
      //       new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.CARGO_SHIP_BACK),
      //             () -> Robot.getSetScoringCommand()),
      //       new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.B1), () -> Robot.getSetScoringCommand())));

      // customOperatorGamepad.getForwardOneButton().whenPressed(
      //       new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.F1), () -> Robot.getSetScoringCommand()));
      // customOperatorGamepad.getForwardTwoButton().whenPressed(
      //       new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.F2), () -> Robot.getSetScoringCommand()));
      // customOperatorGamepad.getForwardThreeButton().whenPressed(
      //       new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.F3), () -> Robot.getSetScoringCommand()));
      // customOperatorGamepad.getBackwardOneButton().whenPressed(
      //       new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.B1), () -> Robot.getSetScoringCommand()));
      // customOperatorGamepad.getBackwardTwoButton().whenPressed(
      //       new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.B2), () -> Robot.getSetScoringCommand()));
      // customOperatorGamepad.getBackwardThreeButton().whenPressed(
      //       new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.B3), () -> Robot.getSetScoringCommand()));

      // customOperatorGamepad.getCargoIntakingButton().whenPressed(new RunIfNotEqualCommand(
      //       () -> new SetScoringPosition(Location.CARGO_INTAKE, () -> false), () -> Robot.getSetScoringCommand()));
      // customOperatorGamepad.getZeroWristButton().whilePressed(new ZeroWrist());
      // customOperatorGamepad.getZeroElevatorButton().whilePressed(new ZeroElevator());
      // customOperatorGamepad.getHatchIntakingButton().whenPressed(new RunIfNotEqualCommand(
      //       () -> new SetScoringPosition(Location.HATCH_INTAKE, () -> true), () -> Robot.getSetScoringCommand()));

      operatorGamepad.getDownDPadButton().whenPressed(new ConditionalCommand(() -> cargoBayToggleMode,
            new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.CARGO_SHIP_FRONT),
                  () -> Robot.getSetScoringCommand()),
            new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.F1), () -> Robot.getSetScoringCommand())));
      operatorGamepad.getButtonA().whenPressed(new ConditionalCommand(() -> cargoBayToggleMode,
            new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.CARGO_SHIP_BACK),
                  () -> Robot.getSetScoringCommand()),
            new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.B1), () -> Robot.getSetScoringCommand())));

      operatorGamepad.getUpDPadButton().whenPressed(
            new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.F2), () -> Robot.getSetScoringCommand()));
      operatorGamepad.getButtonY().whenPressed(
            new RunIfNotEqualCommand(() -> new SetScoringPosition(Location.B2), () -> Robot.getSetScoringCommand()));

      operatorGamepad.getButtonBumperLeft().whenPressed(new RunIfNotEqualCommand(
            () -> new SetScoringPosition(Location.CARGO_INTAKE, () -> false), () -> Robot.getSetScoringCommand()));
      operatorGamepad.getButtonBumperRight().whenPressed(new RunIfNotEqualCommand(
         () -> new SetScoringPosition(Location.HATCH_INTAKE, () -> true), () -> Robot.getSetScoringCommand()));
      Trigger leftTriggerOperator = new TriggerButton(operatorGamepad, TriggerSide.LEFT);
      leftTriggerOperator.whileActive(new ZeroWrist());
      
      Trigger rightTriggerOperator = new TriggerButton(operatorGamepad, TriggerSide.RIGHT);
      rightTriggerOperator.whileActive(new ZeroElevator()); 
      
      operatorGamepad.getButtonSelect().whenPressed(new InstantCommand() {
                     public void initialize() {
                        Scheduler.getInstance().removeAll();
                     }
                                       });
      operatorGamepad.getButtonStart().whenPressed(new InstantCommand() {
         public void initialize() {
            currentDriveMode = DriveMode.getMode((currentDriveMode.getValue() + 1) % DriveMode.values().length);
         }
      });
   }

   public HSGamepad getDriverGamepad() {
      return driverGamepad;
   }

   public CustomOperatorGamepad getCustomOperatorGamepad() {
      return customOperatorGamepad;
   }

   public HSGamepad getOperatorGamepad() {
      return operatorGamepad;
   }

   public Driver getDriver() {
      return driver;
   }

   public static OI getInstance() {
      if (instance == null) {
         instance = new OI();
      }
      return instance;
   }

   public boolean getCargoBayToggleMode() {
      return cargoBayToggleMode;
   }

   public boolean getWristToggleMode() {
      return wristToggleMode;
   }

   public boolean getDriveStraightMode() {
      return driveStraightMode;
   }

   public TriggerMode getCurrentTriggerMode() {
      return currentTriggerMode;
   }
}
