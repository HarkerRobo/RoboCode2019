package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.buttons.Trigger;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.commands.arm.ToggleArmPosition;
import frc.robot.commands.drivetrain.AlignWithLimelightDrive;
import frc.robot.commands.drivetrain.SetLimelightLEDMode;
import frc.robot.commands.drivetrain.SetLimelightViewMode;
import frc.robot.commands.drivetrain.ToggleLimelightLEDMode;
import frc.robot.commands.drivetrain.ToggleLimelightViewMode;
import frc.robot.commands.drivetrain.SetLimelightLEDMode.LEDMode;
import frc.robot.commands.drivetrain.SetLimelightViewMode;
import frc.robot.commands.drivetrain.SetLimelightViewMode.ViewMode;
import frc.robot.commands.elevator.ZeroElevator;
import frc.robot.commands.groups.SetScoringPosition;
import frc.robot.commands.groups.SetScoringPosition.Location;
import frc.robot.commands.groups.StowHatchAndCargoIntake;
import frc.robot.commands.groups.ZeroForMatch;
import frc.robot.commands.hatchpanelintake.ToggleExtenderState;
import frc.robot.commands.hatchpanelintake.ToggleFlowerState;
import frc.robot.commands.wrist.ZeroWrist;
import frc.robot.util.ConditionalCommand;
import frc.robot.util.CustomOperatorGamepad;
import frc.robot.util.TriggerButton;
import frc.robot.util.TriggerButton.TriggerSide;
import harkerrobolib.auto.SequentialCommandGroup;
import harkerrobolib.commands.IndefiniteCommand;
import harkerrobolib.wrappers.HSGamepad;
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

    private HSGamepad driverGamepad;
    private CustomOperatorGamepad customOperatorGamepad;
    private static OI instance;

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

    private OI() {
        driver = Driver.CHRIS;
        driverGamepad = new XboxGamepad(DRIVER_PORT);
        customOperatorGamepad = new CustomOperatorGamepad(OPERATOR_PORT);
        cargoBayToggleMode = false;
        wristToggleMode = false;
        driveStraightMode = false;
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
                }
                else {
                    OI.getInstance().getDriverGamepad().setRumble(RumbleType.kLeftRumble, 0);
                    OI.getInstance().getDriverGamepad().setRumble(RumbleType.kRightRumble, 0);
                }
            }
        });

        driverGamepad.getButtonBumperLeft().whenPressed(new ToggleArmPosition());
        driverGamepad.getButtonB().whenPressed(new ToggleFlowerState());
        driverGamepad.getButtonA().whenPressed(new ToggleExtenderState());
        driverGamepad.getButtonStart().whenPressed(new InstantCommand() {
            public void initialize() {
                wristToggleMode = !wristToggleMode;
            }
        });
        driverGamepad.getButtonSelect().whenPressed(new ToggleLimelightViewMode());
        driverGamepad.getUpDPadButton().whenPressed(new StowHatchAndCargoIntake());
        driverGamepad.getDownDPadButton().whenPressed(new ZeroForMatch());
        
        Trigger rightTrigger = new TriggerButton(driverGamepad, TriggerSide.RIGHT);
        rightTrigger.whileActive(new ConditionalCommand(() -> !wristToggleMode, 
                                                                        new SequentialCommandGroup (new SetLimelightLEDMode(LEDMode.ON),
                                                                            new SetLimelightViewMode(ViewMode.VISION),
                                                                            new AlignWithLimelightDrive(0.0)))); // align to center
        rightTrigger.whenInactive(new SequentialCommandGroup(new SetLimelightViewMode(ViewMode.DRIVER), new SetLimelightLEDMode(LEDMode.OFF)));
        Trigger leftTrigger = new TriggerButton(driverGamepad, TriggerSide.LEFT);
        leftTrigger.whenActive(new InstantCommand() {
            public void initialize() {
                if (!wristToggleMode) {
                    driveStraightMode = true;
                }
            }
        });

        leftTrigger.whenInactive(new InstantCommand() {
            public void initialize() {
                if (!wristToggleMode) {
                    driveStraightMode = false;
                }
            }
        });


        customOperatorGamepad.getForwardOneButton().whenPressed(new ConditionalCommand(() -> cargoBayToggleMode, new SetScoringPosition(Location.CARGO_SHIP_FRONT), new SetScoringPosition(Location.F1)));
        customOperatorGamepad.getBackwardOneButton().whenPressed(new ConditionalCommand(() -> cargoBayToggleMode, new SetScoringPosition(Location.CARGO_SHIP_BACK), new SetScoringPosition(Location.B1)));

        customOperatorGamepad.getForwardOneButton().whenPressed(new SetScoringPosition(Location.F1));
        customOperatorGamepad.getForwardTwoButton().whenPressed(new SetScoringPosition(Location.F2));
        customOperatorGamepad.getForwardThreeButton().whenPressed(new SetScoringPosition(Location.F3));
        customOperatorGamepad.getBackwardOneButton().whenPressed(new SetScoringPosition(Location.B1));
        customOperatorGamepad.getBackwardTwoButton().whenPressed(new SetScoringPosition(Location.B2));
        customOperatorGamepad.getBackwardThreeButton().whenPressed(new SetScoringPosition(Location.B3));


        customOperatorGamepad.getCargoIntakingButton().whenPressed(new SetScoringPosition(Location.CARGO_INTAKE, () -> false));
        customOperatorGamepad.getZeroWristButton().whilePressed(new ZeroWrist());
        customOperatorGamepad.getZeroElevatorButton().whilePressed(new ZeroElevator());
        customOperatorGamepad.getHatchIntakingButton().whenPressed(new SetScoringPosition(Location.HATCH_INTAKE, () -> true));
    }  
    
    public HSGamepad getDriverGamepad() {
        return driverGamepad;
    }

    public CustomOperatorGamepad getCustomOperatorGamepad() {
        return customOperatorGamepad;
    }

    public Driver getDriver() {
        return driver;
    }
    
    public static OI getInstance() {
        if(instance == null){
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
}
