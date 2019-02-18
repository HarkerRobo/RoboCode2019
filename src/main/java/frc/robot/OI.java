package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.commands.arm.SetArmPosition;
import frc.robot.Robot.Side;
import frc.robot.commands.TestCommand;
import frc.robot.commands.arm.ToggleArmPosition;
import frc.robot.commands.drivetrain.AlignWithLimelightDrive;
import frc.robot.commands.elevator.MoveElevatorMotionMagic;
import frc.robot.commands.elevator.ZeroElevator;
import frc.robot.commands.groups.SetScoringPosition;
import frc.robot.commands.groups.SetScoringPosition.Location;
import frc.robot.commands.hatchpanelintake.LoadOrScoreHatch;
import frc.robot.commands.hatchpanelintake.StowHatchIntake;
import frc.robot.commands.hatchpanelintake.ToggleExtenderState;
import frc.robot.commands.hatchpanelintake.ToggleFlowerState;
import frc.robot.commands.hatchpanelintake.LoadOrScoreHatch.ScoreState;
import frc.robot.commands.intake.SpinIntakeIndefinite;
import frc.robot.commands.rollers.SpinRollersManual;
import frc.robot.commands.wrist.MoveWristPosition;
import frc.robot.commands.wrist.ZeroWrist;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Arm.ArmDirection;
import frc.robot.subsystems.Intake.IntakeDirection;
import frc.robot.commands.wrist.MoveWristMotionMagic;
import frc.robot.commands.wrist.ZeroWrist;
import frc.robot.util.CustomOperatorGamepad;
import frc.robot.util.Limelight;
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
    public static final double DRIVER_DEADBAND_TRIGGER = 0.15;
    public static final double OPERATOR_DEADBAND_JOYSTICK = 0.1;
    public static final double OPERATOR_DEADBAND_TRIGGER = 0.1;

    public static final boolean HAS_TWO_CONTROLLERS = true;

    //DPad angles in degrees
    public static final int DPAD_UP_ANGLE = 0;
    public static final int DPAD_LEFT_ANGLE = 270;
    public static final int DPAD_RIGHT_ANGLE = 90;
    public static final int DPAD_DOWN_ANGLE = 180;

    private int driverControlScheme = 0;
    private static final int NUM_DRIVERS = 2;
    private static final int CHRIS_CONTROL_SCHEME = 0;
    private static final int PRANAV_CONTROL_SCHEME = 1;
    private static final int ANGELA_CONTROL_SCHEME = 0;
    private static Driver driver;

    private static boolean cargoBayToggleMode;

    private OI() {
        driver = Driver.CHRIS;
        driverGamepad = new XboxGamepad(DRIVER_PORT);
        customOperatorGamepad = new CustomOperatorGamepad(OPERATOR_PORT);
        cargoBayToggleMode = false;
        initBindings();
    }
    
    public void initBindings() {
        //driverGamepad.getButtonX().whilePressed(new MoveWristPosition(170));
       // driverGamepad.getButtonY().whenPressed(new ZeroWrist());
        driverGamepad.getButtonA().whenPressed(new ZeroElevator());
        driverGamepad.getButtonB().whilePressed(new MoveElevatorMotionMagic(15000));
        driverGamepad.getButtonStart().whenPressed(new InstantCommand() {
            
            @Override
            public void initialize() {
                driverControlScheme++;
                initBindings();
            }
        // driverGamepad.getButtonA().whenPressed(new ZeroElevator());
        
        // driverGamepad.getButtonStart().whilePressed(new InstantCommand() {
            

        //     @Override
        //     public void initialize() {
        //         driverControlScheme++;
        //         initBindings();
        //         System.out.println("START");
        //     }

        });

        if(driverControlScheme % NUM_DRIVERS == CHRIS_CONTROL_SCHEME) {
            driver = Driver.CHRIS;
            driverGamepad = new XboxGamepad(DRIVER_PORT);
            
            driverGamepad.getButtonBumperLeft().whenPressed(new ToggleArmPosition());
            driverGamepad.getButtonB().whenPressed(new ToggleFlowerState());
            driverGamepad.getButtonA().whenPressed(new ToggleExtenderState());
            //driverGamepad.getButtonBumperRight().whenPressed(new StowHatchIntake());
           

        } else if(driverControlScheme % NUM_DRIVERS == PRANAV_CONTROL_SCHEME) {
            driver = Driver.PRANAV;
            driverGamepad = new XboxGamepad(DRIVER_PORT);
        driverGamepad = new XboxGamepad(DRIVER_PORT);
            
        driverGamepad.getButtonBumperLeft().whenPressed(new ToggleArmPosition());
        driverGamepad.getButtonB().whenPressed(new ToggleFlowerState());
        driverGamepad.getButtonA().whenPressed(new ToggleExtenderState());
        driverGamepad.getButtonX().whenPressed(new MoveWristMotionMagic(25));
        driverGamepad.getButtonBumperRight().whenPressed(new InstantCommand() {
            public void initialize() {
                driverGamepad.setRumble(RumbleType.kRightRumble, 1);
            }
        });

        driverGamepad.getButtonBumperRight().whenReleased(new InstantCommand() {
            public void initialize() {
                driverGamepad.setRumble(RumbleType.kRightRumble, 0);
            }
        });
        
        if(driverControlScheme % NUM_DRIVERS == CHRIS_CONTROL_SCHEME) {
            driver = Driver.CHRIS;
           
        
        } //else if(driverControlScheme % NUM_DRIVERS == PRANAV_CONTROL_SCHEME) {
        //     driver = Driver.PRANAV;
        //     driverGamepad = new XboxGamepad(DRIVER_PORT);
            
            driverGamepad.getButtonBumperRight().whileActive(new AlignWithLimelightDrive(Limelight.TX_SETPOINT));

            driverGamepad.getButtonB().whileActive(new SpinIntakeIndefinite(Intake.DEFAULT_INTAKE_MAGNITUDE, IntakeDirection.IN));

            driverGamepad.getButtonA().whenPressed(new ToggleArmPosition());

            driverGamepad.getButtonX().whenPressed(new ToggleFlowerState());
            driverGamepad.getButtonY().whenPressed(new ToggleExtenderState());
            
        } else if(driverControlScheme % NUM_DRIVERS == ANGELA_CONTROL_SCHEME) {
            driver = Driver.ANGELA;
            driverGamepad = new XboxGamepad(DRIVER_PORT);
            driverGamepad.getButtonA().whenPressed(new ToggleFlowerState());
            driverGamepad.getButtonBumperLeft().whenPressed(new ToggleExtenderState());
            driverGamepad.getButtonBumperRight().whenPressed(new AlignWithLimelightDrive(Limelight.TX_SETPOINT));
            driverGamepad.getDownDPadButton().whenPressed(new SetArmPosition(ArmDirection.UP));
            driverGamepad.getUpDPadButton().whenPressed(new SetArmPosition(ArmDirection.DOWN));

            driverGamepad.getButtonB().whenPressed(new SpinIntakeIndefinite(Intake.DEFAULT_INTAKE_MAGNITUDE, IntakeDirection.IN));
            
        } 

        customOperatorGamepad.getForwardOneButton().whenPressed(new SetScoringPosition(Location.F1));
        customOperatorGamepad.getForwardTwoButton().whenPressed(new SetScoringPosition(Location.F2));
        customOperatorGamepad.getForwardThreeButton().whenPressed(new SetScoringPosition(Location.F3));
        customOperatorGamepad.getBackwardOneButton().whenPressed(new SetScoringPosition(Location.B1));
        customOperatorGamepad.getBackwardTwoButton().whenPressed(new SetScoringPosition(Location.B2));
        customOperatorGamepad.getBackwardThreeButton().whenPressed(new SetScoringPosition(Location.B3));



        customOperatorGamepad.getZeroButton().whilePressed(new ZeroWrist());
        customOperatorGamepad.getIntakeHatchButton().whilePressed(new ZeroElevator());
         customOperatorGamepad.getStowButton().whenPressed(new SetScoringPosition(Location.F1, true));
        
        // customOperatorGamepad.getOuttakeButton().whenPressed(new InstantCommand() {
        //     @Override
        //     public void initialize() {
        //         cargoBayToggleMode = !cargoBayToggleMode;
        //         if(cargoBayToggleMode) {
        //             customOperatorGamepad.getForwardOneButton().whenPressed(new SetScoringPosition(Location.CARGO_SHIP_FRONT));
        //             customOperatorGamepad.getBackwardOneButton().whenPressed(new SetScoringPosition(Location.CARGO_SHIP_BACK));
        //         } else {
        //             customOperatorGamepad.getForwardOneButton().whenPressed(new SetScoringPosition(Location.F1));
        //             customOperatorGamepad.getBackwardOneButton().whenPressed(new SetScoringPosition(Location.B1));
        //         }
        //     }
        // });


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
}
